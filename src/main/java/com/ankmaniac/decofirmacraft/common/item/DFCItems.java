package com.ankmaniac.decofirmacraft.common.item;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import net.neoforged.neoforge.registries.*;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Function;

public final class DFCItems {

	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DecoFirmaCraft.MOD_ID);

	private static <I extends Item> DeferredItem<I> register(final String name, final Function<Properties, ? extends I> factory,
			final Properties properties) {
		return ITEMS.registerItem(name, factory, properties);
	}

	private static <I extends Item> DeferredItem<I> register(final String name, final Function<Properties, ? extends I> factory) {
		return ITEMS.registerItem(name, factory);
	}

	private static DeferredItem<Item> registerSimple(final String name, Properties properties) {
		return ITEMS.registerSimpleItem(name, properties);
	}

	private static DeferredItem<Item> registerSimple(final String name) {
		return ITEMS.registerSimpleItem(name);
	}
}