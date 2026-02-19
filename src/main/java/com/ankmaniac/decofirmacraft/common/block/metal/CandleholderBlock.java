package com.ankmaniac.decofirmacraft.common.block.metal;

import com.ankmaniac.decofirmacraft.common.block.DFCShelfBlock;
import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties;
import com.ankmaniac.decofirmacraft.common.blockentities.CandleholderBlockEntity;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

public class CandleholderBlock extends DeviceBlock {
    public static final IntegerProperty CANDLES = IntegerProperty.create("candles", 0, 5);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty ON_WALL = DFCBlockStateProperties.ON_WALL;
    public static final EnumProperty<Direction> DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    public static final ToIntFunction<BlockState> LIGHTING_SCALE = (state) -> state.getValue(LIT) ? 3 * state.getValue(CANDLES) + 3 : 0;

    public CandleholderBlock(ExtendedProperties properties){
        super(properties, InventoryRemoveBehavior.DROP);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(CANDLES, 0)
                .setValue(LIT, false)
                .setValue(ON_WALL, false)
                .setValue(DIRECTION, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CANDLES, LIT, ON_WALL, DIRECTION);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        final BlockState state = super.getStateForPlacement(ctx);
        if (state != null)
        {
            Direction direction = ctx.getHorizontalDirection();
            boolean onWall = false;

            if (!ctx.getClickedFace().getAxis().equals(Direction.Axis.Y)){
                onWall = true;
                direction = ctx.getClickedFace().getOpposite();
            }

            return this.defaultBlockState()
                    .setValue(CANDLES, 0)
                    .setValue(LIT, false)
                    .setValue(ON_WALL, onWall)
                    .setValue(DIRECTION, direction);
        }
        return null;
    }

    public static void onRandomTick(BlockState state, ServerLevel level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof CandleholderBlockEntity candle)
        {
            final int candleTicks = TFCConfig.SERVER.candleTicks.get();
            if (candle.getTicksSinceUpdate() > candleTicks && candleTicks > 0)
            {
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        final CandleholderBlockEntity candleHolder = level.getBlockEntity(pos, DFCBlockEntities.CANDLEHOLDER.get()).orElse(null);

        if (candleHolder != null) {
            final IItemHandlerModifiable inventory = candleHolder.getInventory();
            final ItemStack candles = inventory.getStackInSlot(0);
            // (Taken from TFC Candles)
            if (Helpers.isItem(player.getMainHandItem(), TFCItems.FIRESTARTER.get())) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (stack.isEmpty() && player.getAbilities().mayBuild) {
                if (state.getValue(LIT)) {
                    extinguish(player, state, level, pos);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                } else if (state.getValue(CANDLES) > 0 && !candles.isEmpty()){
                    int removeBy = 1;
                    if (player.isShiftKeyDown()){
                        removeBy = state.getValue(CANDLES);
                    }
                    level.setBlockAndUpdate(pos, state.setValue(CANDLES, (state.getValue(CANDLES) - removeBy)));
                    ItemHandlerHelper.giveItemToPlayer(player, inventory.extractItem(0, removeBy, false));
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (inventory.isItemValid(1, stack) && state.getValue(CANDLES) < 4 && (stack.getItem().equals(candles.getItem()) || candles.isEmpty())){
                level.setBlockAndUpdate(pos, state.setValue(CANDLES, (state.getValue(CANDLES) + 1)));
                ItemHandlerHelper.giveItemToPlayer(player, inventory.insertItem(0, stack.split(1), false));
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(ON_WALL)){
            BlockPos facingPos = pos.relative(state.getValue(DIRECTION));
            if (level.getBlockState(facingPos).isFaceSturdy(level, facingPos, state.getValue(DIRECTION).getOpposite())){
                return true;
            }
            return false;
        }
        else if (Block.canSupportCenter(level, pos.below(), Direction.UP)){
            return true;
        }
        else return false;
//        if (Block.canSupportCenter(level, pos.below(), Direction.UP) && state.getValue(ON_WALL).equals(false)){
//            return true;
//        }
//        else {
//            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()){
//                BlockPos facingPos = pos.relative(direction);
//
//                if (level.getBlockState(facingPos).isFaceSturdy(level, facingPos, direction.getOpposite())) {
//                    return true;
//                }
//            }
//            return false;
//        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        Direction direction = state.getValue(DIRECTION);
        BlockPos supportingPos = currentPos.relative(direction);

        if(Block.canSupportCenter(level, currentPos.below(), Direction.UP) && state.getValue(ON_WALL).equals(false)){
            return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
        else if (level.getBlockState(supportingPos).isFaceSturdy(level, supportingPos, direction.getOpposite()) && state.getValue(ON_WALL).equals(true)){
            return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
        else {
            for (Direction possibleDirection : Direction.Plane.HORIZONTAL.stream().toList()){
                BlockPos possiblePos = currentPos.relative(possibleDirection);

                if (level.getBlockState(possiblePos).isFaceSturdy(level, possiblePos, possibleDirection.getOpposite())) {
                    level.setBlock(currentPos, state.setValue(DIRECTION, possibleDirection).setValue(ON_WALL, true), 3);
                    return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
                }
            }
            if (Block.canSupportCenter(level, currentPos.below(), Direction.UP)){
                level.setBlock(currentPos, state.setValue(ON_WALL, false), 3);
                return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
            }
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return Shapes.or(CANDLEHOLDER_SHAPES.get(state.getValue(ON_WALL)).get(state.getValue(DIRECTION)).get(state.getValue(CANDLES)));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
    {
        onRandomTick(state, level, pos);
    }

    public static boolean isLit(BlockState state) {
        return state.hasProperty(LIT) && state.getBlock() instanceof CandleholderBlock && state.getValue(LIT);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire() && this.canBeLit(state)) {
            setLit(level, state, hit.getBlockPos(), true);
        }

    }

    protected boolean canBeLit(BlockState state) {
        return state.getValue(CANDLES) > 0 && !state.getValue(LIT);
    }

    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return PARTICLE_OFFSETS.get(state.getValue(ON_WALL)).get(state.getValue(DIRECTION)).get(state.getValue(CANDLES));
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            this.getParticleOffsets(state).forEach((offset) -> {
                addParticlesAndSound(level, offset.add((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), random);
            });
        }
    }

    private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.3F) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
            if (f < 0.17F) {
                level.playLocalSound(offset.x + 0.5, offset.y + 0.5, offset.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
    }

    public static void extinguish(@Nullable Player player, BlockState state, LevelAccessor level, BlockPos pos) {
        setLit(level, state, pos, false);
        if (state.getBlock() instanceof CandleholderBlock) {
            ((CandleholderBlock) state.getBlock()).getParticleOffsets(state).forEach((offset) -> {
                level.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + offset.x(), (double)pos.getY() + offset.y(), (double)pos.getZ() + offset.z(), 0.0, 0.10000000149011612, 0.0);
            });
        }

        level.playSound((Player)null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    private static void setLit(LevelAccessor level, BlockState state, BlockPos pos, boolean lit) {
        level.setBlock(pos, (BlockState)state.setValue(LIT, lit), 11);
    }

    public static final Map<Boolean, Map<Direction, Map<Integer, ImmutableList<Vec3>>>> PARTICLE_OFFSETS = Map.of(
            Boolean.TRUE, Map.of(
                    Direction.NORTH, Map.of(
                            1, ImmutableList.of(new Vec3(8d/16d, 14d/16d, 4d/16d)),
                            2, ImmutableList.of(new Vec3(5d/16d,  15d/16d, 4d/16d), new Vec3(11d/16d,  15d/16d, 4d/16d)),
                            3, ImmutableList.of(new Vec3(3d/16d,  15d/16d, 4d/16d), new Vec3(8d/16d,  15d/16d, 4d/16d), new Vec3(13d/16d,  15d/16d, 4d/16d)),
                            4, ImmutableList.of(new Vec3(3d/16d,  15d/16d, 6d/16d), new Vec3(8d/16d,  15d/16d, 3d/16d), new Vec3(8d/16d,  15d/16d, 9d/16d), new Vec3(13d/16d,  15d/16d, 6d/16d))
                    ),
                    Direction.EAST, Map.of(
                            1, ImmutableList.of(new Vec3(12d/16d, 14d/16d, 8d/16d)),
                            2, ImmutableList.of(new Vec3(12d/16d,  15d/16d, 5d/16d), new Vec3(12d/16d,  15d/16d, 11d/16d)),
                            3, ImmutableList.of(new Vec3(12d/16d,  15d/16d, 3d/16d), new Vec3(12d/16d,  15d/16d, 8d/16d), new Vec3(12d/16d,  15d/16d, 13d/16d)),
                            4, ImmutableList.of(new Vec3(10d/16d,  15d/16d, 3d/16d), new Vec3(13d/16d,  15d/16d, 8d/16d), new Vec3(7d/16d,  15d/16d, 8d/16d), new Vec3(10d/16d,  15d/16d, 13d/16d))
                    ),
                    Direction.SOUTH, Map.of(
                            1, ImmutableList.of(new Vec3(8d/16d, 14d/16d, 12d/16d)),
                            2, ImmutableList.of(new Vec3(5d/16d,  15d/16d, 12d/16d), new Vec3(11d/16d,  15d/16d, 12d/16d)),
                            3, ImmutableList.of(new Vec3(3d/16d,  15d/16d, 12d/16d), new Vec3(8d/16d,  15d/16d, 12d/16d), new Vec3(13d/16d,  15d/16d, 12d/16d)),
                            4, ImmutableList.of(new Vec3(3d/16d,  15d/16d, 10d/16d), new Vec3(8d/16d,  15d/16d, 13d/16d), new Vec3(8d/16d,  15d/16d, 7d/16d), new Vec3(13d/16d,  15d/16d, 10d/16d))
                    ),
                    Direction.WEST, Map.of(
                            1, ImmutableList.of(new Vec3(4d/16d, 14d/16d, 8d/16d)),
                            2, ImmutableList.of(new Vec3(4d/16d,  15d/16d, 5d/16d), new Vec3(4d/16d,  15d/16d, 11d/16d)),
                            3, ImmutableList.of(new Vec3(4d/16d,  15d/16d, 3d/16d), new Vec3(4d/16d,  15d/16d, 8d/16d), new Vec3(4d/16d,  15d/16d, 13d/16d)),
                            4, ImmutableList.of(new Vec3(6d/16d,  15d/16d, 3d/16d), new Vec3(3d/16d,  15d/16d, 8d/16d), new Vec3(9d/16d,  15d/16d, 8d/16d), new Vec3(6d/16d,  15d/16d, 13d/16d))
                    )
            ),
            Boolean.FALSE, Map.of(
                    Direction.NORTH, Map.of(
                            1, ImmutableList.of(new Vec3(8d/16d, 11d/16d, 8d/16d)),
                            2, ImmutableList.of(new Vec3(5d/16d, 14d/16d, 8d/16d), new Vec3(11d/16d, 14d/16d, 8d/16d)),
                            3, ImmutableList.of(new Vec3(3d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 8d/16d), new Vec3(13d/16d, 14d/16d, 8d/16d)),
                            4, ImmutableList.of(new Vec3(4d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 4d/16d), new Vec3(8d/16d, 14d/16d, 12d/16d), new Vec3(12d/16d, 14d/16d, 8d/16d))
                    ),
                    Direction.EAST, Map.of(
                            1, ImmutableList.of(new Vec3(8d/16d, 11d/16d, 8d/16d)),
                            2, ImmutableList.of(new Vec3(8d/16d, 14d/16d, 5d/16d), new Vec3(8d/16d, 14d/16d, 11d/16d)),
                            3, ImmutableList.of(new Vec3(8d/16d, 14d/16d, 3d/16d), new Vec3(8d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 13d/16d)),
                            4, ImmutableList.of(new Vec3(4d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 4d/16d), new Vec3(8d/16d, 14d/16d, 12d/16d), new Vec3(12d/16d, 14d/16d, 8d/16d))
                    ),
                    Direction.SOUTH, Map.of(
                            1, ImmutableList.of(new Vec3(8d/16d, 11d/16d, 8d/16d)),
                            2, ImmutableList.of(new Vec3(5d/16d, 14d/16d, 8d/16d), new Vec3(11d/16d, 14d/16d, 8d/16d)),
                            3, ImmutableList.of(new Vec3(3d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 8d/16d), new Vec3(13d/16d, 14d/16d, 8d/16d)),
                            4, ImmutableList.of(new Vec3(4d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 4d/16d), new Vec3(8d/16d, 14d/16d, 12d/16d), new Vec3(12d/16d, 14d/16d, 8d/16d))
                    ),
                    Direction.WEST, Map.of(
                            1, ImmutableList.of(new Vec3(8d/16d, 11d/16d, 8d/16d)),
                            2, ImmutableList.of(new Vec3(8d/16d, 14d/16d, 5d/16d), new Vec3(8d/16d, 14d/16d, 11d/16d)),
                            3, ImmutableList.of(new Vec3(8d/16d, 14d/16d, 3d/16d), new Vec3(8d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 13d/16d)),
                            4, ImmutableList.of(new Vec3(4d/16d, 14d/16d, 8d/16d), new Vec3(8d/16d, 14d/16d, 4d/16d), new Vec3(8d/16d, 14d/16d, 12d/16d), new Vec3(12d/16d, 14d/16d, 8d/16d))
                    )
            )
    );

    private static final Map<Boolean, Map<Direction, Map<Integer, VoxelShape>>> CANDLEHOLDER_SHAPES = Map.of(
            Boolean.TRUE, Map.of(
                    Direction.NORTH, Map.of(
                            0, Shapes.or(box(6.0d, 2.0d, 0.0d, 10.0d, 7.0d, 6.0d)),
                            1, Shapes.or(box(6.0d, 2.0d, 0.0d, 10.0d, 12.0d, 6.0d)),
                            2, Shapes.or(box(3.0d, 2.0d, 0.0d, 13.0d, 13.0d, 6.0d)),
                            3, Shapes.or(box(1.0d, 2.0d, 0.0d, 15.0d, 13.0d, 6.0d)),
                            4, Shapes.or(box(1.0d, 0.0d, 0.0d, 15.0d, 13.0d, 11.0d))
                    ),
                    Direction.EAST, Map.of(
                            0, Shapes.or(box(10.0d, 2.0d, 6.0d, 16.0d, 7.0d, 10.0d)),
                            1, Shapes.or(box(10.0d, 2.0d, 6.0d, 16.0d, 12.0d, 10.0d)),
                            2, Shapes.or(box(10.0d, 2.0d, 3.0d, 16.0d, 13.0d, 13.0d)),
                            3, Shapes.or(box(10.0d, 2.0d, 1.0d, 16.0d, 13.0d, 15.0d)),
                            4, Shapes.or(box(5.0d, 0.0d, 1.0d, 16.0d, 13.0d, 15.0d))
                    ),
                    Direction.SOUTH, Map.of(
                            0, Shapes.or(box(6.0d, 2.0d, 10.0d, 10.0d, 7.0d, 16.0d)),
                            1, Shapes.or(box(6.0d, 2.0d, 10.0d, 10.0d, 12.0d, 16.0d)),
                            2, Shapes.or(box(3.0d, 2.0d, 10.0d, 13.0d, 13.0d, 16.0d)),
                            3, Shapes.or(box(1.0d, 2.0d, 10.0d, 15.0d, 13.0d, 16.0d)),
                            4, Shapes.or(box(1.0d, 0.0d, 5.0d, 15.0d, 13.0d, 16.0d))
                    ),
                    Direction.WEST, Map.of(
                            0, Shapes.or(box(0.0d, 2.0d, 6.0d, 6.0d, 7.0d, 10.0d)),
                            1, Shapes.or(box(0.0d, 2.0d, 6.0d, 6.0d, 12.0d, 10.0d)),
                            2, Shapes.or(box(0.0d, 2.0d, 3.0d, 6.0d, 13.0d, 13.0d)),
                            3, Shapes.or(box(0.0d, 2.0d, 1.0d, 6.0d, 13.0d, 15.0d)),
                            4, Shapes.or(box(0.0d, 0.0d, 1.0d, 11.0d, 13.0d, 15.0d))
                    )
            ),
            Boolean.FALSE, Map.of(
                    Direction.NORTH, Map.of(
                            0, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 4.0d, 10.0d)),
                            1, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 9.0d, 10.0d)),
                            2, Shapes.or(box(3.0d, 0.0d, 6.0d, 13.0d, 12.0d, 10.0d)),
                            3, Shapes.or(box(1.0d, 0.0d, 6.0d, 15.0d, 12.0d, 10.0d)),
                            4, Shapes.or(box(2.0d, 0.0d, 2.0d, 14.0d, 12.0d, 14.0d))
                    ),
                    Direction.EAST, Map.of(
                            0, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 4.0d, 10.0d)),
                            1, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 9.0d, 10.0d)),
                            2, Shapes.or(box(6.0d, 0.0d, 3.0d, 10.0d, 12.0d, 13.0d)),
                            3, Shapes.or(box(6.0d, 0.0d, 1.0d, 10.0d, 12.0d, 15.0d)),
                            4, Shapes.or(box(2.0d, 0.0d, 2.0d, 14.0d, 12.0d, 14.0d))
                    ),
                    Direction.SOUTH, Map.of(
                            0, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 4.0d, 10.0d)),
                            1, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 9.0d, 10.0d)),
                            2, Shapes.or(box(3.0d, 0.0d, 6.0d, 13.0d, 12.0d, 10.0d)),
                            3, Shapes.or(box(1.0d, 0.0d, 6.0d, 15.0d, 12.0d, 10.0d)),
                            4, Shapes.or(box(2.0d, 0.0d, 2.0d, 14.0d, 12.0d, 14.0d))
                    ),
                    Direction.WEST, Map.of(
                            0, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 4.0d, 10.0d)),
                            1, Shapes.or(box(6.0d, 0.0d, 6.0d, 10.0d, 9.0d, 10.0d)),
                            2, Shapes.or(box(6.0d, 0.0d, 3.0d, 10.0d, 12.0d, 13.0d)),
                            3, Shapes.or(box(6.0d, 0.0d, 1.0d, 10.0d, 12.0d, 15.0d)),
                            4, Shapes.or(box(2.0d, 0.0d, 2.0d, 14.0d, 12.0d, 14.0d))
                    )
            )
    );
}
