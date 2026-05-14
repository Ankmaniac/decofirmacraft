package com.ankmaniac.decofirmacraft.common.container;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.Container;
import net.dries007.tfc.common.container.ItemStackContainer;
import net.dries007.tfc.common.container.TFCContainerTypes;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

public final class DFCContainerTypes
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final DFCContainerTypes.Id<DFCInventoryMenu> INVENTORY = register("inventory", (windowId, inv, data) -> DFCInventoryMenu.create(inv, windowId, inv.player));



    private static <T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> DFCContainerTypes.Id<C> registerBlock(String name, Supplier<BlockEntityType<T>> type, BlockEntityContainer.Factory<T, C> factory)
    {
        return new DFCContainerTypes.Id<>(RegistrationHelpers.registerBlockEntityContainer(CONTAINERS, name, type, factory));
    }

    private static <C extends ItemStackContainer> DFCContainerTypes.Id<C> registerItem(String name, ItemStackContainer.Factory<C> factory)
    {
        return new DFCContainerTypes.Id<>(RegistrationHelpers.registerItemStackContainer(CONTAINERS, name, factory));
    }

    private static <C extends AbstractContainerMenu> DFCContainerTypes.Id<C> register(String name, IContainerFactory<C> factory)
    {
        return new DFCContainerTypes.Id<>(RegistrationHelpers.registerContainer(CONTAINERS, name, factory));
    }

    public record Id<T extends AbstractContainerMenu>(DeferredHolder<MenuType<?>, MenuType<T>> holder)
            implements RegistryHolder<MenuType<?>, MenuType<T>> {}
}
