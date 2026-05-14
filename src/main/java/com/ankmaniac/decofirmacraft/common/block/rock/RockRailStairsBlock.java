package com.ankmaniac.decofirmacraft.common.block.rock;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RockRailStairsBlock extends RockRailBlock {
    public static final MapCodec<RockRailBlock> CODEC = simpleCodec(RockRailStairsBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RockRailStairsBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        VoxelShape half = Block.box(0D, 0D, 0D, 16D, 8D, 16D);
        VoxelShape ne = Block.box(8D, 8D, 0D, 16D, 16D, 8D);
        VoxelShape nw = Block.box(0D, 8D, 0D, 8D, 16D, 8D);
        VoxelShape se = Block.box(8D, 8D, 8D, 16D, 16D, 16D);
        VoxelShape sw = Block.box(0D, 8D, 8D, 8D, 16D, 16D);
        return switch(state.getValue(FACING))
        {
            case NORTH -> Shapes.or(half, ne, nw);
            case EAST -> Shapes.or(half, ne, se);
            case SOUTH -> Shapes.or(half, se, sw);
            default -> Shapes.or(half, nw, sw);
        };
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos)
    {
        return true;
    }

    @Override
    protected BlockState updateDir(Level level, BlockPos pos, BlockState state, boolean alwaysPlace)
    {
        return state;
    }
    @Override
    protected BlockState updateState(BlockState state, Level level, BlockPos pos, boolean movedByPiston)
    {
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        BlockState blockstate = super.defaultBlockState();
        Direction direction = context.getHorizontalDirection();
        return blockstate
                .setValue(FACING, direction)
                .setValue(WATERLOGGED, flag)
                .setValue(getShapeProperty(), direction.getAxis() == Direction.Axis.X ?
                        direction == Direction.EAST ? RailShape.ASCENDING_EAST : RailShape.ASCENDING_WEST :
                        direction == Direction.NORTH ? RailShape.ASCENDING_NORTH : RailShape.ASCENDING_SOUTH);
    }

    @Override
    public boolean isValidRailShape(RailShape shape)
    {
        return shape.isAscending();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (context instanceof EntityCollisionContext entityContext)
        {
            Entity entity = entityContext.getEntity();

            if (entity instanceof AbstractMinecart)
            {
                return Shapes.empty();
            }
        }

        return super.getCollisionShape(state, level, pos, context);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SHAPE, WATERLOGGED, FACING);
    }
}
