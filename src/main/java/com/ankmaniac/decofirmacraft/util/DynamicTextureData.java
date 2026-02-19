package com.ankmaniac.decofirmacraft.util;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.*;
import com.ankmaniac.decofirmacraft.common.DecoFirmaCraftRegistries;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.DynamicTextureEither.DYNAMIC_TEXTURE_EITHER_CODEC;

public record DynamicTextureData(Either<DynamicTextureEither, Map<BlockState, DynamicTextureEither>> dynamicTextureData)
{
    public static final Codec<DynamicTextureData> DIRECT_CODEC = Codec.either(
            Codec.unboundedMap(BlockState.CODEC, DYNAMIC_TEXTURE_EITHER_CODEC), DYNAMIC_TEXTURE_EITHER_CODEC).xmap(
            either -> new DynamicTextureData(either.swap()),
            dynamicTextureData -> dynamicTextureData.dynamicTextureData.swap());

    public static final Codec<Holder<DynamicTextureData>> CODEC = RegistryFileCodec.create(
            DecoFirmaCraftRegistries.DYNAMIC_TEXTURE_DATA, DIRECT_CODEC);

    private static ResourceKey<DynamicTextureData> createKey(final String name) {
        return ResourceKey.create(DecoFirmaCraftRegistries.DYNAMIC_TEXTURE_DATA, DecoFirmaCraft.location(name));
    }

    public static final ResourceKey<DynamicTextureData> MARBLE_TILES = createKey("rock/tile/marble");
    public static final ResourceKey<DynamicTextureData> DEFAULT = MARBLE_TILES;

    /**
     * @param registryName The registry name of the dynamic texture
     * @return The lang key for the dynamic textures name
     */
    public static String getDescriptionId(final ResourceLocation registryName) {
        return registryName.toLanguageKey("dynamic_texture_data");
    }
}
