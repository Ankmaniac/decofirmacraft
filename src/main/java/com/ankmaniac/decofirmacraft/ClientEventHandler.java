package com.ankmaniac.decofirmacraft;

import com.ankmaniac.decofirmacraft.client.model.DynamicTextureModel;
import com.ankmaniac.decofirmacraft.client.render.blockentity.CandleholderBlockEntityRenderer;
import com.ankmaniac.decofirmacraft.client.render.blockentity.GobletBlockEntityRenderer;
import com.ankmaniac.decofirmacraft.client.render.blockentity.DFCShelfBlockEntityRenderer;
import com.ankmaniac.decofirmacraft.client.screen.DFCInventoryScreen;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.container.DFCContainerTypes;
import net.dries007.tfc.client.screen.button.PlayerInventoryTabButton;
import net.dries007.tfc.compat.patchouli.PatchouliIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ClientEventHandler
{
    public static void init(ModContainer mod, IEventBus bus)
    {
        bus.addListener(ClientEventHandler::registerEntityRenderers);
        bus.addListener(ClientEventHandler::registerModelLoaders);
        bus.addListener(ClientEventHandler::registerMenuScreens);

    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(DFCBlockEntities.GOBLET.get(), ctw -> new GobletBlockEntityRenderer());
        event.registerBlockEntityRenderer(DFCBlockEntities.DFC_SHELVES.get(), ctw -> new DFCShelfBlockEntityRenderer());
        event.registerBlockEntityRenderer(DFCBlockEntities.CANDLEHOLDER.get(), ctw -> new CandleholderBlockEntityRenderer());
    }

    private static void registerModelLoaders(final ModelEvent.RegisterGeometryLoaders event)
    {
        event.register(DynamicTextureModel.LOADER_ID, DynamicTextureModel.LOADER);
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(DFCContainerTypes.INVENTORY.get(), DFCInventoryScreen::new);
    }
}