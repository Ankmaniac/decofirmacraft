package com.redstoneguy10ls.decofirmacraft.common.blocks.metal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public GateBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(OPEN, false)
                        .setValue(HALF, Half.BOTTOM)
                        .setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, HALF, OPEN, POWERED));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        final BlockState state = super.getStateForPlacement(ctx);
        if (state != null)
        {
            final Direction facing = ctx.getHorizontalDirection().getOpposite();
            final Level level = ctx.getLevel();
            final BlockPos pos = ctx.getClickedPos();
            final BlockPos pos1 = pos.above();
            final boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos1);
            return this.defaultBlockState().setValue(FACING, facing).setValue(OPEN, flag).setValue(HALF, Half.BOTTOM).setValue(POWERED, flag);
        }
        return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        if (block != this && flag != state.getValue(POWERED)) {
            if (flag != state.getValue(OPEN)) {
                state = state.setValue(OPEN, flag);
                level.setBlock(pos, state, 10);
            }

            level.setBlock(pos, state.setValue(POWERED, flag), 2);
            if (state.getValue(HALF) == Half.BOTTOM) {
                level.setBlock(pos.above(), state.setValue(HALF, Half.TOP), 2);
            } else {
                level.setBlock(pos.below(), state.setValue(HALF, Half.BOTTOM), 2);
            }
        }
    }

    //incomplete finish later
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction direction = state.getValue(FACING);
        boolean flag = state.getValue(OPEN);
        if (flag) {
            switch(direction) {
                case EAST:
                default:
                    return Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 2.0D)
                    , Block.box(0.0D, 0.0D, 14.0D, 8.0D, 16.0D, 16.0D));
                case SOUTH:
                    return Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 8.0D)
                            , Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D));
                case WEST:
                    return Shapes.or(Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D)
                            , Block.box(8.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D));
                case NORTH:
                    return Shapes.or(Block.box(0.0D, 0.0D, 8.0D, 2.0D, 16.0D, 16.0D)
                            , Block.box(14.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D));
            }
        } else {
            switch(direction) {
                case EAST:
                default:
                    return Shapes.or(Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
                case SOUTH:
                    return Shapes.or(Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D));
                case WEST:
                    return Shapes.or(Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
                case NORTH:
                    return Shapes.or(Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D));
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        state = state.cycle(OPEN);
        world.setBlock(pos, state, 10);
        world.playSound(null, pos, state.getValue(OPEN) ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);

        BlockPos otherHalfPos = state.getValue(HALF) == Half.BOTTOM ? pos.above() : pos.below();
        BlockState otherHalfState = world.getBlockState(otherHalfPos);
        if (otherHalfState.getBlock() == this) {
            otherHalfState = otherHalfState.cycle(OPEN);
            world.setBlock(otherHalfPos, otherHalfState, 10);
            world.playSound(null, otherHalfPos, otherHalfState.getValue(OPEN) ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getValue(HALF) == Half.BOTTOM) {
            BlockPos blockpos = pos.above();
            if (world.getBlockState(blockpos).isAir()) {
                world.setBlock(blockpos, state.setValue(HALF, Half.TOP), 3);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide && state.getBlock() != newState.getBlock()) {
            if (state.getValue(HALF) == Half.BOTTOM) {
                BlockPos blockpos = pos.above();
                BlockState blockstate = world.getBlockState(blockpos);
                if (blockstate.getBlock() == this && blockstate.getValue(HALF) == Half.TOP) {
                    world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    world.levelEvent(null, 2001, blockpos, Block.getId(blockstate));
                }
            } else {
                BlockPos blockpos = pos.below();
                BlockState blockstate = world.getBlockState(blockpos);
                if (blockstate.getBlock() == this && blockstate.getValue(HALF) == Half.BOTTOM) {
                    world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    world.levelEvent(null, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}