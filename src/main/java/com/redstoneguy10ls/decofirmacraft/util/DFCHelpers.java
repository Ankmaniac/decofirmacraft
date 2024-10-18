package com.redstoneguy10ls.decofirmacraft.util;

import com.google.gson.JsonObject;
import com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft;
import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

public class DFCHelpers {
    public static ResourceLocation identifier(String id)
    {
        return new ResourceLocation(DecoFirmaCraft.MOD_ID, id);
    }
    public static boolean isHoldingFluidContainerInOffhand(Player player) {
        ItemStack offHandItem = player.getOffhandItem();
        // Check if the offhand item has the FLUID_HANDLER_ITEM capability
        LazyOptional<IFluidHandler> fluidHandlerCap = offHandItem.getCapability(ForgeCapabilities.FLUID_HANDLER);
        return fluidHandlerCap.isPresent();
    }
    public static FluidStack getFluidStack(JsonObject json) {
        String fluidName = json.get("fluid").getAsString();
        int amount = json.has("amount") ? json.get("amount").getAsInt() : 1000;

        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));

        if (fluid == null) {
            fluid = Fluids.EMPTY;
        }

        return new FluidStack(fluid, amount);
    }
    public static FluidStack getFluidFromItem(ItemStack stack) {
        // Check if the item is a fluid handler (e.g., a bucket or fluid container)
        if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
            LazyOptional<IFluidHandlerItem> fluidHandlerCap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);

            if (fluidHandlerCap.isPresent()) {
                IFluidHandlerItem fluidHandler = fluidHandlerCap.orElseThrow(IllegalStateException::new);
                return fluidHandler.getFluidInTank(0); // Get fluid from the first tank
            }
        }
        return FluidStack.EMPTY; // Return empty if no fluid is present
    }
    public static Item getLeadedStainedGlass(DyeColor color)
    {
        return switch (color)
        {
            case WHITE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.WHITE).get().asItem();
            case ORANGE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.ORANGE).get().asItem();
            case MAGENTA -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.MAGENTA).get().asItem();
            case LIGHT_BLUE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.LIGHT_BLUE).get().asItem();
            case YELLOW -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.YELLOW).get().asItem();
            case LIME -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.LIME).get().asItem();
            case PINK -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.PINK).get().asItem();
            case GRAY -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.GRAY).get().asItem();
            case LIGHT_GRAY -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.LIGHT_GRAY).get().asItem();
            case CYAN -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.CYAN).get().asItem();
            case PURPLE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.PURPLE).get().asItem();
            case BLUE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.BLUE).get().asItem();
            case BROWN -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.BROWN).get().asItem();
            case GREEN -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.GREEN).get().asItem();
            case RED -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.RED).get().asItem();
            default -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.BLACK).get().asItem();
        };
    }
}
