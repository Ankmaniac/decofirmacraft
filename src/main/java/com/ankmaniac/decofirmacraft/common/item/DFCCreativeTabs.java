package com.ankmaniac.decofirmacraft.common.item;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.block.DFCDecorationBlockHolder;
import com.ankmaniac.decofirmacraft.common.block.metal.DFCExtendedMetal;
import com.ankmaniac.decofirmacraft.common.block.rock.DFCExtendedRock;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public final class DFCCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DecoFirmaCraft.MOD_ID);


    public static final Id MISC = register("misc_tab", () -> new ItemStack(TFCBlocks.PLAIN_ALABASTER), DFCCreativeTabs::fillMiscTab);
    public static final Id ROCK = register("rock_tab", () -> new ItemStack(DFCBlocks.DFC_ROCK_BLOCKS.get(DFCExtendedRock.MARBLE).get(DFCExtendedRock.DFCRockBlockType.COLUMN)), DFCCreativeTabs::fillRockTab);
    public static final Id METAL = register("metal_tab", () -> new ItemStack(DFCBlocks.DFC_METAL_BLOCKS.get(DFCExtendedMetal.GOLD).get(DFCExtendedMetal.DFCMetalBlockType.BRICKS)), DFCCreativeTabs::fillMetalTab);

    public static Stream<CreativeModeTab.DisplayItemsGenerator> generators() {
        return Stream.of(MISC).map(holder -> holder.generator);
    }

    private static void fillMiscTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out) {
    }

    private static void fillRockTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out) {
        for (DFCExtendedRock rock : DFCExtendedRock.values())
        {
            for (DFCExtendedRock.DFCRockBlockType type : new DFCExtendedRock.DFCRockBlockType[]
                    {
                            DFCExtendedRock.DFCRockBlockType.HARDENED,
                            DFCExtendedRock.DFCRockBlockType.RAW,
                            DFCExtendedRock.DFCRockBlockType.PRESSURE_PLATE,
                            DFCExtendedRock.DFCRockBlockType.BUTTON,
                            DFCExtendedRock.DFCRockBlockType.SPIKE,
                            DFCExtendedRock.DFCRockBlockType.COBBLE,
                            DFCExtendedRock.DFCRockBlockType.MOSSY_COBBLE,
                            DFCExtendedRock.DFCRockBlockType.BRICKS,
                            DFCExtendedRock.DFCRockBlockType.CRACKED_BRICKS,
                            DFCExtendedRock.DFCRockBlockType.MOSSY_BRICKS,
                            DFCExtendedRock.DFCRockBlockType.SMOOTH,
                            DFCExtendedRock.DFCRockBlockType.CHISELED,
                            DFCExtendedRock.DFCRockBlockType.AQUEDUCT,
                            DFCExtendedRock.DFCRockBlockType.GRAVEL,
                            DFCExtendedRock.DFCRockBlockType.LOOSE,
                            DFCExtendedRock.DFCRockBlockType.MOSSY_LOOSE,
                            DFCExtendedRock.DFCRockBlockType.PILLAR,
                            DFCExtendedRock.DFCRockBlockType.ROAD,
                            DFCExtendedRock.DFCRockBlockType.TILE,
                            DFCExtendedRock.DFCRockBlockType.OUTLINED,
                            DFCExtendedRock.DFCRockBlockType.POLISHED,
                            DFCExtendedRock.DFCRockBlockType.SMALL_BRICKS,
                            DFCExtendedRock.DFCRockBlockType.LARGE_BRICKS,
                            DFCExtendedRock.DFCRockBlockType.COLUMN,
                            DFCExtendedRock.DFCRockBlockType.RAIL,
                    }) {
                accept(out, DFCBlocks.DFC_ROCK_BLOCKS, rock, type);
                if (type.has(rock) && type.hasVariants())
                {
                    accept(out, DFCBlocks.DFC_ROCK_DECORATIONS.get(rock).get(type));
                }
            }
        }
        DFCBlocks.DFC_ROCK_ANVILS.values().forEach(out::accept);
        DFCBlocks.DFC_MAGMA_BLOCKS.values().forEach(out::accept);
    }

    private static void fillMetalTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out) {
        for (DFCExtendedMetal metal : DFCExtendedMetal.values())
        {
            for (DFCExtendedMetal.DFCMetalBlockType type : new DFCExtendedMetal.DFCMetalBlockType[]
                    {
                            DFCExtendedMetal.DFCMetalBlockType.SMOOTH,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_SMOOTH,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_SMOOTH,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_SMOOTH,
                            DFCExtendedMetal.DFCMetalBlockType.SMOOTH_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_SMOOTH_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_SMOOTH_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_SMOOTH_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.SMOOTH_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_SMOOTH_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_SMOOTH_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_SMOOTH_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.CUT,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_CUT,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_CUT,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_CUT,
                            DFCExtendedMetal.DFCMetalBlockType.CUT_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_CUT_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_CUT_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_CUT_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.CUT_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_CUT_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_CUT_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_CUT_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.BRICKS,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_BRICKS,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_BRICKS,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_BRICKS,
                            DFCExtendedMetal.DFCMetalBlockType.BRICKS_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_BRICKS_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_BRICKS_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_BRICKS_SLAB,
                            DFCExtendedMetal.DFCMetalBlockType.BRICKS_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_BRICKS_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_BRICKS_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_BRICKS_STAIRS,
                            DFCExtendedMetal.DFCMetalBlockType.PILLAR,
                            DFCExtendedMetal.DFCMetalBlockType.EXPOSED_PILLAR,
                            DFCExtendedMetal.DFCMetalBlockType.WEATHERED_PILLAR,
                            DFCExtendedMetal.DFCMetalBlockType.OXIDIZED_PILLAR,
                            DFCExtendedMetal.DFCMetalBlockType.GATE,
                            DFCExtendedMetal.DFCMetalBlockType.GOBLET
                    }) {
                accept(out, DFCBlocks.DFC_METAL_BLOCKS, metal, type);
            }
        }
    }


    private static Id register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
        final var holder = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("dfc.creative_tab." + name))
                .displayItems(displayItems)
                .build());
        return new Id(holder, displayItems);
    }

    private static <R extends ItemLike, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2) {
        if (map.containsKey(key1)) {
            accept(out, map.get(key1), key2);
        }
    }

    private static <R extends ItemLike, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key) {
        if (map.containsKey(key)) {
            out.accept(map.get(key));
        }
    }

    private static void accept(CreativeModeTab.Output out, DFCDecorationBlockHolder decoration) {
        out.accept(decoration.stair());
        out.accept(decoration.slab());
        out.accept(decoration.wall());
    }

    public record Id(DeferredHolder<CreativeModeTab, CreativeModeTab> tab,
                     CreativeModeTab.DisplayItemsGenerator generator) {
    }

}