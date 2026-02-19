package com.ankmaniac.decofirmacraft.common.block;

import com.ankmaniac.decofirmacraft.common.block.metal.DFCExtendedMetal;
import com.ankmaniac.decofirmacraft.common.block.rock.DFCExtendedRock.DFCRockBlockType;
import com.ankmaniac.decofirmacraft.common.block.rock.DFCExtendedRock;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.item.DFCItems;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCMagmaBlock;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockAnvilBlock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

@SuppressWarnings("unused")
public final class DFCBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);

    public static final Map<DFCExtendedRock, Map<DFCRockBlockType, Id<Block>>> DFC_ROCK_BLOCKS = Helpers.mapOf(DFCExtendedRock.class, rock ->
            Helpers.mapOf(DFCRockBlockType.class, type -> type.has(rock), type ->
                    register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
            )
    );

    public static final Map<DFCExtendedRock, Map<DFCRockBlockType, DFCDecorationBlockHolder>> DFC_ROCK_DECORATIONS = Helpers.mapOf(DFCExtendedRock.class, rock ->
            Helpers.mapOf(DFCRockBlockType.class, type -> type.has(rock) && type.hasVariants(), type -> registerDecorations(
                    "rock/" + type.name() + "/" + rock.name(),
                    () -> type.dfcCreateSlab(rock),
                    () -> type.dfcCreateStairs(rock),
                    () -> type.dfcCreateWall(rock),
                    rock.createItemProperties()
            ))
    );

    public static final Map<DFCExtendedRock, Id<Block>> DFC_ROCK_ANVILS = Helpers.mapOf(DFCExtendedRock.class, rock -> rock.isDFCRock() && (rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE), rock ->
            register("rock/anvil/" + rock.name(), () -> new RockAnvilBlock(ExtendedProperties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2, 10).requiresCorrectToolForDrops().cloneItem(DFCBlocks.DFC_ROCK_BLOCKS.get(rock).get(Rock.BlockType.RAW)).blockEntity(TFCBlockEntities.ANVIL)), b -> new BlockItem(b, rock.createItemProperties()))
    );

    public static final Map<DFCExtendedRock, Id<Block>> DFC_MAGMA_BLOCKS = Helpers.mapOf(DFCExtendedRock.class, rock -> rock.isDFCRock() && (rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE), rock ->
            register("rock/magma/" + rock.name(), () -> new TFCMagmaBlock(ExtendedProperties.of().pathType(PathType.LAVA).mapColor(MapColor.NETHER).requiresCorrectToolForDrops().lightLevel(s -> 6).randomTicks().strength(0.5F).isValidSpawn((state, level, pos, type) -> type.fireImmune()).hasPostProcess(DFCBlocks::always)), b -> new BlockItem(b, rock.createItemProperties()))
    );

    public static final Map<DFCExtendedMetal, Map<DFCExtendedMetal.DFCMetalBlockType, Id<Block>>> DFC_METAL_BLOCKS = Helpers.mapOf(DFCExtendedMetal.class, metal ->
            Helpers.mapOf(DFCExtendedMetal.DFCMetalBlockType.class, type -> type.has(metal), type ->
                    register(type.createName(metal), type.create(metal), type.createBlockItem(new Item.Properties()))
            )
    );

    public static final Map<Wood, Id<Block>> DFC_SHELF_BLOCKS = Helpers.mapOf(Wood.class, wood ->
            register("wood/shelf/" + wood.name(), () -> new DFCShelfBlock(ExtendedProperties.of(Blocks.OAK_PLANKS).blockEntity(DFCBlockEntities.DFC_SHELVES)))
    );

    public static final Id<Block> CRAFTING_CLOTH = register("crafting_cloth", () ->
            new CraftingClothBlock(ExtendedProperties
                    .of(Blocks.BROWN_CARPET)
                    .noOcclusion()
                    .noCollission()));

    //This as an item would probably crash the game or maybe not im not quite sure dont try it though ;p
    public static final Id<Block> CHISELED_BLOCK = registerNoItem("chiseled_block", () ->
            new ChiseledBlock(ExtendedProperties
                    .of(Blocks.STONE).blockEntity(DFCBlockEntities.CHISELED_BLOCK_ENTITY)));

    public static boolean always(BlockState state, BlockGetter level, BlockPos pos)
    {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos)
    {
        return false;
    }

    public static boolean neverEntity(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

    public static ToIntFunction<BlockState> alwaysLit()
    {
        return s -> 15;
    }

    public static ToIntFunction<BlockState> lavaLoggedBlockEmission()
    {
        return state -> state.getValue(((IFluidLoggable) state.getBlock()).getFluidProperty()).is(Fluids.LAVA) ? 15 : 0;
    }

    public static ToIntFunction<BlockState> litBlockEmission(int lightValue)
    {
        return state -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }


    private static <T extends Block> Id<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(DFCBlocks.BLOCKS, DFCItems.ITEMS, name, blockSupplier, blockItemFactory));
    }

    private static <T1 extends SlabBlock, T2 extends StairBlock, T3 extends WallBlock> DFCDecorationBlockHolder registerDecorations(String baseName, Supplier<T1> slab, Supplier<T2> stair, Supplier<T3> wall, Item.Properties properties)
    {
        return new DFCDecorationBlockHolder(
                register(baseName + "_slab", slab, b -> new BlockItem(b, properties)),
                register(baseName + "_stairs", stair, b -> new BlockItem(b, properties)),
                register(baseName + "_wall", wall, b -> new BlockItem(b, properties))
        );
    }

    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }
}
