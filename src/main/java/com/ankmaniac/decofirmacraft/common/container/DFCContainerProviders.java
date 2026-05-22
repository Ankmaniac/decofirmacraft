package com.ankmaniac.decofirmacraft.common.container;

import net.dries007.tfc.common.container.Container;
import net.dries007.tfc.common.container.TFCContainerTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;

public class DFCContainerProviders
{
    public static final MenuProvider INVENTORY = new SimpleMenuProvider((windowId, inv, player) -> DFCInventoryMenu.create(player.getInventory(), windowId, player), Component.translatable("dfc.screen.crafting"));
}
