package com.ankmaniac.decofirmacraft.common.block;

import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.IHighlightHandler;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Collectors;

import static com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity.*;

public class DFCShelfBlock extends DeviceBlock implements IHighlightHandler {

    public static int getSlotForSelection(BlockHitResult result, Direction direction)
    {
        final Vec3 location = result.getLocation();
        final BlockPos pos = result.getBlockPos();

        for (Map.Entry<Integer, AABB> entry : SLOT_BOUNDS.get(direction).entrySet())
        {
            if (entry.getValue().move(pos).contains(location))
            {
                return entry.getKey();
            }
        }
        return -1;
    }

    private static final Map<Direction, Map<Integer, VoxelShape>> SLOT_RENDER_SHAPES = Map.of(
            Direction.NORTH, Map.of(
                    SLOT_END, Shapes.box(2.5f/16f, 1f/16f, 3f/16f, 5.5f/16f, 4f/16f, 6f/16f),
                    SLOT_START + 2, Shapes.box(10.5f/16f, 1f/16f, 3f/16f, 13.5f/16f, 4f/16f, 6f/16f), 
                    SLOT_START + 1, Shapes.box(2.5f/16f, 9f/16f, 3f/16f, 5.5f/16f, 12f/16f, 6f/16f), 
                    SLOT_START, Shapes.box(10.5f/16f, 9f/16f, 3f/16f, 13.5f/16f, 12f/16f, 6f/16f)
            ),
            Direction.EAST, Map.of(
                    SLOT_END, Shapes.box(10f/16f, 1f/16f, 2.5f/16f, 13f/16f, 4f/16f, 5.5f/16f),
                    SLOT_START + 2, Shapes.box(10f/16f, 1f/16f, 10.5f/16f, 13f/16f, 4f/16f, 13.5f/16f),
                    SLOT_START + 1, Shapes.box(10f/16f, 9f/16f, 2.5f/16f, 13f/16f, 12f/16f, 5.5f/16f),
                    SLOT_START, Shapes.box(10f/16f, 9f/16f, 10.5f/16f, 13f/16f, 12f/16f, 13.5f/16f)
            ),
            Direction.SOUTH, Map.of(
                    SLOT_END, Shapes.box(2.5f/16f, 1f/16f, 10f/16f, 5.5f/16f, 4f/16f, 13f/16f),
                    SLOT_START + 2, Shapes.box(10.5f/16f, 1f/16f, 10f/16f, 13.5f/16f, 4f/16f, 13f/16f),
                    SLOT_START + 1, Shapes.box(2.5f/16f, 9f/16f, 10f/16f, 5.5f/16f, 12f/16f, 13f/16f),
                    SLOT_START, Shapes.box(10.5f/16f, 9f/16f, 10f/16f, 13.5f/16f, 12f/16f, 13f/16f)
            ),
            Direction.WEST, Map.of(
                    SLOT_END, Shapes.box(3f/16f, 1f/16f, 2.5f/16f, 6f/16f, 4f/16f, 5.5f/16f),
                    SLOT_START + 2, Shapes.box(3f/16f, 1f/16f, 10.5f/16f, 6f/16f, 4f/16f, 13.5f/16f),
                    SLOT_START + 1, Shapes.box(3f/16f, 9f/16f, 2.5f/16f, 6f/16f, 12f/16f, 5.5f/16f),
                    SLOT_START, Shapes.box(3f/16f, 9f/16f, 10.5f/16f, 6f/16f, 12f/16f, 13.5f/16f)
            )
    );

    private static final Map<Direction, Map<Integer, AABB>> SLOT_BOUNDS = SLOT_RENDER_SHAPES.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey, directionEntry -> directionEntry.getValue().entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey, entry  -> entry.getValue().bounds().inflate(0.01f)))));
    public static final Map<Direction, Map<Integer, Vec3>> SLOT_CENTERS = SLOT_BOUNDS.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey, directionEntry -> directionEntry.getValue().entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey, entry -> entry.getValue().getCenter()))));

    private static final VoxelShape SHELF_BASE_NORTH =
            Shapes.or(
                    box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 1.0d),
                    box(0.0d, 0.0d, 0.0d, 16.0d, 1.0d, 8.0d),
                    box(0.0d, 7.0d, 0.0d, 16.0d, 9.0d, 8.0d),
                    box(0.0d, 15.0d, 0.0d, 16.0d, 16.0d, 8.0d),
                    box(2.5d, 1.0d, 3.0d, 5.5d, 4.0d, 6.0d),
                    box(10.5d, 1.0d, 3.0d, 13.5d, 4.0d, 6.0d),
                    box(2.5d, 9.0d, 3.0d, 5.5d, 12.0d, 6.0d),
                    box(10.5d, 9.0d, 3.0d, 13.5d, 12.0d, 6.0d));
    private static final VoxelShape SHELF_BASE_EAST =
            Shapes.or(
                    box(15.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d),
                    box(8.0d, 0.0d, 0.0d, 16.0d, 1.0d, 16.0d),
                    box(8.0d, 7.0d, 0.0d, 16.0d, 9.0d, 16.0d),
                    box(8.0d, 15.0d, 0.0d, 16.0d, 16.0d, 16.0d),
                    box(10.0d, 1.0d, 2.5d, 13.0d, 4.0d, 5.5d),
                    box(10.0d, 1.0d, 10.5d, 13.0d, 4.0d, 13.5d),
                    box(10.0d, 9.0d, 2.5d, 13.0d, 12.0d, 5.5d),
                    box(10.0d, 9.0d, 10.5d, 13.0d, 12.0d, 13.5d));
    private static final VoxelShape SHELF_BASE_SOUTH =
            Shapes.or(
                    box(0.0d, 0.0d, 15.0d, 16.0d, 16.0d, 16.0d),
                    box(0.0d, 0.0d, 8.0d, 16.0d, 1.0d, 16.0d),
                    box(0.0d, 7.0d, 8.0d, 16.0d, 9.0d, 16.0d),
                    box(0.0d, 15.0d, 8.0d, 16.0d, 16.0d, 16.0d),
                    box(2.5d, 1.0d, 10.0d, 5.5d, 4.0d, 13.0d),
                    box(10.5d, 1.0d, 10.0d, 13.5d, 4.0d, 13.0d),
                    box(2.5d, 9.0d, 10.0d, 5.5d, 12.0d, 13.0d),
                    box(10.5d, 9.0d, 10.0d, 13.5d, 12.0d, 13.0d));
    private static final VoxelShape SHELF_BASE_WEST =
            Shapes.or(
                    box(0.0d, 0.0d, 0.0d, 1.0d, 16.0d, 16.0d),
                    box(0.0d, 0.0d, 0.0d, 8.0d, 1.0d, 16.0d),
                    box(0.0d, 7.0d, 0.0d, 8.0d, 9.0d, 16.0d),
                    box(0.0d, 15.0d, 0.0d, 8.0d, 16.0d, 16.0d),
                    box(3.0d, 1.0d, 2.5d, 6.0d, 4.0d, 5.5d),
                    box(3.0d, 1.0d, 10.5d, 6.0d, 4.0d, 13.5d),
                    box(3.0d, 9.0d, 2.5d, 6.0d, 12.0d, 5.5d),
                    box(3.0d, 9.0d, 10.5d, 6.0d, 12.0d, 13.5d));
    private static final VoxelShape SHELF_SIDE_NORTH_RIGHT =
            Shapes.box(15f/16f, 0.0, 0.0, 1.0, 1.0, 8f/16f);
    private static final VoxelShape SHELF_SIDE_NORTH_LEFT =
            Shapes.box(0.0, 0.0, 0.0, 1f/16f, 1.0, 8f/16f);
    private static final VoxelShape SHELF_SIDE_EAST_RIGHT =
            Shapes.box(8f/16f, 0.0, 15f/16f, 1.0, 1.0, 1.0);
    private static final VoxelShape SHELF_SIDE_EAST_LEFT =
            Shapes.box(8f/16f, 0.0, 0.0, 1.0, 1.0, 1f/16f);
    private static final VoxelShape SHELF_SIDE_SOUTH_RIGHT =
            Shapes.box(0.0, 0.0, 8f/16f, 1f/16f, 1.0, 1.0);
    private static final VoxelShape SHELF_SIDE_SOUTH_LEFT =
            Shapes.box(15f/16f, 0.0, 8f/16f, 1.0, 1.0, 1.0);
    private static final VoxelShape SHELF_SIDE_WEST_RIGHT =
            Shapes.box(0.0, 0.0, 0.0, 8f/16f, 1.0, 1f/16f);
    private static final VoxelShape SHELF_SIDE_WEST_LEFT =
            Shapes.box(0.0, 0.0, 15f/16f, 8f/16f, 1.0, 1.0);

    public static final EnumProperty<Direction> DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LEFT = DFCBlockStateProperties.LEFT;
    public static final BooleanProperty RIGHT = DFCBlockStateProperties.RIGHT;

    public DFCShelfBlock(ExtendedProperties properties){
        super(properties, InventoryRemoveBehavior.DROP);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(DIRECTION, Direction.NORTH)
                        .setValue(LEFT, false)
                        .setValue(RIGHT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(DIRECTION, LEFT, RIGHT));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        final BlockState state = super.getStateForPlacement(ctx);
        if (state != null)
        {
            final Direction direction = ctx.getHorizontalDirection();
            final Level level = ctx.getLevel();
            final BlockPos pos = ctx.getClickedPos();
            final BlockState rightBlock = level.getBlockState(pos.relative(direction.getClockWise()));
            final BlockState leftBlock = level.getBlockState(pos.relative(direction.getCounterClockWise()));
            boolean right = false;
            boolean left = false;

            if (rightBlock.getBlock() instanceof DFCShelfBlock && rightBlock.getValue(DIRECTION) == direction) {
                right = true;
            }
            if (leftBlock.getBlock() instanceof DFCShelfBlock && leftBlock.getValue(DIRECTION) == direction) {
                left = true;
            }

            return this.defaultBlockState()
                    .setValue(DIRECTION, direction)
                    .setValue(RIGHT, right)
                    .setValue(LEFT, left);
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        Direction direction = state.getValue(DIRECTION);
        Boolean right = state.getValue(RIGHT);
        Boolean left = state.getValue(LEFT);
        switch(direction){
            case NORTH:
                default:
                    if (right && left){
                        return Shapes.or(SHELF_BASE_NORTH);
                    }
                    else if (right){
                        return Shapes.or(SHELF_BASE_NORTH, SHELF_SIDE_NORTH_LEFT);
                    }
                    else if (left){
                        return Shapes.or(SHELF_BASE_NORTH, SHELF_SIDE_NORTH_RIGHT);
                    }
                    return Shapes.or(SHELF_BASE_NORTH, SHELF_SIDE_NORTH_LEFT, SHELF_SIDE_NORTH_RIGHT);
            case EAST:
                if (right && left){
                    return Shapes.or(SHELF_BASE_EAST);
                }
                else if (right){
                    return Shapes.or(SHELF_BASE_EAST, SHELF_SIDE_EAST_LEFT);
                }
                else if (left){
                    return Shapes.or(SHELF_BASE_EAST, SHELF_SIDE_EAST_RIGHT);
                }
                return Shapes.or(SHELF_BASE_EAST, SHELF_SIDE_EAST_LEFT, SHELF_SIDE_EAST_RIGHT);
            case SOUTH:
                if (right && left){
                    return Shapes.or(SHELF_BASE_SOUTH);
                }
                else if (right){
                    return Shapes.or(SHELF_BASE_SOUTH, SHELF_SIDE_SOUTH_LEFT);
                }
                else if (left){
                    return Shapes.or(SHELF_BASE_SOUTH, SHELF_SIDE_SOUTH_RIGHT);
                }
                return Shapes.or(SHELF_BASE_SOUTH, SHELF_SIDE_SOUTH_LEFT, SHELF_SIDE_SOUTH_RIGHT);
            case WEST:
                if (right && left){
                    return Shapes.or(SHELF_BASE_WEST);
                }
                else if (right){
                    return Shapes.or(SHELF_BASE_WEST, SHELF_SIDE_WEST_LEFT);
                }
                else if (left){
                    return Shapes.or(SHELF_BASE_WEST, SHELF_SIDE_WEST_RIGHT);
                }
                return Shapes.or(SHELF_BASE_WEST, SHELF_SIDE_WEST_LEFT, SHELF_SIDE_WEST_RIGHT);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        return shelfInteraction(stack, state, level, pos, player, hitResult);
    }

    /* Fruitless attempt at getting it to work with items while shifting rightclicking */
//    @Override
//    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit){
//        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
//        System.out.println("DEBUG SHELF 002 ");
//        return shelfInteraction(stack, state, level, pos, player, hit).result();
//    }

    private ItemInteractionResult shelfInteraction(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult){

        final DFCShelfBlockEntity shelf = level.getBlockEntity(pos, DFCBlockEntities.DFC_SHELVES.get()).orElse(null);

        if (shelf != null) {
            final IItemHandlerModifiable inventory = shelf.getInventory();
            final int slot = getSlotForSelection(hitResult, state.getValue(DIRECTION));
            final ItemStack current = slot == -1 ? ItemStack.EMPTY : inventory.getStackInSlot(slot);

            if (slot != -1) {
                if (!stack.isEmpty() && inventory.isItemValid(slot, stack)) {
                    int maxTransfer = Math.min(inventory.getSlotLimit(slot), stack.getCount()) - current.getCount();
                    if (current.isEmpty()) {
                        if (player.isShiftKeyDown()) {
                            ItemHandlerHelper.giveItemToPlayer(player, inventory.insertItem(slot, stack.split(maxTransfer), false));
                        } else {
                            ItemHandlerHelper.giveItemToPlayer(player, inventory.insertItem(slot, stack.split(1), false));
                        }
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    } else if (current.getItem().equals(stack.getItem()) && Math.min(ItemSizeManager.get(current).getWeight(current).stackSize, inventory.getSlotLimit(slot)) > current.getCount() ) {
                        //this cant happen because rightclicking with an item doesn't do useItemOn
                        if (player.isShiftKeyDown()) {
                            ItemHandlerHelper.giveItemToPlayer(player, inventory.insertItem(slot, stack.split(maxTransfer), false));
                        } else {
                            ItemHandlerHelper.giveItemToPlayer(player, inventory.insertItem(slot, stack.split(1), false));
                        }
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
                if (!current.isEmpty() && !stack.getItem().equals(current.getItem())) {
                    int removeBy;
                    //this cant actually happen because if you're shift rightclicking with an item it cant call useItemOn
                    if (player.isShiftKeyDown()) {
                        removeBy = current.getCount();
                    } else {
                        removeBy = 1;
                    }

                    if (stack.isEmpty()) {
                        ItemHandlerHelper.giveItemToPlayer(player, inventory.extractItem(slot, removeBy, false), player.getInventory().selected);
                    } else {
                        ItemHandlerHelper.giveItemToPlayer(player, inventory.extractItem(slot, removeBy, false));
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
    {
        final Direction direction = state.getValue(DIRECTION);

        if (neighborPos.equals(pos.relative(direction.getClockWise()))) {
            state = state.setValue(RIGHT, connectsTo(direction, neighborState));
        }
        else if (neighborPos.equals(pos.relative(direction.getCounterClockWise()))) {
            state = state.setValue(LEFT, connectsTo(direction, neighborState));
        }

        return state;
    }

    private boolean connectsTo(Direction direction, BlockState neighborState){
        return neighborState.getBlock() instanceof DFCShelfBlock && neighborState.getValue(DIRECTION).equals(direction);
    }

    @Override
    public boolean drawHighlight(Level level, BlockPos pos, Player player, BlockHitResult rayTrace, PoseStack stack, MultiBufferSource buffers, Vec3 rendererPosition)
    {
        final int slot = getSlotForSelection(rayTrace, level.getBlockState(pos).getValue(DIRECTION));
        if (slot != -1)
        {
            IHighlightHandler.drawBox(stack, SLOT_RENDER_SHAPES.get(level.getBlockState(pos).getValue(DIRECTION)).get(slot), buffers, pos, rendererPosition, 1f, 0f, 0f, 1f);
            return true;
        }
        return false;
    }
}
