package com.ankmaniac.decofirmacraft.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

@SuppressWarnings("unused")
public class DFCRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);

    public static final RegistryObject<PaintingRecipe.Serializer> PAINTING = register("painting", PaintingRecipe.Serializer::new);

    public static final RegistryObject<AsbestosRecipe.Serializer> ASBESTOS = register("asbestos", AsbestosRecipe.Serializer::new);

    private static <S extends RecipeSerializer<?>> RegistryObject<S> register(String name, Supplier<S> factory)
    {
        return RECIPE_SERIALIZERS.register(name, factory);
    }
}
