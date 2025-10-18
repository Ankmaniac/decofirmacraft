package com.ankmaniac.decofirmacraft.common.capabilities;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.block.metal.DFCExtendedMetal;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.ankmaniac.decofirmacraft.common.item.metal.GobletItem;
import net.dries007.tfc.common.component.fluid.FluidContainerHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class DFCItemCapabilities {
    public static final ItemCapability<IFluidHandlerItem, @Nullable Void> FLUID = Capabilities.FluidHandler.ITEM;
    public static final ItemCapability<IItemHandler, @Nullable Void> ITEM = Capabilities.ItemHandler.ITEM;

    public static void register(RegisterCapabilitiesEvent event) {

        final ItemLike[] goblets = Arrays.stream(DFCExtendedMetal.values())
                .filter(m -> DFCExtendedMetal.DFCMetalBlockType.GOBLET.has(m))
                .map(m -> DFCBlocks.DFC_METAL_BLOCKS.get(m).get(DFCExtendedMetal.DFCMetalBlockType.GOBLET).asItem())
                .toArray(ItemLike[]::new);

        event.registerItem(FLUID, DFCItemCapabilities::forGoblet, goblets);
    }

    public static @Nullable FluidContainerHandler forGoblet(ItemStack stack, @Nullable Void context)
    {
        return stack.getItem() instanceof GobletItem ? new FluidContainerHandler(stack, GobletBlockEntity.GobletTank.INFO) : null;
    }
}
