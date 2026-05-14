package com.ankmaniac.decofirmacraft.common.block;

import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties;
import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties.ThreeSections;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

import static net.minecraft.core.Direction.Axis.X;

public class TallDoor extends ExtendedBlock
{
    public TallDoor(ExtendedProperties properties)
    {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(SECTION, ThreeSections.BOTTOM)
                        .setValue(OPEN, false)
                        .setValue(POWERED, false)
                        .setValue(HINGE, DoorHingeSide.LEFT)
        );
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ThreeSections> SECTION = DFCBlockStateProperties.THREE_SECTIONS;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final VoxelShape SOUTH_SHAPE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 3.0d);
    public static final VoxelShape NORTH_SHAPE = Block.box(0.0d, 0.0d, 13.0d, 16.0d, 16.0d, 16.0d);
    public static final VoxelShape WEST_SHAPE = Block.box(13.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d);
    public static final VoxelShape EAST_SHAPE = Block.box(0.0d, 0.0d, 0.0d, 3.0d, 16.0d, 16.0d);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(FACING, SECTION, OPEN, POWERED, HINGE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean open= !state.getValue(OPEN);
        boolean hinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        VoxelShape shape;
        switch (direction) {
            case SOUTH -> shape = open? SOUTH_SHAPE : (hinge ? EAST_SHAPE : WEST_SHAPE);
            case WEST -> shape = open? WEST_SHAPE : (hinge ? SOUTH_SHAPE : NORTH_SHAPE);
            case NORTH -> shape = open? NORTH_SHAPE : (hinge ? WEST_SHAPE : EAST_SHAPE);
            default -> shape = open? EAST_SHAPE : (hinge ? NORTH_SHAPE : SOUTH_SHAPE);
        }

        return shape;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        ThreeSections section = state.getValue(SECTION);
        if (facing.getAxis() == Direction.Axis.Y && (section == ThreeSections.BOTTOM ? facing == Direction.UP : section == ThreeSections.TOP ? facing == Direction.DOWN : true))
        {
            if (facingState.getBlock() instanceof TallDoor && facingState.getValue(SECTION) != section)
            {
                if (section == ThreeSections.MIDDLE)
                {
                    BlockPos otherPos = currentPos.relative(facing.getOpposite());
                    BlockState blockState = level.getBlockState(otherPos);
                    blockState.updateShape(facing, facingState.setValue(SECTION, section), level, otherPos, currentPos);
                }
                return facingState.setValue(SECTION, section);
            }
            else
            {
                return Blocks.AIR.defaultBlockState();
            }
        }
        else
        {
            return section == ThreeSections.BOTTOM && facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
    }

    @Override
    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && state.getValue(SECTION) == ThreeSections.BOTTOM && !state.getValue(POWERED)) {
            this.setOpen((Entity)null, level, state, pos, !this.isOpen(state));
        }

        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))) {
            preventDropFromBottomPart(level, pos, state, player);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        boolean pathFindable;
        switch (pathComputationType) {
            case LAND:
            case AIR:
                pathFindable = state.getValue(OPEN);
                break;
            case WATER:
                pathFindable = false;
                break;
            default:
                throw new MatchException((String)null, (Throwable)null);
        }

        return pathFindable;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        if (blockPos.getY() < level.getMaxBuildHeight() - 2 && level.getBlockState(blockPos.above()).canBeReplaced(context))
        {
            boolean powered = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above()) || level.hasNeighborSignal(blockPos.above(2));
            return (this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HINGE, this.getHinge(context)).setValue(POWERED, powered)).setValue(OPEN, powered).setValue(SECTION, ThreeSections.BOTTOM);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        level.setBlock(pos.above(), state.setValue(SECTION, ThreeSections.MIDDLE), 3);
        level.setBlock(pos.above(2), state.setValue(SECTION, ThreeSections.TOP), 3);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        state = state.cycle(OPEN);
        level.setBlock(pos, state, 10);
        this.playSound(player, level, pos, state.getValue(OPEN));
        level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    public boolean isOpen(BlockState state)
    {
        return state.getValue(OPEN);
    }

    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open)
    {
        if (state.is(this) && state.getValue(OPEN) != open)
        {
            level.setBlock(pos, state.setValue(OPEN, open), 10);
            this.playSound(entity, level, pos, open);
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        boolean powered = level.hasNeighborSignal(pos) || (state.getValue(SECTION) == ThreeSections.MIDDLE ? (level.hasNeighborSignal(pos.above())) || level.hasNeighborSignal(pos.below()) : level.hasNeighborSignal(pos.relative(state.getValue(SECTION) == ThreeSections.BOTTOM ? Direction.UP : Direction.DOWN)));
        if (!this.defaultBlockState().is(block) && powered != state.getValue(POWERED))
        {
            if (powered != state.getValue(OPEN))
            {
                this.playSound((Entity)null, level, pos, powered);
                level.gameEvent((Entity)null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
            level.setBlock(pos, state.setValue(POWERED, powered).setValue(OPEN, powered), 2);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        return state.getValue(SECTION) == ThreeSections.BOTTOM ? blockstate.isFaceSturdy(level, blockpos, Direction.UP) : state.getValue(SECTION) == ThreeSections.TOP ? blockstate.is(this) : (blockstate.is(this) && level.getBlockState(pos.above()).is(this));
    }

    @Override
    protected long getSeed(BlockState state, BlockPos pos)
    {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(SECTION) == ThreeSections.BOTTOM ? 0 : state.getValue(SECTION) == ThreeSections.MIDDLE ? 1 : 2).getY(), pos.getZ());
    }

    protected void playSound(@Nullable Entity source, Level level, BlockPos pos, boolean isOpening)
    {
        level.playSound(source, pos, isOpening ? BlockSetType.OAK.doorOpen() : BlockSetType.OAK.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player)
    {
        ThreeSections section = state.getValue(SECTION);
        if (section == ThreeSections.TOP)
        {
            BlockPos blockPos = pos.below();
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(state.getBlock()) && blockState.getValue(SECTION) == ThreeSections.MIDDLE)
            {
                BlockState replaceState = blockState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockPos, replaceState, 35);
                level.levelEvent(player, 2001, blockPos, Block.getId(blockState));
            }
            BlockPos blockPos1 = pos.below(2);
            BlockState blockState1 = level.getBlockState(blockPos1);
            if (blockState1.is(state.getBlock()) && blockState1.getValue(SECTION) == ThreeSections.BOTTOM)
            {
                BlockState replaceState = blockState1.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockPos1, replaceState, 35);
                level.levelEvent(player, 2001, blockPos1, Block.getId(blockState1));
            }
        }

    }

    //Update to also check blocks parallel with the top block
    protected DoorHingeSide getHinge(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        Direction directionClockWise = direction.getClockWise();
        Direction directionCounterClockWise = direction.getCounterClockWise();
        BlockGetter blockGetter = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos1 = blockPos.above();
        BlockPos blockPos2 = blockPos.relative(directionCounterClockWise);
        BlockPos blockPos3 = blockPos1.relative(directionCounterClockWise);
        BlockPos blockPos4 = blockPos.relative(directionClockWise);
        BlockPos blockPos5 = blockPos1.relative(directionClockWise);
        BlockState blockState = blockGetter.getBlockState(blockPos2);
        BlockState blockState1 = blockGetter.getBlockState(blockPos3);
        BlockState blockState2 = blockGetter.getBlockState(blockPos4);
        BlockState blockState3 = blockGetter.getBlockState(blockPos5);
        int i = (blockState.isCollisionShapeFullBlock(blockGetter, blockPos2) ? -1 : 0) + (blockState1.isCollisionShapeFullBlock(blockGetter, blockPos3) ? -1 : 0) + (blockState2.isCollisionShapeFullBlock(blockGetter, blockPos4) ? 1 : 0) + (blockState3.isCollisionShapeFullBlock(blockGetter, blockPos5) ? 1 : 0);
        boolean flag = blockState.getBlock() instanceof TallDoor && blockState.getValue(SECTION) == ThreeSections.BOTTOM;
        boolean flag1 = blockState2.getBlock() instanceof TallDoor && blockState2.getValue(SECTION) == ThreeSections.BOTTOM;
        if ((!flag || flag1) && i <= 0)
        {
            if ((!flag1 || flag) && i >= 0)
            {
                int j = direction.getStepX();
                int k = direction.getStepZ();
                Vec3 vec3 = context.getClickLocation();
                double d0 = vec3.x - (double)blockPos.getX();
                double d1 = vec3.z - (double)blockPos.getZ();
                return j < 0 && d1 < 0.5d || j > 0 && d1 > 0.5d || k < 0 && d0 > 0.5d || k > 0 && d0 < 0.5d ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
            }
            else
            {
                return DoorHingeSide.LEFT;
            }
        }
        else
        {
            return DoorHingeSide.RIGHT;
        }
    }
    
}
