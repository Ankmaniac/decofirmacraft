package com.ankmaniac.decofirmacraft.common.block;

import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties;
import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties.ThreeSections;
import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties.DoorSide;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class LargeDoor extends TallDoor
{
    public LargeDoor(ExtendedProperties properties)
    {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(SECTION, ThreeSections.BOTTOM)
                        .setValue(OPEN, false)
                        .setValue(POWERED, false)
                        .setValue(HINGE, DoorHingeSide.LEFT)
                        .setValue(DOOR_SIDE, DoorSide.HINGE)
        );
    }

    public static final EnumProperty<DoorSide> DOOR_SIDE = DFCBlockStateProperties.DOOR_SIDE;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(DOOR_SIDE));
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
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        ThreeSections section = state.getValue(SECTION);
        DoorSide side = state.getValue(DOOR_SIDE);
//        boolean open = state.getValue(OPEN);
//        DoorHingeSide hinge = state.getValue(HINGE);
//        Direction direction = state.getValue(FACING);
//        final Direction adjacentDirection =
////                side.ordinal() == 0 ? open ? direction.getOpposite() : hinge.ordinal() == 0 ? direction.getCounterClockWise() : direction.getClockWise() : open ? hinge.ordinal() == 0 ? direction.getClockWise() : direction.getOpposite() : direction;
//                open ? side.ordinal() == 0 ? direction.getOpposite() : direction : hinge.ordinal() == 0 ? side.ordinal() == 0 ? direction.getCounterClockWise() : direction.getClockWise() : side.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise();
//        if ((facing.getAxis() == Direction.Axis.Y && (section == ThreeSections.BOTTOM ? facing == Direction.UP : section == ThreeSections.TOP ? facing == Direction.DOWN : true) || facing.getAxis() == Direction.Axis.X && facing == adjacentDirection))
//        {
//            if (facingState.getBlock() instanceof  LargeDoor)System.out.println("DEBUG FACINGSTATE = " + facingState.getValue(FACING) + facingState.getValue(SECTION) + facingState.getValue(DOOR_SIDE));
//
//            if (facingState.getBlock() instanceof LargeDoor && (facingState.getValue(DOOR_SIDE) != side || facingState.getValue(SECTION) != section))
//            {
//                System.out.println("DEBUG X9");
//                return state;
//            }
//            else
//            {
//                if (canSurvive(state, level, currentPos))
//                {
//                    System.out.println("DEBUG XX PASS");
//                }
//                else
//                {
//                    System.out.println("DEBUG XX1 BREAK");
//                    if (side.ordinal() == 0)
//                    {
//                        return Blocks.AIR.defaultBlockState();
//                    }
//                    else
//                    {
//                        return state;
//                    }
//                    //preventDropFromBottomPart((Level) level, currentPos, state, (Player) null);
//                    //return Blocks.AIR.defaultBlockState();
//                }
//            }
//        }
//        else
//        {
//            System.out.println("DEBUG X13");
//            }
//        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        if (section == ThreeSections.BOTTOM && side == DoorSide.HINGE && facing == Direction.DOWN && !state.canSurvive(level, currentPos))
        {
            preventDropFromBottomPart((Level) level, currentPos, state, (Player) null);
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    private boolean canOpen(BlockState state, LevelAccessor level, BlockPos currentPos)
    {
        ThreeSections section = state.getValue(SECTION);
        DoorSide side = state.getValue(DOOR_SIDE);
        boolean open = state.getValue(OPEN);
        DoorHingeSide hinge = state.getValue(HINGE);
        Direction direction = state.getValue(FACING);
        final Direction checkDirection1 = open ? hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise() : direction;
        final Direction checkDirection2 = open ? direction : hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise();
        BlockPos doorOrigin = ThreeSections.getBasePos(section, DoorSide.getBasePos(side, direction, hinge, open, currentPos));
        boolean canOpen = true;
        for (int i = 0; i < 6; i++)
        {
            if (!level.getBlockState(doorOrigin.above(i % 3).relative(checkDirection1).relative(checkDirection2, i < 3 ? 0 : 1)).canBeReplaced())
            {
                canOpen = false;
                break;
            }
        }
        System.out.println("DEBUG X " + canOpen);
        return true;
//        return canOpen;
    }

    private void updateDoor(BlockState state, LevelAccessor level, BlockPos currentPos, boolean open)
    {
        ThreeSections section = state.getValue(SECTION);
        DoorSide side = state.getValue(DOOR_SIDE);
        DoorHingeSide hinge = state.getValue(HINGE);
        Direction direction = state.getValue(FACING);
        BlockPos doorOrigin = ThreeSections.getBasePos(section, DoorSide.getBasePos(side, direction, hinge, state.getValue(OPEN), currentPos));

        System.out.println("DEBUG UPDATEDOOR " + currentPos);
        System.out.println("DEBUG UPDATEDOOR " + doorOrigin);
        System.out.println("DEBUG X8");
        for(int i = 0; i < 6; i++)
        {
            ThreeSections otherSection = ThreeSections.getSectionFromInt(i);
            DoorSide otherSide = i < 3 ? DoorSide.HINGE : DoorSide.KNOB;

            System.out.println("DEBUG UPDATEDOOR " + i);
            if (i < 3)
            {
                BlockPos otherPos = doorOrigin.above(i % 3);
                System.out.println("DEBUG UPDATEDOOR " + otherPos);
                if (!otherPos.equals(currentPos))
                {
                    level.setBlock(otherPos, state.setValue(SECTION, otherSection).setValue(DOOR_SIDE, otherSide).setValue(OPEN, open), 3);
                }
            }
            else
            {
                final Direction adjacentDirection1 = !open ? direction : hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise();
                final Direction adjacentDirection2 = !open ? hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise() : direction;

                BlockPos otherPos1 = doorOrigin.above(i % 3).relative(adjacentDirection1);
                BlockPos otherPos2 = doorOrigin.above(i % 3).relative(adjacentDirection2);

                if (!otherPos1.equals(currentPos))
                {
                    level.setBlock(otherPos2, state.setValue(SECTION, otherSection).setValue(DOOR_SIDE, otherSide).setValue(OPEN, open), 3);
                    level.setBlock(otherPos1, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer)
    {
        if (explosion.canTriggerBlocks() && state.getValue(SECTION) == ThreeSections.BOTTOM && !state.getValue(POWERED))
        {
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
            System.out.println("DEBUG 1");
            boolean powered = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above()) || level.hasNeighborSignal(blockPos.above(2));
            return (this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HINGE, this.getHinge(context)).setValue(POWERED, powered)).setValue(OPEN, powered).setValue(SECTION, ThreeSections.BOTTOM).setValue(DOOR_SIDE, DoorSide.HINGE);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        System.out.println("DEBUG 4");
        for (int i = 1; i < 6; i++)
        {
            if (i < 3)
            {
                level.setBlock(pos.above(i % 3), state.setValue(SECTION, ThreeSections.getSectionFromInt(i)).setValue(DOOR_SIDE, DoorSide.HINGE), 3);
            }
            else
            {
                final Direction facing = state.getValue(FACING);
                final DoorHingeSide hinge = state.getValue(HINGE);
                final boolean open = state.getValue(OPEN);
                level.setBlock(pos.above(i % 3).relative(open ? facing : hinge.ordinal() == 0 ? facing.getClockWise() : facing.getCounterClockWise()), state.setValue(SECTION, ThreeSections.getSectionFromInt(i)).setValue(DOOR_SIDE, DoorSide.KNOB), 3);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        ThreeSections section = state.getValue(SECTION);
        DoorSide side = state.getValue(DOOR_SIDE);
        boolean open = state.getValue(OPEN);
        DoorHingeSide hinge = state.getValue(HINGE);
        Direction direction = state.getValue(FACING);
        final boolean canOpen = canOpen(state, level, pos);
        if (canOpen)
        {
            updateDoor(state, level, pos, !open);
            if (side == DoorSide.KNOB)
            {
                Direction replaceDirection = open ? hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise() : direction;
                BlockPos hingePos = DoorSide.getBasePos(side, direction, hinge, open, pos);
                BlockPos replacePos = hingePos.relative(replaceDirection);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                level.setBlock(replacePos, state.setValue(OPEN, !open), 10);
                System.out.println("DEBUG X7");
            }
            else
            {
                System.out.println("DEBUG X6");
                level.setBlock(pos, state.setValue(OPEN, !open), 10);
            }
            System.out.println("DEBUG X1");
            this.playSound(player, level, pos, state.getValue(OPEN));
            level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        System.out.println("DEBUG X2");
        return InteractionResult.PASS;
    }


    public boolean isOpen(BlockState state)
    {
        return state.getValue(OPEN);
    }

    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open)
    {
        if (state.is(this) && state.getValue(OPEN) != open)
        {
            ThreeSections section = state.getValue(SECTION);
            DoorSide side = state.getValue(DOOR_SIDE);
            boolean open1 = state.getValue(OPEN);
            DoorHingeSide hinge = state.getValue(HINGE);
            Direction direction = state.getValue(FACING);
            state = state.cycle(OPEN);
            if (side == DoorSide.KNOB)
            {
                Direction replaceDirection = open1 ? hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise() : direction;
                BlockPos hingePos = DoorSide.getBasePos(side, direction, hinge, open1, pos);
                BlockPos replacePos = hingePos.relative(replaceDirection);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                level.setBlock(replacePos, state, 10);
            }
            else
            {
                level.setBlock(pos, state, 10);
            }
            updateDoor(state, level, pos, open);
            this.playSound(entity, level, pos, open);
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        System.out.println("DEBUG X3");
        boolean powered = level.hasNeighborSignal(pos) || (state.getValue(SECTION) == ThreeSections.MIDDLE ? (level.hasNeighborSignal(pos.above())) || level.hasNeighborSignal(pos.below()) : level.hasNeighborSignal(pos.relative(state.getValue(SECTION) == ThreeSections.BOTTOM ? Direction.UP : Direction.DOWN)));
        if (!this.defaultBlockState().is(block) && powered != state.getValue(POWERED))
        {
            if (powered != state.getValue(OPEN))
            {
                System.out.println("DEBUG X4");
                this.playSound((Entity)null, level, pos, powered);
                level.gameEvent((Entity)null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
            System.out.println("DEBUG X5");
            updateDoor(state, level, pos, powered);
            level.setBlock(pos, state.setValue(POWERED, powered).setValue(OPEN, powered), 2);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        final ThreeSections section = state.getValue(SECTION);
        final DoorSide side = state.getValue(DOOR_SIDE);
        final DoorHingeSide hinge = state.getValue(HINGE);
        final Direction facing = state.getValue(FACING);
        final boolean open = state.getValue(OPEN);
        final BlockPos blockPosUp = pos.above();
        final BlockState blockStateUp = level.getBlockState(blockPosUp);
        final BlockPos blockPosDown = pos.below();
        final BlockState blockStateDown = level.getBlockState(blockPosDown);
        final BlockPos blockPosAdjacent = pos.relative(open ? side.ordinal() == 0 ? facing.getOpposite() : facing : hinge.ordinal() == 0 ? side.ordinal() == 0 ? facing.getCounterClockWise() : facing.getClockWise() : side.ordinal() == 0 ? facing.getClockWise() : facing.getCounterClockWise());
        final BlockState blockStateAdjacent = level.getBlockState(blockPosAdjacent);
        boolean canSurvive = true;
        switch (section)
        {
            case BOTTOM:
            {
                if (side == DoorSide.HINGE)
                {
                    if (!blockStateDown.isFaceSturdy(level, blockPosDown, Direction.UP))
                    {
                        System.out.println("DEBUG 5");
                        canSurvive = false;
                    }
                    break;
                }
                else
                {
                    if (!blockStateAdjacent.is(this) || !blockStateUp.is(this))
                    {
                        System.out.println("DEBUG 6");
                        canSurvive = false;
                    }
                    break;
                }
            }
            case MIDDLE:
            {
                if (!blockStateAdjacent.is(this) || !blockStateUp.is(this) || !blockStateDown.is(this))
                {
                    System.out.println("DEBUG 7");
                    canSurvive = false;
                }
                break;
            }
            case TOP:
            {
                if (!blockStateAdjacent.is(this) || !blockStateDown.is(this))
                {
                    System.out.println("DEBUG 2");
                    canSurvive = false;
                }
                break;
            }
        }
        System.out.println("DEBUG 3 " + canSurvive);
        return canSurvive;
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

    protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, @Nullable Player player)
    {
        System.out.println("DEBUG BREAKDOOR");
        ThreeSections section = state.getValue(SECTION);
        DoorSide side = state.getValue(DOOR_SIDE);
        boolean open = state.getValue(OPEN);
        DoorHingeSide hinge = state.getValue(HINGE);
        Direction direction = state.getValue(FACING);
        BlockPos doorOrigin = ThreeSections.getBasePos(section, DoorSide.getBasePos(side, direction, hinge, open, pos));
        for (int i = 0; i < 6; i++)
        {
            final Direction adjacentDirection1 = open ? direction : hinge.ordinal() == 0 ? direction.getClockWise() : direction.getCounterClockWise();
            BlockPos otherPos = doorOrigin.above(i % 3).relative(adjacentDirection1, i < 3 ? 0 : 1);
            if (!otherPos.equals(pos))
            {
                BlockState otherBlockState = level.getBlockState(otherPos);
                if (otherBlockState.is(state.getBlock()))
                {
                    BlockState replaceState = otherBlockState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    level.setBlock(otherPos, replaceState, 35);
                    level.levelEvent(player, 2001, otherPos, Block.getId(otherBlockState));
                }
            }
        }
    }

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
