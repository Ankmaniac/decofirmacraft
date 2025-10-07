package com.ankmaniac.decofirmacraft.common.capabilities;

import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BlockCapabilities
{
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, DFCBlockEntities.GOBLET.get(), GobletBlockEntity::getTank);
    }
}
