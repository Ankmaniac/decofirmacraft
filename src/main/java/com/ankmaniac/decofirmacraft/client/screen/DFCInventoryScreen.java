package com.ankmaniac.decofirmacraft.client.screen;

import com.ankmaniac.decofirmacraft.common.container.DFCInventoryMenu;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;
import net.dries007.tfc.common.container.Container;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DFCInventoryScreen extends DFCContainerScreen<DFCInventoryMenu>
{
    public static final ResourceLocation BACKGROUND = DFCHelpers.identifier("textures/gui/player_inventory.png");

    public DFCInventoryScreen(DFCInventoryMenu container, Inventory playerInv, Component name)
    {
        super(container, playerInv, name, BACKGROUND);
        this.imageWidth = 214;
        this.imageHeight = 166;
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    protected void drawDefaultBackground(GuiGraphics graphics)
    {
        graphics.blit(texture, leftPos, topPos, 0, 0, 0, imageWidth, imageHeight, 214, 166);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}