package com.ankmaniac.decofirmacraft.common;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.util.DynamicTextureDataMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public final class DecoFirmaCraftDataMaps
{
    public static final DataMapType<Block, DynamicTextureDataMap> CHISELED_BLOCK = DataMapType.builder(
                    DecoFirmaCraft.location("chiseled_block"), Registries.BLOCK, DynamicTextureDataMap.CODEC).synced(DynamicTextureDataMap.CODEC, true)
            .build();

    public static void registerDataMaps(final RegisterDataMapTypesEvent event)
    {
        event.register(CHISELED_BLOCK);
    }
}
