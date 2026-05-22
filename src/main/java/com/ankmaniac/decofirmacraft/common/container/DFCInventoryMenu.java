package com.ankmaniac.decofirmacraft.common.container;

import com.ankmaniac.decofirmacraft.DFCAttachments;
import lombok.Getter;
import lombok.Setter;
import net.dries007.tfc.common.capabilities.ItemCapabilities;
import net.dries007.tfc.common.component.item.ItemContainer;
import net.dries007.tfc.common.component.mold.Vessel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DFCInventoryMenu extends DFCInventoryContainer implements Scrollable
{
    public static DFCInventoryMenu create(Inventory playerInv, int windowId, Player player)
    {
        return new DFCInventoryMenu(playerInv, player, windowId).init(playerInv);
    }

    public static final int RESULT_SLOT = 0;
    public static final int CRAFT_SLOT_START = 1;
    public static final int CRAFT_SLOT_COUNT = 9;
    public static final int CRAFT_SLOT_END = 10;
    public static final int CONTAINER_SLOT_START = 10;
    public static final int CONTAINER_SLOT_COUNT = 5;
    public static final int CONTAINER_SLOT_END = 15;
    public static final int MAX_CONTAINERS = 8;
    public static final int CONTENTS_SLOT_START = 15;
    public static final int CONTENTS_SLOT_COUNT = 15;
    public static final int CONTENTS_SLOT_END = 30;

    private final Player player;
    private final Inventory playerInv;
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final SlotWrappingContainer containers;
    private final SlotWrappingContainer contents;
    @Getter
    private final Scroller containerScroller;
    @Getter
    private final Scroller contentsScroller;
    private int containerOffset = 0;
    private int contentOffset = 0;
    private int maxContainerPixels = 70;
    private int maxContentPixels = 70;
    private final SlotMap containerMap;
    private final SlotMap contentsMap;

    public DFCInventoryMenu(Inventory playerInventory, final Player player, int windowId)
    {
        super(DFCContainerTypes.INVENTORY.get(), windowId);

        this.player = player;
        this.playerInv = playerInventory;
        this.containerScroller = new Scroller(true, maxContainerPixels, 72, 7);
        this.contentsScroller = new Scroller(true, maxContentPixels, 72, 7);
        this.containerMap = new SlotMap(1, 5, this.player.getData(DFCAttachments.EXTRA_PLAYER_SLOTS), this.containerScroller);
        this.contentsMap = new SlotMap(3, 5, this.player.getData(DFCAttachments.EXTRA_PLAYER_SLOTS), this.contentsScroller);
        this.containers = new SlotWrappingContainer(5, this.containerMap);
        this.contents = new SlotWrappingContainer(15, this.contentsMap);
        this.containerMap.setContainer(this.containers);
        this.contentsMap.setContainer(this.contents);
    }

    public void scroll(int id, int offset)
    {
        if (id == 0)
        {
            final int oldOffset = this.containerOffset;
            final int maxScrollAmount = this.containerScroller.getMaxScrollPixels();
            this.containerOffset = Math.clamp(this.containerOffset + offset, 0, maxScrollAmount);
            if (oldOffset != this.containerOffset)
            {
                this.containerScroller.setOffset(containerOffset);
            }
            if (Math.floorDiv(this.containerOffset, 18) != Math.floorDiv(oldOffset, 18))
            {
                this.containerMap.updatePositions();
                this.broadcastChanges();
            }
        }
        else if (id == 1)
        {
            final int oldOffset = this.contentOffset;
            final int maxScrollAmount = this.contentsScroller.getMaxScrollPixels();
            this.contentOffset = Math.clamp(this.contentOffset + offset, 0, maxScrollAmount);
            if (oldOffset != this.contentOffset)
            {
                this.contentsScroller.setOffset(contentOffset);
            }
            if (Math.floorDiv(this.contentOffset, 18) != Math.floorDiv(oldOffset, 18))
            {
                this.contentsMap.updatePositions();
                this.broadcastChanges();
            }
        }
    }

    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player)
    {
        super.clicked(slot, button, clickType, player);
        if ((slot >= CONTAINER_SLOT_START && slot < CONTAINER_SLOT_END))
        {
            this.containerMap.updateContainers();
            this.contentsMap.updateContents();
            this.broadcastChanges();
        }
    }

    @Override
    protected void addContainerSlots()
    {
        // Result Index 0
        this.addSlot(new ResultSlot(this.player, this.craftSlots, this.resultSlots, 0, 170, 36));

        // Crafting Slots always on Indexes 1, 2, 4, 5
        // Togglable Crafting Slots Indexes 3, 6, 7, 8, 9
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                if (i == 2 || j == 2)
                {
                    this.addSlot(new ToggledCraftingSlot(this.craftSlots, j + i * 3, 96 + j * 18, 18 + i * 18, player));
                }
                else
                {
                    this.addSlot(new Slot(this.craftSlots, j + i * 3, 96 + j * 18, 18 + i * 18));
                }
            }
        }

        // Scrolling Container Slots Indexes [10, 15)
        for(int i = 0; i < 5; ++i)
        {
            this.addSlot(new RemappableSlot(this.containers, i, 8, 8 + i * 18, this.containerScroller));
        }

        // Scrolling Container Slots Indexes [15, 30)
        for(int i = 0; i < 5; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                this.addSlot(new RemappableSlot(this.contents, j + i * 3, 33 + j * 18, 8 + i * 18, this.contentsScroller));
            }
        }

        this.containerMap.updateContainers();
        this.contentsMap.updateContents();
        this.containerMap.updatePositions();
        this.contentsMap.updatePositions();
        this.broadcastChanges();
    }

    @Nullable
    static IItemHandler getItemHandler(ItemStack stack)
    {
        return stack.getCapability(ItemCapabilities.ITEM);
    }

    public class ToggledCraftingSlot extends Slot
    {
        private final Player player;

        public ToggledCraftingSlot(Container container, int slot, int x, int y, Player player)
        {
            super(container, slot, x, y);

            //TODO - add events that can detect when a crafting table is crafted and
            // updates this instantly
            this.player = player;
        }

        public boolean canUse()
        {
            return this.player.getData(DFCAttachments.PLAYER_INFO).isFullCraftingEnabled();
        }

        @Override
        public boolean isActive()
        {
            return this.canUse();
        }

        @Override
        public boolean isHighlightable()
        {
            return this.canUse();
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return canUse();
        }

        @Override
        public boolean mayPickup(Player player)
        {
            return canUse();
        }
    }
}
