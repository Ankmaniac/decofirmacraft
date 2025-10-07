package com.ankmaniac.decofirmacraft.common.block;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * A triple of {@link DeferredHolder}s for slabs, stairs, and walls
 */
public record DFCDecorationBlockHolder(
        DFCBlocks.Id<? extends SlabBlock> slab,
        DFCBlocks.Id<? extends StairBlock> stair,
        DFCBlocks.Id<? extends WallBlock> wall
) {}