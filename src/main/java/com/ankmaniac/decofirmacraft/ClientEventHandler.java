package com.ankmaniac.decofirmacraft;

import com.ankmaniac.decofirmacraft.client.render.blockentity.GobletBlockEntityRenderer;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventHandler
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::registerEntityRenderers);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(DFCBlockEntities.GOBLET.get(), ctw -> new GobletBlockEntityRenderer());
    }
}
