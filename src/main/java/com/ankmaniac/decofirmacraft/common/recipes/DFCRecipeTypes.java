package com.ankmaniac.decofirmacraft.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID);

    public static final RegistryObject<RecipeType<PaintingRecipe>> PAINTING = register("painting");

    private static <R extends Recipe<?>> RegistryObject<RecipeType<R>> register(String name)
    {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString()
            {
                return name;
            }
        });
    }
}
