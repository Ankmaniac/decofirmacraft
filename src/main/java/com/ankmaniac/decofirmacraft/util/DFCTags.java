package com.ankmaniac.decofirmacraft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class DFCTags {
    public static class Blocks{
        public static final TagKey<Block> ROCK_COLUMNS = create("stones/columns");
        public static final TagKey<Block> ROCK_PILLARS = create("stones/pillars");
        public static final TagKey<Block> ROCK_ROADS = create("stones/roads");
        public static final TagKey<Block> ROCK_TILES = create("stones/tiles");
        public static final TagKey<Block> ROCK_THICK_OUTLINES = create("stones/thick_outlines");
        public static final TagKey<Block> ROCK_THIN_OUTLINES = create("stones/thin_outlines");
        public static final TagKey<Block> ROCK_RAILS = create("stones/rails");
        public static final TagKey<Block> METAL_GATES = create("metal/gates");
        public static final TagKey<Block> METAL_BRICKS_BLOCKS = create("metal_bricks_blocks");
        public static final TagKey<Block> METAL_CUT_BLOCKS = create("metal_cut_blocks");
        public static final TagKey<Block> METAL_PILLAR_BLOCKS = create("metal_pillar_blocks");
        public static final TagKey<Block> METAL_SMOOTH_BLOCKS = create("metal_smooth_blocks");
        public static final TagKey<Block> PLASTERABLE_BLOCKS = create("plasterable_blocks");
        public static final TagKey<Block> MINEABLE_WITH_PAINTBRUSH = create("mineable_with_paintbrush"); //its stupid this needs to exist
        public static final TagKey<Block> CHISELABLE = create("chiselable");

        private static TagKey<Block> create(String id)
        {
            return TagKey.create(Registries.BLOCK, DFCHelpers.identifier(id));
        }
    }

    public static class Items{
        public static final TagKey<Item> ROCK_COLUMNS = create("stones/columns");
        public static final TagKey<Item> ROCK_PILLARS = create("stones/pillars");
        public static final TagKey<Item> ROCK_ROADS = create("stones/roads");
        public static final TagKey<Item> ROCK_TILES = create("stones/tiles");
        public static final TagKey<Item> ROCK_THICK_OUTLINES = create("stones/thick_outlines");
        public static final TagKey<Item> ROCK_THIN_OUTLINES = create("stones/thin_outlines");
        public static final TagKey<Item> ROCK_RAILS = create("stones/rails");
        public static final TagKey<Item> CANDLES = create("candles");
        public static final TagKey<Item> CHISELABLE = create("chiselable");

        private static TagKey<Item> tag(TagKey<Block> blockTag)
        {
            return TagKey.create(Registries.ITEM, blockTag.location());
        }

        private static TagKey<Item> create(String name)
        {
            return TagKey.create(Registries.ITEM, DFCHelpers.identifier(name));
        }
    }
}
