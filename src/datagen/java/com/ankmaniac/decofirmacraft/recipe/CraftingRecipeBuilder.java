package com.ankmaniac.decofirmacraft.recipe;

import com.google.common.collect.*;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import org.jetbrains.annotations.Nullable;
import java.util.*;

/**
 * An enhanced crafting recipe builder allowing folder names. Also contains all factory functions for the related builders
 * like {@link #shaped(String, CraftingBookCategory, ItemLike, int)} and {@link #shapeless(String, CraftingBookCategory, ItemLike, int)}
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class CraftingRecipeBuilder<B extends CraftingRecipeBuilder<B>> extends DirectoryAwareBuilder implements RecipeBuilder {

	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	protected String group;

	protected CraftingRecipeBuilder(final String directory) {
		super(directory);
	}

	public static ShapelessCraftingRecipeBuilder shapeless(final ItemLike result) {
		return shapeless("crafting", result);
	}

	public static ShapelessCraftingRecipeBuilder shapeless(final ItemLike result, final int count) {
		return shapeless("crafting", CraftingBookCategory.MISC, result, count);
	}

	public static ShapelessCraftingRecipeBuilder shapeless(final String directory, final ItemLike result) {
		return shapeless(directory, CraftingBookCategory.MISC, result, 1);
	}

	public static ShapelessCraftingRecipeBuilder shapeless(final String directory, final CraftingBookCategory craftingBookCategory,
			final ItemLike result, final int count) {
		return shapeless(directory, craftingBookCategory, new ItemStack(result, count));
	}

	public static ShapelessCraftingRecipeBuilder shapeless(final String directory, final CraftingBookCategory craftingBookCategory,
			final ItemStack result) {
		return new ShapelessCraftingRecipeBuilder(directory, craftingBookCategory, result);
	}

	public static ShapedCraftingRecipeBuilder shaped(final ItemLike result) {
		return shaped("crafting", result);
	}

	public static ShapedCraftingRecipeBuilder shaped(final ItemLike result, final int count) {
		return shaped("crafting", CraftingBookCategory.MISC, result, count);
	}

	public static ShapedCraftingRecipeBuilder shaped(final String directory, final ItemLike result) {
		return shaped(directory, CraftingBookCategory.MISC, result, 1);
	}

	public static ShapedCraftingRecipeBuilder shaped(final String directory, final CraftingBookCategory craftingBookCategory, final ItemLike result,
			final int count) {
		return shaped(directory, craftingBookCategory, new ItemStack(result, count));
	}

	private static ShapedCraftingRecipeBuilder shaped(final String directory, final CraftingBookCategory craftingBookCategory,
			final ItemStack result) {
		return new ShapedCraftingRecipeBuilder(directory, craftingBookCategory, result);
	}

	@Override
	public B unlockedBy(final String criterionName, final Criterion<?> criterion) {
		criteria.put(criterionName, criterion);
		return self();
	}

	@Override
	public B group(@Nullable final String groupName) {
		group = groupName;
		return self();
	}

	@Override
	public void save(final RecipeOutput recipeOutput, final ResourceLocation recipeId) {
		ensureValid(recipeId);
		final var advancement = recipeOutput.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.requirements(AdvancementRequirements.Strategy.OR);
		criteria.forEach(advancement::addCriterion);
		save(recipeOutput, recipeId, createRecipe(), advancement);
	}

	/**
	 * Makes sure that this recipe is valid and obtainable.
	 */
	protected void ensureValid(final ResourceLocation recipeId) {
		if (criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		}
	}

	protected abstract B self();

	protected abstract Recipe<?> createRecipe();

	public static final class ShapedCraftingRecipeBuilder extends CraftingRecipeBuilder<ShapedCraftingRecipeBuilder> {

		private final ItemStack result;
		private final CraftingBookCategory craftingBookCategory;
		private final List<String> rows = Lists.newArrayList();
		private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
		private boolean showNotification = true;

		private ShapedCraftingRecipeBuilder(final String folderName, final CraftingBookCategory craftingBookCategory, final ItemStack result) {
			super(folderName);
			this.craftingBookCategory = craftingBookCategory;
			this.result = result;
		}

		/**
		 * Adds a tag key to the recipe pattern.
		 */
		public ShapedCraftingRecipeBuilder define(final Character symbol, final TagKey<Item> tag) {
			return define(symbol, Ingredient.of(tag));
		}

		/**
		 * Adds an item key to the recipe pattern.
		 */
		public ShapedCraftingRecipeBuilder define(final Character symbol, final ItemLike item) {
			return define(symbol, Ingredient.of(item));
		}

		/**
		 * Adds an ingredient key to the recipe pattern.
		 */
		public ShapedCraftingRecipeBuilder define(final Character symbol, final Ingredient ingredient) {
			if (key.containsKey(symbol)) {
				throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
			}

			if (symbol == ' ') {
				throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
			}

			key.put(symbol, ingredient);
			return self();
		}

		/**
		 * Adds a new row to the pattern for this recipe.
		 */
		public ShapedCraftingRecipeBuilder pattern(final String pattern) {
			if (!rows.isEmpty() && pattern.length() != rows.getFirst().length()) {
				throw new IllegalArgumentException("Pattern must be the same width on every line!");
			}

			rows.add(pattern);
			return self();
		}

		/**
		 * Adds multiple rows to the pattern for this recipe.
		 */
		public ShapedCraftingRecipeBuilder pattern(final String... pattern) {
			Arrays.stream(pattern).forEach(this::pattern);
			return self();
		}

		public ShapedCraftingRecipeBuilder showNotification(final boolean showNotification) {
			this.showNotification = showNotification;
			return self();
		}

		@Override
		protected void ensureValid(final ResourceLocation recipeId) {
			super.ensureValid(recipeId);

			if (rows.isEmpty()) {
				throw new IllegalStateException("No pattern is defined for shaped recipe " + recipeId + "!");
			}

			final var set = Sets.newHashSet(key.keySet());
			set.remove(' ');

			for (final var pattern : rows) {
				for (int i = 0; i < pattern.length(); ++i) {
					final var symbol = pattern.charAt(i);
					if (!key.containsKey(symbol) && symbol != ' ') {
						throw new IllegalStateException("Pattern in recipe " + recipeId + " uses undefined symbol '" + symbol + "'");
					}

					set.remove(symbol);
				}
			}

			if (!set.isEmpty()) {
				throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + recipeId);
			}

			if (rows.size() == 1 && rows.getFirst().length() == 1) {
				throw new IllegalStateException(
						"Shaped recipe " + recipeId + " only takes in a single item - it should be a shapeless recipe instead");
			}
		}

		@Override
		protected ShapedCraftingRecipeBuilder self() {
			return this;
		}

		@Override
		protected ShapedRecipe createRecipe() {
			return new ShapedRecipe(group == null ? "" : group, craftingBookCategory, ShapedRecipePattern.of(key, rows), result, showNotification);
		}

		@Override
		public Item getResult() {
			return result.getItem();
		}
	}

	public static final class ShapelessCraftingRecipeBuilder extends CraftingRecipeBuilder<ShapelessCraftingRecipeBuilder> {

		private final ItemStack result;
		private final CraftingBookCategory craftingBookCategory;
		private final NonNullList<Ingredient> ingredients = NonNullList.create();

		private ShapelessCraftingRecipeBuilder(final String folderName, final CraftingBookCategory craftingBookCategory, final ItemStack result) {
			super(folderName);
			this.craftingBookCategory = craftingBookCategory;
			this.result = result;
		}

		/**
		 * Adds an ingredient that can be any item in the given tag.
		 */
		public ShapelessCraftingRecipeBuilder requires(final TagKey<Item> tag) {
			return requires(Ingredient.of(tag));
		}

		/**
		 * Adds an ingredient of the given item.
		 */
		public ShapelessCraftingRecipeBuilder requires(final ItemLike item) {
			return requires(Ingredient.of(item));
		}

		/**
		 * Adds the given item as an ingredient multiple times.
		 */
		public ShapelessCraftingRecipeBuilder requires(final ItemLike item, final int quantity) {
			for (int i = 0; i < quantity; ++i) requires(item);
			return this;
		}

		/**
		 * Adds an ingredient.
		 */
		public ShapelessCraftingRecipeBuilder requires(final Ingredient ingredient) {
			ingredients.add(ingredient);
			return this;
		}

		/**
		 * Adds an ingredient multiple times.
		 */
		public ShapelessCraftingRecipeBuilder requires(final Ingredient ingredient, final int quantity) {
			for (int i = 0; i < quantity; ++i) requires(ingredient);

			return this;
		}

		@Override
		protected void ensureValid(final ResourceLocation recipeId) {
			super.ensureValid(recipeId);
			if (ingredients.isEmpty()) throw new IllegalStateException("Recipe must have at least 1 ingredient");
		}

		@Override
		protected ShapelessCraftingRecipeBuilder self() {
			return this;
		}

		@Override
		protected ShapelessRecipe createRecipe() {
			return new ShapelessRecipe(group == null ? "" : group, craftingBookCategory, result, ingredients);
		}

		@Override
		public Item getResult() {
			return result.getItem();
		}
	}
}