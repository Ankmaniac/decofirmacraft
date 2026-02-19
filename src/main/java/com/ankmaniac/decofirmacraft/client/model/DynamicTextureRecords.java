package com.ankmaniac.decofirmacraft.client.model;

import com.ankmaniac.decofirmacraft.util.DynamicTextureData;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.DynamicTextureSet.DYNAMIC_TEXTURE_SET_CODEC;
import static com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.WeightedDynamicTextureSet.WEIGHTED_DYNAMIC_TEXTURE_SET_CODEC;

public class DynamicTextureRecords
{
    /**
     * Keys are currently used in chiseled blocks for 8 individual chunks and painted/wallpapered blocks for 6 faces.
     * Model loader will check for whichever value is keyed to its "identifier".
     * Leaving a single entry will bypass.
     */
    public record DynamicTextureModelData(ImmutableMap<String, Either<DynamicTextureSet, List<WeightedDynamicTextureSet>>> dynamicModelData)
    {
        public static final ModelProperty<DynamicTextureModelData> PROPERTY = new ModelProperty<>();
    }

    public record DynamicTextureEither(Either<DynamicTextureSet, List<WeightedDynamicTextureSet>> dynamicTextureEither)
    {
        public static final Codec<DynamicTextureEither> DYNAMIC_TEXTURE_EITHER_CODEC = Codec.either(
                WEIGHTED_DYNAMIC_TEXTURE_SET_CODEC.listOf(), DYNAMIC_TEXTURE_SET_CODEC).xmap(
                either -> new DynamicTextureEither(either.swap()),
                dynamicTextureEither -> dynamicTextureEither.dynamicTextureEither.swap());

    }

    /**
     * Stores weights per set of textures and transformation overrides.
     * Leave a single entry to bypass.
     */
    public record WeightedDynamicTextureSet(Integer weight, DynamicTextureSet dynamicTextureSet)
    {
        public static final Codec<WeightedDynamicTextureSet> WEIGHTED_DYNAMIC_TEXTURE_SET_CODEC = RecordCodecBuilder
                .create(instance ->
                        instance.group(Codec.INT.fieldOf("weight").forGetter(WeightedDynamicTextureSet::weight),
                                        DYNAMIC_TEXTURE_SET_CODEC.fieldOf("texture_map").forGetter(WeightedDynamicTextureSet::dynamicTextureSet))
                                .apply(instance, WeightedDynamicTextureSet::new));

    }

    /**
     * Stores a set of textures and an optional transformation override for the relevant model.
     * An entry in the texture map marked as "default" will make all dynamic texture slots within the model without an entry use it.
     */
    public record DynamicTextureSet(Map<String, ResourceLocation> textureMap, Transformation override)
    {
        // Transformation codec fields
        // VECTOR3F.fieldOf("translation")
        // QUATERNIONF.fieldOf("left_rotation")
        // VECTOR3F.fieldOf("scale")
        // QUATERNIONF.fieldOf("right_rotation")

        public static final Codec<DynamicTextureSet> DYNAMIC_TEXTURE_SET_CODEC = RecordCodecBuilder
                .create(instance ->
                        instance.group(Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).fieldOf("textures")
                                                .forGetter(DynamicTextureSet::textureMap),
                                        Transformation.CODEC.optionalFieldOf("overrides", Transformation.identity())
                                                .forGetter(DynamicTextureSet::override))
                                .apply(instance, DynamicTextureSet::new));

    }
}