package com.ankmaniac.decofirmacraft.client.screen;

import com.ankmaniac.decofirmacraft.common.container.DFCInventoryContainer;
import com.ankmaniac.decofirmacraft.common.container.DFCInventoryContainer.RemappableSlot;
import com.ankmaniac.decofirmacraft.common.container.DFCInventoryContainer.Scroller;
import com.ankmaniac.decofirmacraft.common.container.DFCInventoryMenu;
import com.ankmaniac.decofirmacraft.mixin.client.accessors.AbstractContainerScreenAccessor;
import com.ankmaniac.decofirmacraft.network.ScrollMenuPacket;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.container.Container;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

import static com.ankmaniac.decofirmacraft.common.container.DFCInventoryMenu.*;

public class DFCInventoryScreen extends DFCContainerScreen<DFCInventoryMenu>
{
    public static final ResourceLocation BACKGROUND = DFCHelpers.identifier("textures/gui/player_inventory.png");
    private static final ResourceLocation SLOT_TEXTURE = DFCHelpers.identifier("textures/gui/slot.png");

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
        drawSlots(graphics);
    }

    @Override
    protected void renderSlotContents(GuiGraphics graphics, ItemStack itemstack, Slot slot, @Nullable String countString) 
    {
        //definitely way overdone. could probably just skip doing one for each scissor
        //and just have disable scissor at the end regardless if i make one or not
        //but im not sure if thatd break anything
        if (slot instanceof RemappableSlot remappableSlot)
        {
            int i = slot.x;
            int j = slot.y;
            if (remappableSlot.isVertical())
            {
                j -= remappableSlot.getOffset() % 18;
            }
            else
            {
                i -= remappableSlot.getOffset() % 18;
            }
            int j1 = i + j * this.imageWidth;

            if (slot.index >= CONTAINER_SLOT_START && slot.index < CONTAINER_SLOT_END)
            {
                graphics.enableScissor(leftPos + 7, topPos + 8, leftPos + 24, topPos + 78);
                if (slot.isFake())
                {
                    graphics.renderFakeItem(itemstack, i, j, j1);
                }
                else
                {
                    graphics.renderItem(itemstack, i, j, j1);
                }

                graphics.renderItemDecorations(this.font, itemstack, i, j, countString);
                graphics.disableScissor();
            }
            else if (slot.index >= CONTENTS_SLOT_START && slot.index < CONTENTS_SLOT_END)
            {
                graphics.enableScissor(leftPos + 32, topPos + 8, leftPos + 85, topPos + 78);
                if (slot.isFake())
                {
                    graphics.renderFakeItem(itemstack, i, j, j1);
                }
                else
                {
                    graphics.renderItem(itemstack, i, j, j1);
                }

                graphics.renderItemDecorations(this.font, itemstack, i, j, countString);
                graphics.disableScissor();
            }
            else
            {
                if (slot.isFake())
                {
                    graphics.renderFakeItem(itemstack, i, j, j1);
                }
                else
                {
                    graphics.renderItem(itemstack, i, j, j1);
                }

                graphics.renderItemDecorations(this.font, itemstack, i, j, countString);
            }
        }
        else
        {
            super.renderSlotContents(graphics, itemstack, slot, countString);
        }
    }

    @Override
    protected void renderSlotHighlight(GuiGraphics graphics, Slot slot, int mouseX, int mouseY, float partialTick)
    {
        if (slot.isHighlightable())
        {
            if (slot instanceof RemappableSlot remappableSlot)
            {
                int i = slot.x;
                int j = slot.y;
                if (remappableSlot.isVertical())
                {
                    j -= remappableSlot.getOffset() % 18;
                }
                else
                {
                    i -= remappableSlot.getOffset() % 18;
                }

                if (slot.index >= CONTAINER_SLOT_START && slot.index < CONTAINER_SLOT_END)
                {
                    graphics.enableScissor(leftPos + 7, topPos + 8, leftPos + 24, topPos + 78);

                    renderSlotHighlight(graphics, i, j, 0, this.getSlotColor(slot.index));

                    graphics.disableScissor();
                }
                else if (slot.index >= CONTENTS_SLOT_START && slot.index < CONTENTS_SLOT_END)
                {
                    graphics.enableScissor(leftPos + 32, topPos + 8, leftPos + 85, topPos + 78);

                    renderSlotHighlight(graphics, i, j, 0, this.getSlotColor(slot.index));

                    graphics.disableScissor();
                }
                else
                {
                    renderSlotHighlight(graphics, i, j, 0, this.getSlotColor(slot.index));
                }
                return;
            }
            renderSlotHighlight(graphics, slot.x, slot.y, 0, this.getSlotColor(slot.index));
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        //TODO - make scroll amount a setting in controls
        if (withinBounds(mouseX, mouseY, leftPos + 7, topPos + 8, leftPos + 24, topPos + 78))
        {
            PacketDistributor.sendToServer(new ScrollMenuPacket(0, scrollY > 0 ? -1 : 1));
            menu.scroll(0, scrollY > 0 ? -6 : 6);
            return true;
        }
        if (withinBounds(mouseX, mouseY, leftPos + 32, topPos + 8, leftPos + 85, topPos + 78))
        {
            PacketDistributor.sendToServer(new ScrollMenuPacket(1, scrollY > 0 ? -1 : 1));
            menu.scroll(1, scrollY > 0 ? -6 : 6);
            return true;
        }
        return false;
    }

    private boolean withinBounds(double mouseX, double mouseY, int minX, int minY, int maxX, int maxY)
    {
        return mouseX > minX && mouseX < maxX && mouseY > minY && mouseY < maxY;
    }

    private void drawSlots(final GuiGraphics graphics)
    {
        for (int i = CRAFT_SLOT_START; i < CRAFT_SLOT_END; i++)
        {
            final Slot slot = menu.slots.get(i);
            if (slot.isActive())
            {
                final int x = leftPos + slot.x - 1;
                final int y = topPos + slot.y - 1;
                graphics.blit(SLOT_TEXTURE, x, y, 0, 0, 18, 18, 18, 18);
            }
        }
        graphics.enableScissor(leftPos + 7, topPos + 8, leftPos + 24, topPos + 78);
        for (int i = CONTAINER_SLOT_START; i < CONTAINER_SLOT_END; i++)
        {
            final Slot slot = menu.slots.get(i);
            if (slot.isActive())
            {
                final Scroller scroller = menu.getContainerScroller();
                final int x = leftPos + slot.x - 1;
                final int y = topPos + slot.y - 1 - (scroller.getOffset() % 18);
                graphics.blit(SLOT_TEXTURE, x, y, 0, 0, 18, 18, 18, 18);
            }
        }
        graphics.disableScissor();
        graphics.enableScissor(leftPos + 32, topPos + 8, leftPos + 85, topPos + 78);
        for (int i = CONTENTS_SLOT_START; i < CONTENTS_SLOT_END; i++)
        {
            final Slot slot = menu.slots.get(i);
            if (slot.isActive())
            {
                final Scroller scroller = menu.getContentsScroller();
                final int x = leftPos + slot.x - 1;
                final int y = topPos + slot.y - 1 - (scroller.getOffset() % 18);
                graphics.blit(SLOT_TEXTURE, x, y, 0, 0, 18, 18, 18, 18);
            }
        }
        graphics.disableScissor();
    }
}