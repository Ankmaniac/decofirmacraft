package com.ankmaniac.decofirmacraft.common.block.rock;

import com.ankmaniac.decofirmacraft.common.block.IDFCRail;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RockRailBlock extends BaseRailBlock implements IDFCRail
{
    public static final MapCodec<RockRailBlock> CODEC = simpleCodec(RockRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;

    private final float kineticFrictionCoefficient = 0.45F;

    public RockRailBlock(Properties properties)
    {
        super(false, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false));
    }

    @Override
    public MapCodec<RockRailBlock> codec()
    {
        return CODEC;
    }

    @Override
    public Property<RailShape> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos)
    {
        return false;
    }

    @Override
    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart)
    {
        if (cart instanceof MinecartFurnace)
        {
            return cart.isInWater() ? 0.05F : 0.1F;
        }
        else
        {
            return cart.isInWater() ? 0.1F : 0.2F;
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.block();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return true;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (!level.isClientSide && level.getBlockState(pos).is(this))
        {
            this.updateState(state, level, pos, block);
        }
    }

    @Override
    protected void updateState(BlockState state, Level level, BlockPos pos, Block block)
    {
        if (block.defaultBlockState().isSignalSource())
        {
            this.updateDir(level, pos, state, false);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }

    @Override
    public float getKineticFrictionCoefficient() {
        return this.kineticFrictionCoefficient;
    }

    @Override
    public float getStaticFrictionCoefficient() {
        return 0;
    }

    @Override
    public BlockPos nextRailPos(boolean negative, BlockPos currentRailPos) {
        return null;
    }

    @Override
    public double getLength() {
        return 0;
    }
}
