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

        private static TagKey<Item> create(String id)
        {
            return TagKey.create(Registries.ITEM, DFCHelpers.identifier(id));
        }
    }
}
