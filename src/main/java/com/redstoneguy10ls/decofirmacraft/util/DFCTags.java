package com.redstoneguy10ls.decofirmacraft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class DFCTags {
    public static class Blocks{
        public static final TagKey<Block> ROCK_COLUMNS = create("rock/columns");
        public static final TagKey<Block> ROCK_PILLARS = create("rock/pillars");
        public static final TagKey<Block> ROCK_ROADS = create("rock/roads");
        public static final TagKey<Block> ROCK_TILESS = create("rock/tiles");
        public static final TagKey<Block> ROCK_THICK_OUTLINES = create("rock/thick_outlines");
        public static final TagKey<Block> ROCK_THIN_OUTLINES = create("rock/thin_outlines");
        public static final TagKey<Block> METAL_GATES = create("metal/gates");
        public static final TagKey<Block> METAL_BRICKS_BLOCKS = create("metal_bricks_blocks");
        public static final TagKey<Block> METAL_CUT_BLOCKS = create("metal_cut_blocks");
        public static final TagKey<Block> METAL_PILLAR_BLOCKS = create("metal_pillar_blocks");
        public static final TagKey<Block> METAL_SMOOTH_BLOCKS = create("metal_smooth_blocks");
        public static final TagKey<Block> NORMAL_ALUMINA_ORE = create("ores/alumina/normal");
        public static final TagKey<Block> POOR_ALUMINA_ORE = create("ores/alumina/poor");
        public static final TagKey<Block> RICH_ALUMINA_ORE = create("ores/alumina/rich");
        public static final TagKey<Block> NORMAL_ALUMINUM_ORE = create("ores/aluminum/normal");
        public static final TagKey<Block> POOR_ALUMINUM_ORE = create("ores/aluminum/poor");
        public static final TagKey<Block> RICH_ALUMINUM_ORE = create("ores/aluminum/rich");
        public static final TagKey<Block> NORMAL_LEAD_ORE = create("ores/lead/normal");
        public static final TagKey<Block> POOR_LEAD_ORE = create("ores/lead/poor");
        public static final TagKey<Block> RICH_LEAD_ORE = create("ores/lead/rich");
        public static final TagKey<Block> NORMAL_PLATINUM_ORE = create("ores/platinum/normal");
        public static final TagKey<Block> POOR_PLATINUM_ORE = create("ores/platinum/poor");
        public static final TagKey<Block> RICH_PLATINUM_ORE = create("ores/platinum/rich");

        private static TagKey<Block> create(String id)
        {
            return TagKey.create(Registries.BLOCK, DFCHelpers.identifier(id));
        }
    }
    public static class Items{
        public static final TagKey<Item> ROCK_COLUMNS = create("rock/columns");
        public static final TagKey<Item> ROCK_PILLARS = create("rock/pillars");
        public static final TagKey<Item> ROCK_ROADS = create("rock/roads");
        public static final TagKey<Item> ROCK_TILESS = create("rock/tiles");
        public static final TagKey<Item> ROCK_THICK_OUTLINES = create("rock/thick_outlines");
        public static final TagKey<Item> ROCK_THIN_OUTLINES = create("rock/thin_outlines");
        public static final TagKey<Item> ALUMINA_ITEMS = create("metal_item/alumina");
        public static final TagKey<Item> ALUMINUM_ITEMS = create("metal_item/aluminum");
        public static final TagKey<Item> LEAD_ITEMS = create("metal_item/lead");
        public static final TagKey<Item> PEWTER_ITEMS = create("metal_item/pewter");
        public static final TagKey<Item> PLATINUM_ITEMS = create("metal_item/platinum");

        private static TagKey<Item> create(String id)
        {
            return TagKey.create(Registries.ITEM, DFCHelpers.identifier(id));
        }
    }
}
