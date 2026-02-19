package com.ankmaniac.decofirmacraft.common;

import com.ankmaniac.decofirmacraft.util.DynamicTextureData;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class DecoFirmaCraftBuiltInRegistries
{
    public static void registerDatapackRegistries(final DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(DecoFirmaCraftRegistries.DYNAMIC_TEXTURE_DATA, DynamicTextureData.DIRECT_CODEC,
                DynamicTextureData.DIRECT_CODEC);
    }
}
