package com.ankmaniac.decofirmacraft.common.items;

import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blocks.metal.DFCMetal;
import com.ankmaniac.decofirmacraft.common.blocks.rock.CustomDFCRockBlocks;
import com.ankmaniac.decofirmacraft.common.blocks.rock.CustomRockBlocks;
import com.ankmaniac.decofirmacraft.common.blocks.rock.DFCOre;
import com.ankmaniac.decofirmacraft.common.blocks.rock.DFCRock;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.DecorationBlockRegistryObject;
import net.dries007.tfc.common.blocks.OreDeposit;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.SelfTests;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DFCTabs.CreativeTabHolder ROCKS =
            register("rock_tab", () -> new ItemStack(DFCBlocks.CUSTOM_ROCK_BLOCKS.get(Rock.ANDESITE).get(CustomRockBlocks.PILLAR).get()), DFCTabs::fillRock);
    public static final DFCTabs.CreativeTabHolder ORES =
            register("ore_tab", () -> new ItemStack(DFCBlocks.DFC_ROCK_ORES.get(DFCRock.ARKOSE).get(Ore.DIAMOND).get()), DFCTabs::fillOre);
    public static final DFCTabs.CreativeTabHolder METAL =
            register("metal_tab", () -> new ItemStack(DFCBlocks.METAL_GATES.get(Metal.Default.WROUGHT_IRON).get()), DFCTabs::fillMetal);
    public static final DFCTabs.CreativeTabHolder CERAMICS =
            register("ceramics_tab", () -> new ItemStack(DFCBlocks.PAINTED_SHINGLES.get(DyeColor.RED).get()), DFCTabs::fillCeramics);
    public static final DFCTabs.CreativeTabHolder MISC =
            register("misc_tab", () -> new ItemStack(DFCBlocks.PLAIN_PLASTER.get()), DFCTabs::fillMisc);


    private static void fillRock(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for(Rock rock : Rock.VALUES)
        {
            for (CustomRockBlocks type : CustomRockBlocks.VALUES)
            {
                accept(out, DFCBlocks.CUSTOM_ROCK_BLOCKS, rock, type);
                if(type.hasVariants())
                {
                    accept(out, DFCBlocks.ROCK_DECORATIONS.get(rock).get(type));
                }

            }
        }
        for(DFCRock rock : DFCRock.VALUES)
        {
            for (CustomDFCRockBlocks type : CustomDFCRockBlocks.VALUES)
            {
                accept(out, DFCBlocks.CUSTOM_DFC_ROCK_BLOCKS, rock, type);
                if(type.hasVariants())
                {
                    accept(out, DFCBlocks.DFC_ROCK_DECORATIONS.get(rock).get(type));
                }
                if(rock.category() == RockCategory.IGNEOUS_EXTRUSIVE)
                {
                   accept(out, DFCBlocks.DFC_MAGMA_BLOCKS.get(rock));
                }
            }
        }
        for(DFCRock rock : DFCRock.VALUES)
        {
            for (Rock.BlockType type : Rock.BlockType.VALUES)
            {
                accept(out, DFCBlocks.CUSTOM_ROCK_TYPES, rock, type);
                if(type.hasVariants())
                {
                    accept(out, DFCBlocks.CUSTOM_DFC_ROCK_DECORATIONS.get(rock).get(type));
                }
            }
            accept(out, DFCItems.DFC_BRICKS, rock);
        }
    }

    private static void fillOre(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for (DFCOre ore : DFCOre.values())
        {
            if (ore.isGraded())
            {
                accept(out, DFCBlocks.DFC_SMALL_ORES, ore);
                accept(out, DFCItems.GRADED_ORES, ore, Ore.Grade.POOR);
                accept(out, DFCItems.GRADED_ORES, ore, Ore.Grade.NORMAL);
                accept(out, DFCItems.GRADED_ORES, ore, Ore.Grade.RICH);
            }
        }
        for (DFCOre ore : DFCOre.values())
        {
            if (!ore.isGraded())
            {
                accept(out, DFCItems.ORES, ore);
            }
        }
        for(DFCOre ore : DFCOre.values())
        {
            if (ore.isGraded())
            {
                DFCBlocks.DFC_GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                DFCBlocks.DFC_ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
        for(Ore ore : Ore.values())
        {
            if (ore.isGraded())
            {
                DFCBlocks.DFC_ROCK_GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                DFCBlocks.DFC_ROCK_ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
        for(DFCOre ore : DFCOre.values())
        {
            if (ore.isGraded())
            {
                DFCBlocks.DFC_ROCK_DFC_GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                DFCBlocks.DFC_ROCK_DFC_ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
        for (OreDeposit deposit : OreDeposit.values())
        {
            DFCBlocks.DFC_ORE_DEPOSITS.values().forEach(map -> accept(out, map, deposit));
        }
    }

    private static void fillMetal(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
            for (DFCMetal.DFCDefault.DFCItemType type : DFCMetal.DFCDefault.DFCItemType.values())
            {
                accept(out, DFCItems.METAL_ITEMS, dfcmetal, type);
            }
        }
        for(Metal.Default metal : Metal.Default.values())
        {
            if(metal.hasParts()) {
                accept(out, DFCItems.METAL_POWDERS.get(metal));
            }
        }
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
                accept(out, DFCItems.DFC_METAL_POWDERS, dfcmetal);
        }
        for(Metal.Default metal : Metal.Default.values())
        {
            if(metal.hasUtilities()) {
                accept(out, DFCBlocks.METAL_GATES.get(metal));
            }
        }
        for(Metal.Default metal : Metal.Default.values())
        {
            for(DFCMetal.DFCBlockType type : DFCMetal.DFCBlockType.values())
            {
                accept(out, DFCBlocks.METALS_DFC_BLOCKS, metal, type);
            }
        }
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
            for (DFCMetal.DFCBlockType type : DFCMetal.DFCBlockType.values())
            {
                accept(out, DFCBlocks.DFC_METALS_DFC_BLOCKS, dfcmetal, type);
            }
        }
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
            for (Metal.BlockType type : Metal.BlockType.values())
            {
                accept(out, DFCBlocks.DFC_METALS, dfcmetal, type);
            }
        }
    }

    private static void fillCeramics(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        accept(out, DFCItems.UNFIRED_SHINGLE);
        accept(out, DFCItems.SHINGLE);
        accept(out, DFCItems.UNFIRED_TILE);
        accept(out, DFCItems.TILE);
        accept(out, DFCBlocks.PLAIN_TILES);
        accept(out, DFCBlocks.PLAIN_TILES_SLAB);
        accept(out, DFCBlocks.PLAIN_TILES_STAIRS);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_TILES, dye);
            accept(out, DFCBlocks.PAINTED_TILES_SLABS, dye);
            accept(out, DFCBlocks.PAINTED_TILES_STAIRS, dye);
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_TILES, dye);
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_TILES_SLABS, dye);
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_TILES_STAIRS, dye);
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.GLAZED_TILES, dye);
            accept(out, DFCBlocks.GLAZED_TILES_SLABS, dye);
            accept(out, DFCBlocks.GLAZED_TILES_STAIRS, dye);
        }
        accept(out, DFCBlocks.PLAIN_SMALL_TILES);
        accept(out, DFCBlocks.PLAIN_SMALL_TILES_SLAB);
        accept(out, DFCBlocks.PLAIN_SMALL_TILES_STAIRS);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.SMALL_PAINTED_TILES, dye);
            accept(out, DFCBlocks.SMALL_PAINTED_TILES_SLABS, dye);
            accept(out, DFCBlocks.SMALL_PAINTED_TILES_STAIRS, dye);
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.SMALL_TERRACOTTA_PAINTED_TILES, dye);
            accept(out, DFCBlocks.SMALL_TERRACOTTA_PAINTED_TILES_SLABS, dye);
            accept(out, DFCBlocks.SMALL_TERRACOTTA_PAINTED_TILES_STAIRS, dye);
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_BRICKS, dye);
            accept(out, DFCBlocks.PAINTED_BRICKS_DECORATIONS.get(dye));
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_BRICKS, dye);
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_BRICKS_DECORATIONS.get(dye));
        }
        accept(out, DFCBlocks.PLAIN_SHINGLES);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_SHINGLES, dye);
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_SHINGLES, dye);
        }
    }

    private static void fillMisc(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for(Wood wood : Wood.values())
        {
            accept(out, DFCBlocks.WOOD_PANELS, wood);
        }
        accept(out, DFCItems.PAINTBRUSH);
        accept(out, DFCItems.GYPSUM_POWDER);
        accept(out, DFCItems.PLASTER_FLUID_BUCKET);
        accept(out, DFCBlocks.PLAIN_PLASTER);
        accept(out, DFCBlocks.PLAIN_PLASTER_SLAB);
        accept(out, DFCBlocks.PLAIN_PLASTER_STAIRS);
        accept(out, DFCBlocks.PLAIN_PLASTER_PILLAR);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_PLASTER, dye);
            accept(out, DFCBlocks.PAINTED_PLASTER_SLABS, dye);
            accept(out, DFCBlocks.PAINTED_PLASTER_STAIRS, dye);
            accept(out, DFCBlocks.PAINTED_PLASTER_PILLARS, dye);
        }
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_PLASTER, dye);
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_PLASTER_SLABS, dye);
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_PLASTER_STAIRS, dye);
            accept(out, DFCBlocks.TERRACOTTA_PAINTED_PLASTER_PILLARS, dye);
        }
        accept(out, DFCItems.CONCRETE_FLUID_BUCKET);
        accept(out, DFCItems.CINDER_BLOCK_MOLD);
        accept(out, DFCItems.SLAB_MOLD);
        accept(out, DFCItems.FILLED_CINDER_BLOCK_MOLD);
        accept(out, DFCItems.FILLED_SLAB_MOLD);
        accept(out, DFCItems.CINDER_BLOCK);
        accept(out, DFCItems.CONCRETE_SLAB);
        accept(out, DFCBlocks.PLAIN_SMOOTH_CONCRETE);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_SMOOTH_CONCRETE, dye);
        }
        accept(out, DFCBlocks.PLAIN_SLAB_CONCRETE);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_SLAB_CONCRETE, dye);
        }
        accept(out, DFCBlocks.PLAIN_CONCRETE_BRICKS);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.PAINTED_CONCRETE_BRICKS, dye);
        }
        accept(out, DFCBlocks.PLAIN_LARGE_SLAB_CONCRETE);
        accept(out, DFCItems.LEADED_SILICA_GLASS_BATCH);
        accept(out, DFCItems.LEADED_HEMATITIC_GLASS_BATCH);
        accept(out, DFCItems.LEADED_OLIVINE_GLASS_BATCH);
        accept(out, DFCItems.LEADED_VOLCANIC_GLASS_BATCH);

        accept(out, DFCBlocks.FOGGY_GLASS);
        accept(out, DFCBlocks.FOGGY_GLASS_PANE);
        accept(out, DFCBlocks.LEADED_GLASS);
        accept(out, DFCBlocks.LEADED_GLASS_PANE);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.STAINED_LEADED_GLASS, dye);
            accept(out, DFCBlocks.STAINED_LEADED_GLASS_PANE.get(dye));
        }
        accept(out, DFCItems.GLASS_BRICK);
        accept(out, DFCItems.FOGGY_BRICK);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCItems.STAINED_GLASS_BRICK, dye);
        }
        accept(out, DFCBlocks.GLASS_BRICKS);
        accept(out, DFCBlocks.GLASS_BRICKS_PANE);
        accept(out, DFCBlocks.FOGGY_GLASS_BRICKS);
        accept(out, DFCBlocks.FOGGY_GLASS_BRICKS_PANE);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.STAINED_GLASS_BRICKS, dye);
            accept(out, DFCBlocks.STAINED_GLASS_BRICKS_PANE.get(dye));
        }
        accept(out, DFCBlocks.GLASS_TILES);
        accept(out, DFCBlocks.GLASS_TILES_PANE);
        accept(out, DFCBlocks.FOGGY_GLASS_TILES);
        accept(out, DFCBlocks.FOGGY_GLASS_TILES_PANE);
        for(DyeColor dye : DyeColor.values())
        {
            accept(out, DFCBlocks.STAINED_GLASS_TILES, dye);
            accept(out, DFCBlocks.STAINED_GLASS_TILES_PANE.get(dye));
        }
    }


    private static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == Items.AIR)
        {
            TerraFirmaCraft.LOGGER.error("BlockItem with no Item added to creative tab: " + reg);
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }
    private static <T extends ItemLike, R extends Supplier<T>, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key)
    {
        if (map.containsKey(key))
        {
            out.accept(map.get(key).get());
        }
    }
    private static <T extends ItemLike, R extends Supplier<T>, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2)
    {
        if (map.containsKey(key1) && map.get(key1).containsKey(key2))
        {
            out.accept(map.get(key1).get(key2).get());
        }
    }
    private static void accept(CreativeModeTab.Output out, DecorationBlockRegistryObject decoration)
    {
        out.accept(decoration.stair().get());
        out.accept(decoration.slab().get());
        out.accept(decoration.wall().get());
    }

    private static CreativeTabHolder register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final RegistryObject<CreativeModeTab> reg = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("dfc.creative_tab." + name))
                .displayItems(displayItems)
                .build());
        return new DFCTabs.CreativeTabHolder(reg, displayItems);
    }

    public record CreativeTabHolder(RegistryObject<CreativeModeTab> tab, CreativeModeTab.DisplayItemsGenerator generator) {}
}
