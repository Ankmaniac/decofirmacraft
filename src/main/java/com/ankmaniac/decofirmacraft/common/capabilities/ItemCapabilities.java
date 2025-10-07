package com.ankmaniac.decofirmacraft.common.capabilities;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.block.metal.DFCExtendedMetal;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.ankmaniac.decofirmacraft.common.item.metal.GobletItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.component.fluid.FluidContainerHandler;

public class ItemCapabilities
{
    public static void register(RegisterCapabilitiesEvent event)
    {
        final ItemLike[] goblets = DFCBlocks.DFC_DECORATIVE_METAL_BLOCKS.values()
            .stream()
            .map(m -> m.get(DFCExtendedMetal.DFCMetalBlockType.GOBLET).asItem())
            .toArray(ItemLike[]::new);

        event.registerItem(Capabilities.FluidHandler.ITEM, ItemCapabilities::forGoblet, goblets);
    }

    public static @Nullable FluidContainerHandler forGoblet(ItemStack stack, @Nullable Void context)
    {
        return stack.getItem() instanceof GobletItem ? new FluidContainerHandler(stack, GobletBlockEntity.GobletTank.INFO) : null;
    }
}
