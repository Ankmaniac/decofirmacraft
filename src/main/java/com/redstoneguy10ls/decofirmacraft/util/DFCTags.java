package com.redstoneguy10ls.decofirmacraft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class DFCTags {
    public static class Blocks{
        public static final TagKey<Block> Rock_Columns = create("rock_columns");

        private static TagKey<Block> create(String id)
        {
            return TagKey.create(Registries.BLOCK, DFCHelpers.identifier(id));
        }
    }
}
