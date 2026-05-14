package com.ankmaniac.decofirmacraft.common.item;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import net.dries007.tfc.util.registry.IdHolder;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.*;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DFCItems {

	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DecoFirmaCraft.MOD_ID);

	public static final ItemId WOODEN_MINECART = register("wooden_minecart", () -> new MinecartItem(AbstractMinecart.Type.CHEST, (new Item.Properties()).stacksTo(1)));


	private static <T extends Mob> ItemId registerSpawnEgg(IdHolder<EntityType<T>> entity)
	{
		return register("spawn_egg/" + entity.getId().getPath(), () -> new DeferredSpawnEggItem(entity.holder(), 0xffffff, 0xffffff, new Properties()));
	}

	private static ItemId register(String name)
	{
		return register(name, () -> new Item(new Properties()));
	}

	private static ItemId register(String name, Properties properties)
	{
		return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new Item(properties)));
	}

	private static ItemId register(String name, Supplier<Item> item)
	{
		return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
	}

	public record ItemId(DeferredHolder<Item, Item> holder) implements RegistryHolder<Item, Item>, ItemLike
	{
		@Override
		public Item asItem()
		{
			return get();
		}
	}}