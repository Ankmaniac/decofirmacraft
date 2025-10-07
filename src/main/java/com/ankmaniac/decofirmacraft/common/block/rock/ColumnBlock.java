package com.ankmaniac.decofirmacraft.common.block.rock;

import com.ankmaniac.decofirmacraft.util.DFCTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

import static net.minecraft.core.Direction.Axis.X;

public class ColumnBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final EnumProperty<ColumnStyles> STYLE = EnumProperty.create("style", ColumnStyles.class);

    private static final Random RANDOM = new Random();

    public ColumnBlock(Properties properties){
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(UP, true)
                        .setValue(DOWN, true)
                        .setValue(AXIS, X)
                        .setValue(STYLE, ColumnStyles.DORIC)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS, UP, DOWN, STYLE));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        final BlockState state = super.getStateForPlacement(ctx);
        if (state != null)
        {
            final Direction.Axis axis = ctx.getHorizontalDirection().getAxis();
            final Level level = ctx.getLevel();
            final BlockPos pos = ctx.getClickedPos();
            final BlockState aboveState = level.getBlockState(pos.above());
            final BlockState belowState = level.getBlockState(pos.below());

            ColumnStyles style = ColumnStyles.DORIC;

            if (aboveState.getBlock() instanceof ColumnBlock) {
                style = aboveState.getValue(STYLE);
            } else if (belowState.getBlock() instanceof ColumnBlock) {
                style = belowState.getValue(STYLE);
            }

            return this.defaultBlockState()
                    .setValue(STYLE, style)
                    .setValue(AXIS, axis);
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        boolean up = state.getValue(UP);
        boolean down = state.getValue(DOWN);
        ColumnStyles style = state.getValue(STYLE);
        Direction.Axis axis = state.getValue(AXIS);

        VoxelShape topShape = Shapes.box(1f/16f, 13f/16f, 1f/16f, 15f/16f, 1.0, 15f/16f);
        VoxelShape topShapeIonicX = Shapes.or(
                Block.box(1.0D, 12.0D, 1.0D, 3.0D, 13.0D, 5.0D),
                Block.box(1.0D, 12.0D, 11.0D, 3.0D, 13.0D, 15.0D),
                Block.box(13.0D, 12.0D, 1.0D, 15.0D, 13.0D, 5.0D),
                Block.box(13.0D, 12.0D, 11.0D, 15.0D, 13.0D, 15.0D));
        VoxelShape topShapeIonicZ = Shapes.or(
                Block.box(1.0D, 12.0D, 1.0D, 5.0D, 13.0D, 3.0D),
                Block.box(11.0D, 12.0D, 1.0D, 15.0D, 13.0D, 3.0D),
                Block.box(1.0D, 12.0D, 13.0D, 5.0D, 13.0D, 15.0D),
                Block.box(11.0D, 12.0D, 13.0D, 15.0D, 13.0D, 15.0D));
        VoxelShape topShapeCorinthian = Shapes.box(2f/16f, 7f/16f, 2f/16f, 14f/16f, 13f/16f, 14f/16f);
        VoxelShape bottomShape = Shapes.box(1f/16f, 0.0, 1f/16f, 15f/16f, 3f/16f, 15f/16f);
        VoxelShape middleShape = Shapes.box(3f/16f, 0.0, 3f/16f, 13f/16f, 1.0, 13f/16f);

        if (up && down) {
            switch (style) {
                case DORIC, TUSCAN:
                default:
                    return Shapes.or(topShape, middleShape, bottomShape);
                case IONIC:
                    if (axis == X){
                        return Shapes.or(topShape, topShapeIonicX, middleShape, bottomShape);
                    }
                    else{
                        return Shapes.or(topShape, topShapeIonicZ, middleShape, bottomShape);
                    }
                case CORINTHIAN:
                    return Shapes.or(topShape, topShapeCorinthian, middleShape, bottomShape);
            }
        } else if (up) {
            switch (style) {
                case DORIC, TUSCAN:
                default:
                    return Shapes.or(topShape, middleShape);
                case IONIC:
                    if (axis == X){
                        return Shapes.or(topShape, topShapeIonicX, middleShape);
                    }
                    else{
                        return Shapes.or(topShape, topShapeIonicZ, middleShape);
                    }
                case CORINTHIAN:
                    return Shapes.or(topShape, topShapeCorinthian, middleShape);
            }
        } else if (down) {
            return Shapes.or(bottomShape, middleShape);
        }
        return middleShape;
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (Helpers.isItem(player.getItemInHand(hand), TFCTags.Items.TOOLS_HAMMER))
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

            ColumnStyles currentStyle = state.getValue(STYLE);
            ColumnStyles newStyle = ColumnStyles.values()[(currentStyle.ordinal() + 1) % ColumnStyles.values().length];

            updateColumn(level, pos, newStyle);
            return ItemInteractionResult.SUCCESS;
        }
        else
        {
            return ItemInteractionResult.FAIL;
        }
    }

    private void updateColumn(Level level, BlockPos pos, ColumnStyles newStyle) {
        for (int y = pos.getY(); y < level.getMaxBuildHeight(); y++) {
            BlockPos currentPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState currentState = level.getBlockState(currentPos);
            if (currentState.getBlock() instanceof ColumnBlock) {
                level.setBlock(currentPos, currentState.setValue(STYLE, newStyle), 3);
            } else {
                break;
            }
        }

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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("dfc.tooltip.column").withStyle(ChatFormatting.WHITE));
    }
}
