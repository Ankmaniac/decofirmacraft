package com.ankmaniac.decofirmacraft.providers;

import com.ankmaniac.decofirmacraft.common.item.DFCItems;
import com.ankmaniac.decofirmacraft.tfc.DataManagerProvider;
import net.dries007.tfc.common.component.size.*;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class BuiltInItemSizes extends DataManagerProvider<ItemSizeDefinition> {

	public BuiltInItemSizes(final PackOutput output, final CompletableFuture<Provider> lookup) {
		super(ItemSizeManager.MANAGER, output, lookup);
	}

	@Override
	protected void addData(final Provider provider) {
//		add("example_size_def", DFCItems.EXAMPLE_ITEM, Size.HUGE, Weight.VERY_HEAVY);
	}

	private void add(final String name, final TagKey<Item> item, final Size size, final Weight weight) {
		add(name, new ItemSizeDefinition(Ingredient.of(item), size, weight));
	}

	private void add(final String name, final ItemLike item, final Size size, final Weight weight) {
		add(name, new ItemSizeDefinition(Ingredient.of(item), size, weight));
	}

	private void add(final String name, final Ingredient item, final Size size, final Weight weight) {
		add(name, new ItemSizeDefinition(item, size, weight));
	}
}