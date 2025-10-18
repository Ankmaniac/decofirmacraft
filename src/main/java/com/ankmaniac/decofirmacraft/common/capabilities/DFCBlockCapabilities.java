package com.ankmaniac.decofirmacraft.common.capabilities;

import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class DFCBlockCapabilities {
    public static final BlockCapability<IItemHandler, @Nullable Direction> ITEM = Capabilities.ItemHandler.BLOCK;
    public static final BlockCapability<IFluidHandler, @Nullable Direction> FLUID = Capabilities.FluidHandler.BLOCK;

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(FLUID, DFCBlockEntities.GOBLET.get(), GobletBlockEntity::getTank);
        registerInventory(event, DFCBlockEntities.DFC_SHELVES);
    }

    private static void registerInventory(RegisterCapabilitiesEvent event, Supplier<? extends BlockEntityType<? extends InventoryBlockEntity<?>>> type)
    {
        event.registerBlockEntity(ITEM, type.get(), InventoryBlockEntity::getSidedInventory);
    }
}
