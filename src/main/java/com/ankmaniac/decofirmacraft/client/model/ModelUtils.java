package com.ankmaniac.decofirmacraft.client.model;

import com.ankmaniac.decofirmacraft.mixin.client.accessors.ElementsModelAccessor;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * General model utils. Primarily for dynamic baking of models such as via
 * {@link #bakeDynamic(IGeometryBakingContext, ElementsModel, ModelState)}
 */
public final class ModelUtils {

    /**
     * @param context    The bake context
     * @param model      The model
     * @param modelState The model state
     */
    public static BakedModel bakeDynamic(final IGeometryBakingContext context, final ElementsModel model,
                                         final ModelState modelState)
    {
        return bakeWithElements(context, ((ElementsModelAccessor) model).getElements(), modelState);
    }

    //Current changes: bypass bakeWithElements, pass straight into bakeModel
    /**
     * @param context    The bake context
     * @param elements   The model elements
     * @param modelState The model state
     */
    public static BakedModel bakeWithElements(final IGeometryBakingContext context, final List<BlockElement> elements,
                                              final ModelState modelState)
    {
        return bakeModel(context, elements, Material::sprite, modelState, ItemOverrides.EMPTY);
    }

    /**
     * @param context      The bake context
     * @param elements     The model elements
     * @param spriteGetter The sprite getter
     * @param modelState   The model state
     * @param overrides    The item overrides
     */
    public static BakedModel bakeModel(final IGeometryBakingContext context, final List<BlockElement> elements,
                                       final Function<Material, TextureAtlasSprite> spriteGetter, final ModelState modelState,
                                       final ItemOverrides overrides)
    {
        final TextureAtlasSprite particle = spriteGetter.apply(context.getMaterial("particle"));

        final SimpleBakedModel.Builder builder = getBuilder(context, overrides).particle(particle);
        final IQuadTransformer quadTransformer = getTransformer(modelState, context.getRootTransform(),
                (context instanceof DynamicTextureModel.BakedDynamicTextureModel.DynamicTexturesContext ?
                        ((DynamicTextureModel.BakedDynamicTextureModel.DynamicTexturesContext) context).getOverrides() : null));

        for (final BlockElement part : elements)
        {
            bakePart(builder, context, part, spriteGetter, modelState, quadTransformer);
        }

        final ResourceLocation renderTypeHint = context.getRenderTypeHint();

        return builder.build(renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY);
    }

    /**
     * @param builder         The builder
     * @param context         The bake context
     * @param part            The part to bake
     * @param spriteGetter    The sprite getter
     * @param modelState      The model state
     * @param quadTransformer The quad transformer
     */
    public static void bakePart(final SimpleBakedModel.Builder builder, final IGeometryBakingContext context,
                                final BlockElement part, final Function<Material, TextureAtlasSprite> spriteGetter,
                                final ModelState modelState, final IQuadTransformer quadTransformer)
    {
        for (final Direction direction : part.faces.keySet())
        {
            final BlockElementFace face = part.faces.get(direction);
            String texture = face.texture();
            if (texture.charAt(0) == '#')
            {
                texture = texture.substring(1);
            }
            final TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(texture));
            final BakedQuad bakedQuad = BlockModel.bakeFace(part, face, sprite, direction, modelState);
            quadTransformer.processInPlace(bakedQuad);
            if (face.cullForDirection() == null)
            {
                builder.addUnculledFace(bakedQuad);
            }
            else
            {
                builder.addCulledFace(Direction.rotate(modelState.getRotation().getMatrix(), face.cullForDirection()),
                        bakedQuad);
            }
        }
    }

    /**
     * @param context   Context to initialize the builder with
     * @param overrides The overrides
     */
    public static SimpleBakedModel.Builder getBuilder(final IGeometryBakingContext context,
                                                      final ItemOverrides overrides)
    {
        return new SimpleBakedModel.Builder(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(),
                context.getTransforms(), overrides);
    }

    /**
     * @param modelState     The model state
     * @param transformation The transformation
     */
    public static IQuadTransformer getTransformer(final ModelState modelState, final Transformation transformation,
                                                  final Transformation overrides)
    {
        if (transformation.isIdentity() && overrides.isIdentity())
        {
            return QuadTransformers.empty();
        }
        else
        {
            boolean overridesAreDefault = !overrides.isIdentity();
            Transformation newTransformation = null;
            if (overridesAreDefault)
            {
                newTransformation = new Transformation(
                        !overrides.getTranslation().equals(Transformation.identity().getTranslation()) ? overrides.getTranslation() : transformation.getTranslation(),
                        !overrides.getLeftRotation().equals(Transformation.identity().getLeftRotation()) ? overrides.getLeftRotation() : transformation.getLeftRotation(),
                        !overrides.getScale().equals(Transformation.identity().getScale()) ? overrides.getScale() : transformation.getScale(),
                        !overrides.getRightRotation().equals(Transformation.identity().getRightRotation()) ? overrides.getRightRotation() : transformation.getRightRotation()
                );
            }
            return UnbakedGeometryHelper.applyRootTransform(modelState, overridesAreDefault ? newTransformation : transformation);
        }
    }
}
