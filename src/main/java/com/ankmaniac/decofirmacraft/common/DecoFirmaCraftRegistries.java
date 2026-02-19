package com.ankmaniac.decofirmacraft.common;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.util.DynamicTextureData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class DecoFirmaCraftRegistries
{
    public static final ResourceKey<net.minecraft.core.Registry<DynamicTextureData>> DYNAMIC_TEXTURE_DATA =
            createRegistryKey("dynamic_textures");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String name)
    {
        return ResourceKey.createRegistryKey(DecoFirmaCraft.location(name));
    }
}
