package com.ankmaniac.decofirmacraft.recipe;

import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;

/**
 * A simple builder that supports directories
 */
abstract class DirectoryAwareBuilder {

	private final String directory;

	protected DirectoryAwareBuilder(final String directory) {
		this.directory = directory;
	}

	/**
	 * @param recipeOutput The output
	 * @param recipeId The recipe id
	 * @param recipe The recipe
	 * @param advancement The optional advancement builder
	 */
	protected final void save(final RecipeOutput recipeOutput, final ResourceLocation recipeId, final Recipe<?> recipe,
			final @Nullable Builder advancement) {
		recipeOutput.accept(recipeId.withPrefix(directory + "/"), recipe,
				advancement == null ? null : advancement.build(recipeId.withPrefix("recipes/" + directory + "/")));
	}
}