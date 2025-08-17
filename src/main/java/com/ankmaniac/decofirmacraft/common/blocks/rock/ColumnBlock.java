package com.ankmaniac.decofirmacraft.common.blocks.rock;

import com.ankmaniac.decofirmacraft.util.DFCTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ColumnBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final EnumProperty<ColumnProperties> STYLE = EnumProperty.create("style", ColumnProperties.class);

    private static final Random RANDOM = new Random();

    public ColumnBlock(Properties properties){
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(UP, true)
                        .setValue(DOWN, true)
                        .setValue(FACING, Direction.NORTH)
                        .setValue(STYLE, ColumnProperties.DORIC)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, UP, DOWN, STYLE));
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
            final BlockState aboveState = level.getBlockState(pos.above());
            final BlockState belowState = level.getBlockState(pos.below());

            ColumnProperties style = ColumnProperties.DORIC;

            if (aboveState.getBlock() instanceof ColumnBlock) {
                style = aboveState.getValue(STYLE);
            } else if (belowState.getBlock() instanceof ColumnBlock) {
                style = belowState.getValue(STYLE);
            }

            return this.defaultBlockState()
                    .setValue(STYLE, style)
                    .setValue(FACING, facing);
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        boolean up = state.getValue(UP);
        boolean down = state.getValue(DOWN);


        VoxelShape topShape1 = Shapes.box(1f/16f, 14f/16f, 1f/16f, 15f/16f, 1.0, 15f/16f);
        VoxelShape topShape2 = Shapes.box(2f/16f, 13f/16f, 2f/16f, 14f/16f, 14f/16f, 14f/16f);
        VoxelShape bottomShape1 = Shapes.box(1f/16f, 0.0, 1f/16f, 15f/16f, 2f/16f, 15f/16f);
        VoxelShape bottomShape2 = Shapes.box(2f/16f, 2f/16f, 2f/16f, 14f/16f, 3f/16f, 14f/16f);
        VoxelShape middleShape = Shapes.box(3f/16f, 0.0, 3f/16f, 13f/16f, 1.0, 13f/16f);

        if (up && down) {
            return Shapes.or(topShape1, topShape2, bottomShape1, bottomShape2, middleShape);
        } else if (up) {
            return Shapes.or(topShape1, topShape2, middleShape);
        } else if (down) {
            return Shapes.or(bottomShape1, bottomShape2, middleShape);
        } else{
            return middleShape;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (Helpers.isItem(player.getItemInHand(hand), TFCTags.Items.HAMMERS))
        {
            level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

            int particleCount = 100;
            for (int i = 0; i < particleCount; i++) {
                double speedX = RANDOM.nextDouble() * 20 - 10;
                double speedZ = RANDOM.nextDouble() * 20 - 10;
                double offsetY = RANDOM.nextDouble() * 1;
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + offsetY, pos.getZ() + 0.5, speedX, -0.1, speedZ);
            }

            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).swing(hand, true);
            }

            ColumnProperties currentStyle = state.getValue(STYLE);
            ColumnProperties newStyle = ColumnProperties.values()[(currentStyle.ordinal() + 1) % ColumnProperties.values().length];

            updateColumn(level, pos, newStyle);
        }
        return InteractionResult.PASS;
    }

    private void updateColumn(Level level, BlockPos pos, ColumnProperties newStyle) {
        // Update blocks upwards
        for (int y = pos.getY(); y < level.getMaxBuildHeight(); y++) {
            BlockPos currentPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState currentState = level.getBlockState(currentPos);
            if (currentState.getBlock() instanceof ColumnBlock) {
                level.setBlock(currentPos, currentState.setValue(STYLE, newStyle), 3);
            } else {
                break;
            }
        }

        // Update blocks downwards
        for (int y = pos.getY(); y >= level.getMinBuildHeight(); y--) {
            BlockPos currentPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState currentState = level.getBlockState(currentPos);
            if (currentState.getBlock() instanceof ColumnBlock) {
                level.setBlock(currentPos, currentState.setValue(STYLE, newStyle), 3);
            } else {
                break;
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.UP)
        {
            state = state.setValue(UP, !connects(facingState));
        }
        else if (facing == Direction.DOWN)
        {
            state = state.setValue(DOWN, !connects(facingState));
        }
        return state;
    }

    public boolean connects(BlockState adjacent)
    {
        return Helpers.isBlock(adjacent, DFCTags.Blocks.ROCK_COLUMNS);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }


}
