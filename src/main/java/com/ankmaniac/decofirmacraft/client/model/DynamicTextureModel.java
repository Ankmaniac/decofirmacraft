package com.ankmaniac.decofirmacraft.client.model;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import lombok.Getter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DynamicTextureModel implements IUnbakedGeometry<DynamicTextureModel>
{

    public static final ResourceLocation LOADER_ID = DecoFirmaCraft.location("dynamic_texture");

    private static final Codec<Set<String>> RETEXTURED_TEXTURES_CODEC = Codec.either(Codec.STRING,
            Codec.STRING.listOf().comapFlatMap(strings -> {
                if (strings.isEmpty())
                {
                    return DataResult.error(() -> "Must have at least one texture");
                }

                return DataResult.success(Set.copyOf(strings));
            }, List::copyOf)).xmap(stringSetEither -> Either.unwrap(stringSetEither.mapLeft(Set::of)), strings -> {
        if (strings.size() == 1) {
            return Either.left(strings.iterator().next());
        }

        return Either.right(strings);
    });

    public static final IGeometryLoader<DynamicTextureModel> LOADER = DynamicTextureModel::deserialize;

    private final ElementsModel baseModel;
    private final Set<String> dynamicTextures;
    private final String identifier;

    public DynamicTextureModel(ElementsModel baseModel, Set<String> dynamicTextures, String identifier)
    {
        this.identifier = identifier;
        this.baseModel = baseModel;
        this.dynamicTextures = dynamicTextures;
    }

    public static DynamicTextureModel deserialize(final JsonObject jsonObject,
                                                  final JsonDeserializationContext context) throws JsonParseException
    {
        final ElementsModel baseModel = ElementsModel.Loader.INSTANCE.read(jsonObject, context);

        final DataResult<Set<String>> retextured = RETEXTURED_TEXTURES_CODEC.parse(JsonOps.INSTANCE, jsonObject.get("dynamic_textures"));
        retextured.ifError(error -> {
            throw new JsonParseException(error.message());
        });

        final DataResult<String> identifierDataResult = Codec.STRING.parse(JsonOps.INSTANCE, jsonObject.get("identifier"));
        identifierDataResult.ifError(stringError -> {
            throw new JsonParseException(stringError.message());
        });
        final String identifier = identifierDataResult.getOrThrow().isEmpty() ? "default" : identifierDataResult.getOrThrow();

        return new DynamicTextureModel(baseModel, retextured.getOrThrow(), identifier);
    }



    @Override
    public BakedModel bake(final IGeometryBakingContext context, final ModelBaker baker,
                           final Function<Material, TextureAtlasSprite> spriteGetter, final ModelState modelState,
                           final ItemOverrides overrides)
    {
        final BakedModel bake = this.baseModel.bake(context, baker, spriteGetter, modelState, overrides);
        return new BakedDynamicTextureModel(bake, context, this.baseModel, modelState, this.dynamicTextures, this.identifier);
    }

    protected static class BakedDynamicTextureModel extends BakedModelWrapper<BakedModel> implements IDynamicBakedModel
    {
        private final Map<DynamicTextureSet, BakedModel> cache = new ConcurrentHashMap<>();

        private final IGeometryBakingContext context;
        private final ElementsModel baseModel;
        private final ModelState modelState;
        private final Set<String> dynamicTextures;
        private final String identifier;

        public BakedDynamicTextureModel(final BakedModel bake, final IGeometryBakingContext context,
                                        final ElementsModel baseModel, final ModelState modelState,
                                        final Set<String> dynamicTextures, final String identifier)
        {
            super(bake);
            this.context = context;
            this.baseModel = baseModel;
            this.modelState = modelState;
            this.dynamicTextures = dynamicTextures;
            this.identifier = identifier;
        }

        private BakedModel getModel(DynamicTextureModelData property, @Nullable RandomSource random)
        {
            final ImmutableMap<String, Either<DynamicTextureSet, List<WeightedDynamicTextureSet>>> dynamicModelData =
                    property.dynamicModelData();
            final Either<DynamicTextureSet, List<WeightedDynamicTextureSet>> eitherTextureSet =
                    !(dynamicModelData.size() == 1) || !identifier.equals("default") ?
                            dynamicModelData.get(identifier) :
                            dynamicModelData.get(dynamicModelData.keySet().iterator().next());
            final DynamicTextureSet dynamicTextureSet = eitherTextureSet.map(left -> left,
                    right -> getWeightedItem(right, random));

            return this.cache.computeIfAbsent(dynamicTextureSet, this::bake);
        }

        private BakedModel bake(DynamicTextureSet dynamicTextureSet)
        {
            final Map<String, ResourceLocation> textureMap = dynamicTextureSet.textureMap();
            final Transformation overrides = dynamicTextureSet.override();
            log.debug("Baking dynamic texture model {} for {}, part {}", this.context.getModelName(),
                    textureMap.values().toArray(), this.identifier);
            return ModelUtils.bakeDynamic(
                    new DynamicTexturesContext(this.context, this.dynamicTextures, textureMap, overrides),
                    this.baseModel, this.modelState);
        }

        @Override
        public List<BakedQuad> getQuads(final @Nullable BlockState state, final @Nullable Direction side,
                                        final RandomSource random, final ModelData data, final @Nullable RenderType renderType)
        {
            final DynamicTextureModelData property = data.get(DynamicTextureModelData.PROPERTY);
            if (property != null)
            {
                return this.getModel(property, random).getQuads(state, side, random, data, renderType);
            }
            return super.getQuads(state, side, random, data, renderType);
        }

        @Override
        public TextureAtlasSprite getParticleIcon(final ModelData data)
        {
            final DynamicTextureModelData property = data.get(DynamicTextureModelData.PROPERTY);
            if (property != null)
            {
                return this.getModel(property, null).getParticleIcon(data);
            }
            return super.getParticleIcon();
        }

        private DynamicTextureSet getWeightedItem(List<WeightedDynamicTextureSet> entries, @Nullable RandomSource random)
        {
            if (random != null)
            {
                int totalWeight = 0;
                for (WeightedDynamicTextureSet weight : entries)
                {
                    totalWeight += weight.weight();
                }
                int weightedIndex = Math.abs((int) random.nextLong()) % totalWeight;
                for (WeightedDynamicTextureSet weight : entries)
                {
                    weightedIndex -= weight.weight();
                    if (weightedIndex < 0)
                    {
                        return weight.dynamicTextureSet();
                    }
                }
            }
            else
            {
                return entries.getFirst().dynamicTextureSet();
            }
            return null;
        }

        protected static class DynamicTexturesContext extends WrappedGeometryBakingContext
        {
            //Using it to check that the keys in the map are correct
            private final Set<String> dynamicTextures;
            private final Map<String, Material> textureMap;
            @Getter
            private final Transformation overrides;

            public DynamicTexturesContext(final IGeometryBakingContext delegate, final Set<String> dynamicTextures,
                                          final Map<String, ResourceLocation> textureMap, final Transformation overrides)
            {
                super(delegate);
                this.dynamicTextures = dynamicTextures;
                this.textureMap = textureMap.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey, texture
                                -> new Material(InventoryMenu.BLOCK_ATLAS, texture.getValue())));
                this.overrides = overrides;
            }

            @Override
            public boolean hasMaterial(final String name)
            {
                if (this.dynamicTextures.contains(name))
                {
                    if (this.textureMap.containsKey(name))
                    {
                        return !MissingTextureAtlasSprite.getLocation().equals(this.textureMap.get(name).texture());
                    }
                    else if (this.textureMap.containsKey("default"))
                    {
                        return !MissingTextureAtlasSprite.getLocation().equals(this.textureMap.get("default").texture());
                    }
                }
                return super.hasMaterial(name);
            }

            @Override
            public Material getMaterial(final String name)
            {
                if (this.dynamicTextures.contains(name))
                {
                    if (this.textureMap.containsKey(name))
                    {
                        return this.textureMap.get(name);
                    }
                    else if (this.textureMap.containsKey("default"))
                    {
                        return this.textureMap.get("default");
                    }
                }
                return super.getMaterial(name);
            }

        }
    }
}
