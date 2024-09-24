package com.redstoneguy10ls.decofirmacraft.common.blocks;

import com.redstoneguy10ls.decofirmacraft.common.blocks.metal.DFCMetal;
import com.redstoneguy10ls.decofirmacraft.common.blocks.metal.GateBlock;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.*;
import com.redstoneguy10ls.decofirmacraft.common.items.DFCItems;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.*;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockAnvilBlock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);


    public static final Map<DFCRock, Map<Rock.BlockType, RegistryObject<Block>>> CUSTOM_ROCK_TYPES = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(Rock.BlockType.class, type ->
                    register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
            )
    );

    public static final Map<DFCRock, Map<Rock.BlockType, DecorationBlockRegistryObject>> CUSTOM_DFC_ROCK_DECORATIONS = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(Rock.BlockType.class, Rock.BlockType::hasVariants, type -> new DecorationBlockRegistryObject(
                    register(("rock/" + type.name() + "/" + rock.name()) + "_slab", () -> type.createSlab(rock)),
                    register(("rock/" + type.name() + "/" + rock.name()) + "_stairs", () -> type.createStairs(rock)),
                    register(("rock/" + type.name() + "/" + rock.name()) + "_wall", () -> type.createWall(rock))
            ))
    );

    public static final Map<DFCRock, RegistryObject<Block>> DFC_ROCK_ANVILS = Helpers.mapOfKeys(DFCRock.class, rock -> rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE, rock ->
            register("rock/anvil/" + rock.name(), () -> new RockAnvilBlock(ExtendedProperties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2, 10).requiresCorrectToolForDrops().blockEntity(TFCBlockEntities.ANVIL), DFCBlocks.CUSTOM_ROCK_TYPES.get(rock).get(Rock.BlockType.RAW)))
    );

    public static final Map<DFCRock, RegistryObject<Block>> DFC_MAGMA_BLOCKS = Helpers.mapOfKeys(DFCRock.class, rock -> rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE, rock ->
            register("rock/magma/" + rock.name(), () -> new TFCMagmaBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).requiresCorrectToolForDrops().lightLevel(s -> 6).randomTicks().strength(0.5F).isValidSpawn((state, level, pos, type) -> type.fireImmune()).hasPostProcess(TFCBlocks::always)))
    );

    //pillar,roads,tiles
    public static final Map<Rock, Map<CustomRockBlocks, RegistryObject<Block>>> CUSTOM_ROCK_BLOCKS = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(CustomRockBlocks.class, type ->
                    register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
            )
    );

    public static final Map<Rock, Map<CustomRockBlocks, DecorationBlockRegistryObject>> ROCK_DECORATIONS = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(CustomRockBlocks.class, CustomRockBlocks::hasVariants, type -> new DecorationBlockRegistryObject(
                    register(("rock/" + type.name() + "/" + rock.name()) + "_slab", () -> type.createSlab(rock)),
                    register(("rock/" + type.name() + "/" + rock.name()) + "_stairs", () -> type.createStairs(rock)),
                    register(("rock/" + type.name() + "/" + rock.name()) + "_wall", () -> type.createWall(rock))
            ))
    );

    public static final Map<DFCRock, Map<CustomDFCRockBlocks, RegistryObject<Block>>> CUSTOM_DFC_ROCK_BLOCKS = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(CustomDFCRockBlocks.class, type ->
                    register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
            )
    );

    public static final Map<DFCRock, Map<CustomDFCRockBlocks, DecorationBlockRegistryObject>> DFC_ROCK_DECORATIONS = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(CustomDFCRockBlocks.class, CustomDFCRockBlocks::hasVariants, type -> new DecorationBlockRegistryObject(
                    register(("rock/" + type.name() + "/" + rock.name()) + "_slab", () -> type.createSlab(rock)),
                    register(("rock/" + type.name() + "/" + rock.name()) + "_stairs", () -> type.createStairs(rock)),
                    register(("rock/" + type.name() + "/" + rock.name()) + "_wall", () -> type.createWall(rock))
            ))
    );

    public static final Map<DFCRock, Map<Ore, RegistryObject<Block>>> DFC_ROCK_ORES = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(Ore.class, ore -> !ore.isGraded(), ore ->
                    register(("ore/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
            )
    );

    public static final Map<DFCRock, Map<Ore, Map<Ore.Grade, RegistryObject<Block>>>> DFC_ROCK_GRADED_ORES = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(Ore.class, Ore::isGraded, ore ->
                    Helpers.mapOfKeys(Ore.Grade.class, grade ->
                            register(("ore/" + grade.name() + "_" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
                    )
            )
    );

    public static final Map<Rock, Map<DFCOre, RegistryObject<Block>>> DFC_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(DFCOre.class, ore -> !ore.isGraded(), ore ->
                    register(("ore/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
            )
    );

    public static final Map<Rock, Map<DFCOre, Map<Ore.Grade, RegistryObject<Block>>>> DFC_GRADED_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(DFCOre.class, DFCOre::isGraded, ore ->
                    Helpers.mapOfKeys(Ore.Grade.class, grade ->
                            register(("ore/" + grade.name() + "_" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
                    )
            )
    );

    public static final Map<DFCOre, RegistryObject<Block>> DFC_SMALL_ORES = Helpers.mapOfKeys(DFCOre.class, DFCOre::isGraded, type ->
            register(("ore/small_" + type.name()), () -> GroundcoverBlock.looseOre(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .strength(0.05F, 0.0F)
                    .sound(SoundType.NETHER_ORE)
                    .noCollission()
                    .pushReaction(PushReaction.DESTROY)))
    );

    public static final Map<DFCRock, Map<DFCOre, RegistryObject<Block>>> DFC_ROCK_DFC_ORES = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(DFCOre.class, ore -> !ore.isGraded(), ore ->
                    register(("ore/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
            )
    );

    public static final Map<DFCRock, Map<DFCOre, Map<Ore.Grade, RegistryObject<Block>>>> DFC_ROCK_DFC_GRADED_ORES = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(DFCOre.class, DFCOre::isGraded, ore ->
                    Helpers.mapOfKeys(Ore.Grade.class, grade ->
                            register(("ore/" + grade.name() + "_" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
                    )
            )
    );

    public static final Map<DFCRock, Map<OreDeposit, RegistryObject<Block>>> DFC_ORE_DEPOSITS = Helpers.mapOfKeys(DFCRock.class, rock ->
            Helpers.mapOfKeys(OreDeposit.class, ore ->
                    register("deposit/" + ore.name() + "/" + rock.name(), () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(rock.category().hardness(2.0f)))) // Same hardness as gravel
            )
    );

    public static final Map<DFCMetal.DFCDefault, Map<Metal.BlockType, RegistryObject<Block>>> DFC_METALS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
            Helpers.mapOfKeys(Metal.BlockType.class, type -> type.has(Metal.Default.BISMUTH), type ->
                    register(type.createName(metal), type.create(metal), type.createBlockItem(new Item.Properties()))
            )
    );

    public static final Map<Metal.Default, Map<DFCMetal.DFCBlockType, RegistryObject<Block>>> METALS_DFC_BLOCKS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasParts, metal ->
            Helpers.mapOfKeys(DFCMetal.DFCBlockType.class, type ->
                    register(type.createName(metal), type.create(metal), type.createBlockItem(new Item.Properties()))
            )
    );

    public static final Map<DFCMetal.DFCDefault, Map<DFCMetal.DFCBlockType, RegistryObject<Block>>> DFC_METALS_DFC_BLOCKS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
            Helpers.mapOfKeys(DFCMetal.DFCBlockType.class, type ->
                    register(type.createName(metal), type.create(metal), type.createBlockItem(new Item.Properties()))
            )
    );
    public static final Map<Metal.Default, RegistryObject<Block>> METAL_GATES = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasTools, metals ->(
            register(("metal/gate/"+ metals.name()), () ->
                    new GateBlock(BlockBehaviour.Properties.of()
                            .mapColor(metals.mapColor())
                            .requiresCorrectToolForDrops()
                            .strength(6.0F, 7.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion()))));

    public static final RegistryObject<Block> PLAIN_TILES = register("ceramic/tiles/normal/plain", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> PLAIN_SMALL_TILES = register("ceramic/tiles/small/plain", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> PLAIN_SHINGLES = register("ceramic/shingles/plain", () -> new StairBlock(() -> PLAIN_TILES.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F).noOcclusion()));
    public static final Map<DyeColor, RegistryObject<Block>> PAINTED_TILES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/tiles/normal/" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> TERRACOTTA_PAINTED_TILES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/tiles/normal/terracotta_" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> GLAZED_TILES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/tiles/glazed/" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> SMALL_PAINTED_TILES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/tiles/small/" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> SMALL_TERRACOTTA_PAINTED_TILES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/tiles/small/terracotta_" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> PAINTED_BRICKS = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/bricks/" + color.getName()), () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS).mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> TERRACOTTA_PAINTED_BRICKS = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/bricks/terracotta_" + color.getName()), () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS).mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> PAINTED_SHINGLES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/shingles/" + color.getName()), () -> new StairBlock(() -> PAINTED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F).noOcclusion())));
    public static final Map<DyeColor, RegistryObject<Block>> TERRACOTTA_PAINTED_SHINGLES = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("ceramic/shingles/terracotta_" + color.getName()), () -> new StairBlock(() -> PAINTED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));

    public static final DecorationBlockRegistryObject PLAIN_TILES_DECORATIONS = new DecorationBlockRegistryObject(
            register(("ceramic/tiles/normal/plain_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/normal/plain_stairs"), () -> new StairBlock(() -> PLAIN_TILES.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/normal/plain_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, DecorationBlockRegistryObject> PAINTED_TILES_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/tiles/normal/" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/normal/" + color.getName() + "_stairs"), () -> new StairBlock(() -> PAINTED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/normal/" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));
    public static final Map<DyeColor, DecorationBlockRegistryObject> TERRACOTTA_PAINTED_TILES_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/tiles/normal/terracotta_" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/normal/terracotta_" + color.getName() + "_stairs"), () -> new StairBlock(() -> TERRACOTTA_PAINTED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/normal/terracotta_" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));
    public static final Map<DyeColor, DecorationBlockRegistryObject> GLAZED_TILES_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/tiles/glazed/" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/glazed/" + color.getName() + "_stairs"), () -> new StairBlock(() -> GLAZED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/glazed/" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));

    public static final DecorationBlockRegistryObject PLAIN_SMALL_TILES_DECORATIONS = new DecorationBlockRegistryObject(
            register(("ceramic/tiles/small/plain_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/small/plain_stairs"), () -> new StairBlock(() -> PLAIN_SMALL_TILES.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/small/plain_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, DecorationBlockRegistryObject> SMALL_PAINTED_TILES_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/tiles/small/" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/small/" + color.getName() + "_stairs"), () -> new StairBlock(() -> SMALL_PAINTED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/small/" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));
    public static final Map<DyeColor, DecorationBlockRegistryObject> SMALL_TERRACOTTA_PAINTED_TILES_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/tiles/small/terracotta_" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/small/terracotta_" + color.getName() + "_stairs"), () -> new StairBlock(() -> SMALL_TERRACOTTA_PAINTED_TILES.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/tiles/small/terracotta_" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));

    public static final Map<DyeColor, DecorationBlockRegistryObject> PAINTED_BRICKS_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/bricks/" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/bricks/" + color.getName() + "_stairs"), () -> new StairBlock(() -> PAINTED_BRICKS.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/bricks/" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));
    public static final Map<DyeColor, DecorationBlockRegistryObject> TERRACOTTA_PAINTED_BRICKS_DECORATIONS = Helpers.mapOfKeys(DyeColor.class, color -> new DecorationBlockRegistryObject(
            register(("ceramic/bricks/terracotta_" + color.getName() + "_slab"), () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/bricks/terracotta_" + color.getName() + "_stairs"), () -> new StairBlock(() -> TERRACOTTA_PAINTED_BRICKS.get(color).get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
            register(("ceramic/bricks/terracotta_" + color.getName() + "_wall"), () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F)))));


    public static final RegistryObject<Block> PLAIN_PLASTER = register("plaster/plain", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final Map<DyeColor, RegistryObject<Block>> PAINTED_PLASTER = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("plaster/" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));
    public static final Map<DyeColor, RegistryObject<Block>> TERRACOTTA_PAINTED_PLASTER = Helpers.mapOfKeys(DyeColor.class, color ->
            register(("plaster/terracotta_" + color.getName()), () -> new Block(BlockBehaviour.Properties.of().mapColor(color.getMapColor()).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));



    public static final Map<DFCMetal.DFCDefault, RegistryObject<LiquidBlock>> DFC_METAL_FLUIDS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
            registerNoItem("metal/fluid/" + metal.name(), () -> new LiquidBlock(DFCFluids.METALS.get(metal).source(), BlockBehaviour.Properties.copy(Blocks.LAVA).noLootTable()))
    );

    public static final RegistryObject<LiquidBlock> PLASTER_FLUID = registerNoItem("fluid/plaster", () -> new LiquidBlock(DFCFluids.PLASTER.flowing(), BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(DFCBlocks.BLOCKS, DFCItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}
