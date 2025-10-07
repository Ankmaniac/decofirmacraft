package com.ankmaniac.decofirmacraft.recipe;

import com.google.common.collect.*;
import com.google.errorprone.annotations.DoNotCall;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.outputs.*;

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
 * Similar to {@link CraftingRecipeBuilder} but for TFC's advanced crafting recipe types
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class AdvancedCraftingRecipeBuilder<B extends AdvancedCraftingRecipeBuilder<B>> extends DirectoryAwareBuilder implements
		RecipeBuilder {

	protected final ItemStackProvider result;
	protected final List<ItemStackModifier> remainder = new ArrayList<>();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	protected AdvancedCraftingRecipeBuilder(final String directory, final ItemStackProvider result) {
		super(directory);
		this.result = result;
	}

	public static AdvancedShapedRecipeBuilder shaped(final ItemLike result, final int count, final ItemStackModifier... modifiers) {
		return shaped("crafting", ItemStackProvider.of(new ItemStack(result, count), modifiers));
	}

	public static AdvancedShapedRecipeBuilder shaped(final String directory, final ItemStackProvider result) {
		return new AdvancedShapedRecipeBuilder(directory, result);
	}

	public static AdvancedShapelessRecipeBuilder shapeless(final ItemLike result, final int count, final ItemStackModifier... modifiers) {
		return shapeless("crafting", ItemStackProvider.of(new ItemStack(result, count), modifiers));
	}

	public static AdvancedShapelessRecipeBuilder shapeless(final String directory, final ItemStackProvider result) {
		return new AdvancedShapelessRecipeBuilder(directory, result);
	}

	@Override
	public B unlockedBy(final String criterionName, final Criterion<?> criterion) {
		criteria.put(criterionName, criterion);
		return self();
	}

	@Override
	@Deprecated
	@DoNotCall("TFC's advanced recipe types don't support groups")
	public B group(@Nullable final String group) {
		return self();
	}

	@Override
	public Item getResult() {
		return result.stack().getItem();
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
	 * @param stackModifier An {@link ItemStackModifier} for the remainder
	 */
	public B remainder(final ItemStackModifier stackModifier) {
		remainder.add(stackModifier);
		return self();
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

	/**
	 * Adds {@link DamageCraftingRemainderModifier} when set
	 */
	public B damageInputs() {
		return remainder(DamageCraftingRemainderModifier.INSTANCE);
	}

	public static final class AdvancedShapedRecipeBuilder extends AdvancedCraftingRecipeBuilder<AdvancedShapedRecipeBuilder> {

		private final List<String> rows = Lists.newArrayList();
		private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
		private boolean showNotification = true;
		private int inputRow;
		private int inputColumn;

		private AdvancedShapedRecipeBuilder(final String folderName, final ItemStackProvider result) {
			super(folderName, result);
		}

		/**
		 * Sets and defines the input item
		 *
		 * @param row The row index of the input item
		 * @param column The column index of the input item
		 */
		public AdvancedShapedRecipeBuilder inputItem(final Character symbol, final TagKey<Item> tag, final int row, final int column) {
			return inputItem(symbol, Ingredient.of(tag), row, column);
		}

		/**
		 * Sets and defines the input item
		 *
		 * @param row The row index of the input item
		 * @param column The column index of the input item
		 */
		public AdvancedShapedRecipeBuilder inputItem(final Character symbol, final ItemLike item, final int row, final int column) {
			return inputItem(symbol, Ingredient.of(item), row, column);
		}

		/**
		 * Sets and defines the input item
		 *
		 * @param row The row index of the input item
		 * @param column The column index of the input item
		 */
		public AdvancedShapedRecipeBuilder inputItem(final Character symbol, final Ingredient ingredient, final int row, final int column) {
			inputRow = row;
			inputColumn = column;
			return define(symbol, ingredient);
		}

		/**
		 * Adds a key to the recipe pattern.
		 */
		public AdvancedShapedRecipeBuilder define(final Character symbol, final TagKey<Item> tag) {
			return define(symbol, Ingredient.of(tag));
		}

		/**
		 * Adds a key to the recipe pattern.
		 */
		public AdvancedShapedRecipeBuilder define(final Character symbol, final ItemLike item) {
			return define(symbol, Ingredient.of(item));
		}

		/**
		 * Adds a key to the recipe pattern.
		 */
		public AdvancedShapedRecipeBuilder define(final Character symbol, final Ingredient ingredient) {
			if (key.containsKey(symbol)) throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");

			if (symbol == ' ') throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");

			key.put(symbol, ingredient);
			return this;
		}

		/**
		 * Adds a new row to the pattern for this recipe.
		 */
		public AdvancedShapedRecipeBuilder pattern(final String pattern) {
			if (!rows.isEmpty() && pattern.length() != rows.getFirst().length()) {
				throw new IllegalArgumentException("Pattern must be the same width on every line!");
			}

			rows.add(pattern);
			return this;
		}

		/**
		 * Adds multiple rows to the pattern for this recipe.
		 */
		public AdvancedShapedRecipeBuilder pattern(final String... pattern) {
			Arrays.stream(pattern).forEach(this::pattern);
			return this;
		}

		public AdvancedShapedRecipeBuilder showNotification(final boolean showNotification) {
			this.showNotification = showNotification;
			return this;
		}

		@Override
		protected AdvancedShapedRecipeBuilder self() {
			return this;
		}

		@Override
		protected Recipe<?> createRecipe() {
			final var remainderProvider = remainder.isEmpty() ? null : ItemStackProvider.of(remainder.toArray(ItemStackModifier[]::new));
			return new AdvancedShapedRecipe(ShapedRecipePattern.of(key, rows), showNotification, result, Optional.ofNullable(remainderProvider),
					inputRow, inputColumn);
		}
	}

	public static final class AdvancedShapelessRecipeBuilder extends AdvancedCraftingRecipeBuilder<AdvancedShapelessRecipeBuilder> {

		private final List<Ingredient> ingredients = Lists.newArrayList();
		@Nullable
		private Ingredient primaryIngredient;

		private AdvancedShapelessRecipeBuilder(final String folderName, final ItemStackProvider result) {
			super(folderName, result);
		}

		public AdvancedShapelessRecipeBuilder primaryIngredient(final Ingredient primaryIngredient) {
			this.primaryIngredient = primaryIngredient;
			return this;
		}

		/**
		 * Adds an ingredient that can be any item in the given tag.
		 */
		public AdvancedShapelessRecipeBuilder requires(final TagKey<Item> tag) {
			return requires(Ingredient.of(tag));
		}

		/**
		 * Adds an ingredient of the given item.
		 */
		public AdvancedShapelessRecipeBuilder requires(final ItemLike item) {
			return requires(Ingredient.of(item));
		}

		/**
		 * Adds the given ingredient multiple times.
		 */
		public AdvancedShapelessRecipeBuilder requires(final ItemLike item, final int quantity) {
			for (int i = 0; i < quantity; ++i) requires(item);
			return this;
		}

		/**
		 * Adds an ingredient.
		 */
		public AdvancedShapelessRecipeBuilder requires(final Ingredient ingredient) {
			ingredients.add(ingredient);
			return this;
		}

		/**
		 * Adds an ingredient multiple times.
		 */
		public AdvancedShapelessRecipeBuilder requires(final Ingredient ingredient, final int quantity) {
			for (int i = 0; i < quantity; ++i) requires(ingredient);

			return this;
		}

		@Override
		protected void ensureValid(final ResourceLocation recipeId) {
			super.ensureValid(recipeId);
			if (primaryIngredient == null) {
				throw new IllegalStateException("No primary ingredient set");
			}
		}

		@Override
		protected AdvancedShapelessRecipeBuilder self() {
			return this;
		}

		@Override
		protected Recipe<?> createRecipe() {
			assert primaryIngredient != null : "How has this happened?";
			final var remainderProvider = remainder.isEmpty() ? null : ItemStackProvider.of(remainder.toArray(ItemStackModifier[]::new));
			return new AdvancedShapelessRecipe(NonNullList.copyOf(ingredients), result, Optional.ofNullable(remainderProvider),
					Optional.of(primaryIngredient));
		}
	}
}