package com.redstoneguy10ls.decofirmacraft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class DFCTags {
    public static class Blocks{
        public static final TagKey<Block> ROCK_COLUMNS = create("rock_columns");
        public static final TagKey<Block> METAL_GATES = create("metal_gates");

        private static TagKey<Block> create(String id)
        {
            return TagKey.create(Registries.BLOCK, DFCHelpers.identifier(id));
        }
    }
}
