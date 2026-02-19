package com.ankmaniac.decofirmacraft.common.block;

import com.ankmaniac.decofirmacraft.common.DecoFirmaCraftDataMaps;
import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties;
import com.ankmaniac.decofirmacraft.common.blockentities.ChiseledBlockEntity;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.ankmaniac.decofirmacraft.util.DFCTags;
import com.ankmaniac.decofirmacraft.util.DynamicTextureDataMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChiseledBlock extends ExtendedBlock implements EntityBlockExtension, DynamicBlock {
    public static final BooleanProperty TOP_NORTHEAST = DFCBlockStateProperties.TOP_NORTHEAST;
    public static final BooleanProperty TOP_NORTHWEST = DFCBlockStateProperties.TOP_NORTHWEST;
    public static final BooleanProperty TOP_SOUTHEAST = DFCBlockStateProperties.TOP_SOUTHEAST;
    public static final BooleanProperty TOP_SOUTHWEST = DFCBlockStateProperties.TOP_SOUTHWEST;
    public static final BooleanProperty BOTTOM_NORTHEAST = DFCBlockStateProperties.BOTTOM_NORTHEAST;
    public static final BooleanProperty BOTTOM_NORTHWEST = DFCBlockStateProperties.BOTTOM_NORTHWEST;
    public static final BooleanProperty BOTTOM_SOUTHEAST = DFCBlockStateProperties.BOTTOM_SOUTHEAST;
    public static final BooleanProperty BOTTOM_SOUTHWEST = DFCBlockStateProperties.BOTTOM_SOUTHWEST;
    public static final List<BooleanProperty> properties = List.of(TOP_NORTHEAST, TOP_NORTHWEST, TOP_SOUTHEAST, TOP_SOUTHWEST, BOTTOM_NORTHEAST, BOTTOM_NORTHWEST, BOTTOM_SOUTHEAST, BOTTOM_SOUTHWEST);

    public ChiseledBlock(ExtendedProperties properties)
    {
        super(properties);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(TOP_NORTHEAST, true)
                        .setValue(TOP_NORTHWEST, true)
                        .setValue(TOP_SOUTHEAST, true)
                        .setValue(TOP_SOUTHWEST, true)
                        .setValue(BOTTOM_NORTHEAST, true)
                        .setValue(BOTTOM_NORTHWEST, true)
                        .setValue(BOTTOM_SOUTHEAST, true)
                        .setValue(BOTTOM_SOUTHWEST, true)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ChiseledBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(TOP_NORTHEAST, TOP_NORTHWEST, TOP_SOUTHEAST, TOP_SOUTHWEST, BOTTOM_NORTHEAST, BOTTOM_NORTHWEST, BOTTOM_SOUTHEAST, BOTTOM_SOUTHWEST));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return this.defaultBlockState();
    }


    private static final VoxelShape TOP_NORTHEAST_SHAPE =
            Shapes.box(0.5, 0.5, 0.0, 1.0, 1.0, 0.5);
    private static final VoxelShape TOP_NORTHWEST_SHAPE =
            Shapes.box(0.0, 0.5, 0.0, 0.5, 1.0, 0.5);
    private static final VoxelShape TOP_SOUTHEAST_SHAPE =
            Shapes.box(0.5, 0.5, 0.5, 1.0, 1.0, 1.0);
    private static final VoxelShape TOP_SOUTHWEST_SHAPE =
            Shapes.box(0.0, 0.5, 0.5, 0.5, 1.0, 1.0);
    private static final VoxelShape BOTTOM_NORTHEAST_SHAPE =
            Shapes.box(0.5, 0.0, 0.0, 1.0, 0.5, 0.5);
    private static final VoxelShape BOTTOM_NORTHWEST_SHAPE =
            Shapes.box(0.0, 0.0, 0.0, 0.5, 0.5, 0.5);
    private static final VoxelShape BOTTOM_SOUTHEAST_SHAPE =
            Shapes.box(0.5, 0.0, 0.5, 1.0, 0.5, 1.0);
    private static final VoxelShape BOTTOM_SOUTHWEST_SHAPE =
            Shapes.box(0.0, 0.0, 0.5, 0.5, 0.5, 1.0);


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape voxelShape = Shapes.empty();

        if (state.getValue(TOP_NORTHEAST)) {
            voxelShape = Shapes.or(voxelShape, TOP_NORTHEAST_SHAPE);
        }
        if (state.getValue(TOP_NORTHWEST)) {
            voxelShape = Shapes.or(voxelShape, TOP_NORTHWEST_SHAPE);
        }
        if (state.getValue(TOP_SOUTHEAST)) {
            voxelShape = Shapes.or(voxelShape, TOP_SOUTHEAST_SHAPE);
        }
        if (state.getValue(TOP_SOUTHWEST)) {
            voxelShape = Shapes.or(voxelShape, TOP_SOUTHWEST_SHAPE);
        }
        if (state.getValue(BOTTOM_NORTHEAST)) {
            voxelShape = Shapes.or(voxelShape, BOTTOM_NORTHEAST_SHAPE);
        }
        if (state.getValue(BOTTOM_NORTHWEST)) {
            voxelShape = Shapes.or(voxelShape, BOTTOM_NORTHWEST_SHAPE);
        }
        if (state.getValue(BOTTOM_SOUTHEAST)) {
            voxelShape = Shapes.or(voxelShape, BOTTOM_SOUTHEAST_SHAPE);
        }
        if (state.getValue(BOTTOM_SOUTHWEST)) {
            voxelShape = Shapes.or(voxelShape, BOTTOM_SOUTHWEST_SHAPE);
        }
        return voxelShape;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        if (!state.canSurvive(level, pos))
        {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
        }
        else
        {
            boolean fullBlock = true;
            for (BooleanProperty property : properties)
            {
                if (!state.getValue(property))
                {
                    fullBlock = false;
                    break;
                }
            }
            if (fullBlock)
            {
                final ChiseledBlockEntity chiseledBlockEntity = (ChiseledBlockEntity) level.getBlockEntity(pos);
                if(chiseledBlockEntity != null)
                {
                    final int[] stateMap = chiseledBlockEntity.getStateMap();
                    final int idValue = stateMap[0];
                    boolean sameBlock = true;
                    for (int i = 0; i < 8; i++)
                    {
                        if (stateMap[i] != idValue)
                        {
                            sameBlock = false;
                            break;
                        }
                    }
                    if (sameBlock)
                    {
                        level.setBlock(pos, Block.stateById(idValue), Block.UPDATE_ALL);
                    }
                }
            }
            else
            {
                super.onPlace(state, level, pos, oldState, movedByPiston);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        boolean canSurvive = false;
        for (BooleanProperty property : properties)
        {
            if (state.getValue(property)) {
                canSurvive = true;
                break;
            }
        }
        return canSurvive;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (Helpers.isItem(stack.getItem(), DFCTags.Items.CHISELABLE) && stack.getItem() instanceof BlockItem blockItem)
        {
            double hitOffsetY = hitResult.getLocation().y - pos.getY();
            double hitOffsetX = hitResult.getLocation().x - pos.getX();
            double hitOffsetZ = hitResult.getLocation().z - pos.getZ();

            if(hitOffsetY > 0D && hitOffsetY < 1D &&
                    hitOffsetX > 0D && hitOffsetX < 1D &&
                    hitOffsetZ > 0D && hitOffsetZ < 1D)
            {
                final var registry = level.registryAccess().registryOrThrow(Registries.BLOCK);
                final DataMapType<Block, DynamicTextureDataMap> dynamicTextureDataMap = DecoFirmaCraftDataMaps.CHISELED_BLOCK;
                final var dataHolder = registry.getHolder(registry.getResourceKey(blockItem.getBlock()).orElseThrow()).orElseThrow().getData(dynamicTextureDataMap);
                final ChiseledBlockEntity chiseledBlockEntity = (ChiseledBlockEntity) level.getBlockEntity(pos);
                if (chiseledBlockEntity != null && dataHolder != null)
                {
                    final ChiseledBlockEntity.ChiseledBlockMaps chunkMap = chiseledBlockEntity.oldMap();
                    Set<BooleanProperty> replacedProperties = new HashSet<>();
                    for (BooleanProperty property : properties)
                    {
                        if (!state.getValue(property))
                        {
                            replacedProperties.add(property);
                            state = state.setValue(property, true);
                        }
                    }
                    chiseledBlockEntity.addBlock(blockItem.getBlock().getStateForPlacement(
                            new BlockPlaceContext(player, hand, stack, hitResult)),
                            dataHolder.dynamicTextureData(), replacedProperties);
                    level.setBlockAndUpdate(pos, state);
                    if (level.getBlockState(pos).getBlock() instanceof ChiseledBlock)
                    {
                        final ChiseledBlockEntity newChiseledBlockEntity = (ChiseledBlockEntity) level.getBlockEntity(pos);
                        assert newChiseledBlockEntity != null;
                        newChiseledBlockEntity.replaceChunkMap(chunkMap);
                    }
                    if (!player.hasInfiniteMaterials())
                    {
                        stack.shrink(1);
                    }
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
