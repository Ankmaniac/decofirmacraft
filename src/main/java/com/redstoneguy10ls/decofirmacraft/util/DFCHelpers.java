package com.redstoneguy10ls.decofirmacraft.util;

import com.google.gson.JsonObject;
import com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
}
