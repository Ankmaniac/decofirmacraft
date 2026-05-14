package com.ankmaniac.decofirmacraft.common.container;

import com.ankmaniac.decofirmacraft.DFCAttachments;
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
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DFCInventoryMenu extends DFCInventoryContainer
{
    public static DFCInventoryMenu create(Inventory playerInv, int windowId, Player player)
    {
        return new DFCInventoryMenu(playerInv, player, windowId).init(playerInv, 0);
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
    private Map<Integer, int[]> containerPositions;
    private int containerOffset = 0;
    private int contentOffset = 0;
    private int openContentSlots = 0;
    private int openContainerSlots = 4;
    private int maxContainerPixels = 70;
    private int maxContentPixels = 70;

    public DFCInventoryMenu(Inventory playerInventory, final Player player, int windowId)
    {
        super(DFCContainerTypes.INVENTORY.get(), windowId);

        this.player = player;
        this.playerInv = playerInventory;
        this.containerPositions = new HashMap<>();
    }

    private void updateContainerSlots()
    {
        int topSlotOffset = Math.floorDiv(this.containerOffset, 18);
        for (int i = CONTAINER_SLOT_START; i < CONTAINER_SLOT_END; i++)
        {
            ToggledRemappableScrollingContainerSlot slot = (ToggledRemappableScrollingContainerSlot) this.getSlot(i);

            slot.setSlot(i - CONTAINER_SLOT_START + topSlotOffset);
            this.slots.set(i, slot);
        }
    }

    private void updateContentSlots()
    {
        System.out.println("UPDATING CONTENT SLOTS");
        int topSlotOffset = Math.floorDiv(this.contentOffset, 18) * 3;
        for (int i = CONTENTS_SLOT_START; i < CONTENTS_SLOT_END; i++)
        {
            ToggledRemappableScrollingSlot slot = (ToggledRemappableScrollingSlot) this.getSlot(i);

            int[] newSlots = this.containerPositions.get(i - CONTENTS_SLOT_START + topSlotOffset);
            if (newSlots != null)
            {
                System.out.println("UPDATING CONTENT SLOTS " + i);
                ItemStack stack = player.getData(DFCAttachments.EXTRA_PLAYER_SLOTS).getStackInSlot(newSlots[0]);

                IItemHandler container = getItemHandler(stack);
                if (container != null)
                {
                    slot.setItemHandler(container, newSlots[1]);
                    slot.setEnabled(true);
                }
                else
                {
                    slot.setItemHandler(null, -1);
                    slot.setEnabled(false);
                }
            }
            else
            {
                slot.setItemHandler(null, -1);
                slot.setEnabled(false);
            }
            this.slots.set(i, slot);
        }
    }

    private void updateContents()
    {
        System.out.println("UPDATING CONTENTS");
        this.openContentSlots = 0;
        for (int i = 0; i < MAX_CONTAINERS; ++i)
        {
            ItemStack stack = player.getData(DFCAttachments.EXTRA_PLAYER_SLOTS).getStackInSlot(i);

            IItemHandler container = getItemHandler(stack);
            if (container != null)
            {
                System.out.println("CONTAINER " + i);
                for (int j = 0; j < container.getSlots(); ++j)
                {
                    int[] pair = {i, j};
                    this.containerPositions.put(openContentSlots + j + 1, pair);
                }
                this.openContentSlots += container.getSlots();
            }
        }
        this.maxContentPixels = Math.max(Math.ceilDiv(this.openContentSlots, 3) * 18, 70);
        updateContentSlots();
    }

    public void scrollContainers(int scrollAmount)
    {
        final int oldOffset = this.containerOffset;
        scrollAmount = scrollAmount + this.containerOffset > 0 ? scrollAmount + this.containerOffset < maxContainerPixels ? scrollAmount : scrollAmount + this.containerOffset - maxContainerPixels : this.containerOffset;
        this.containerOffset += scrollAmount;
        if (Math.floorDiv(this.containerOffset, 18) != Math.floorDiv(oldOffset, 18))
        {
            this.updateContainerSlots();
        }
        if (oldOffset != this.containerOffset)
        {
            for (int i = CONTAINER_SLOT_START; i < CONTAINER_SLOT_END; i++)
            {
                ToggledRemappableScrollingContainerSlot slot = (ToggledRemappableScrollingContainerSlot) this.getSlot(i);

                slot.addToOffset(scrollAmount);

                this.slots.set(i, slot);
            }
        }
    }

    public void scrollContent(int scrollAmount)
    {
        final int oldOffset = this.contentOffset;
        scrollAmount = scrollAmount + this.contentOffset > 0 ? scrollAmount + this.contentOffset < maxContentPixels ? scrollAmount : scrollAmount + this.contentOffset - maxContentPixels : this.contentOffset;
        this.contentOffset += scrollAmount;
        if (Math.floorDiv(this.contentOffset, 18) != Math.floorDiv(oldOffset, 18))
        {
            this.updateContentSlots();
        }
        if (oldOffset != this.contentOffset)
        {
            for (int i = CONTENTS_SLOT_START; i < CONTENTS_SLOT_END; i++)
            {
                ToggledRemappableScrollingSlot slot = (ToggledRemappableScrollingSlot) this.getSlot(i);

                slot.addToOffset(scrollAmount);

                this.slots.set(i, slot);
            }
        }
    }

    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player)
    {
        if ((slot >= CONTAINER_SLOT_START && slot < CONTAINER_SLOT_END) && (button == 0 || button == 1))
        {
            this.updateContents();
        }
        if (slot < CRAFT_SLOT_END)
        {
            System.out.println("CRAFTING SLOT " + slot);
        }
        if (slot >= CONTAINER_SLOT_START && slot < CONTAINER_SLOT_END)
        {
            System.out.println("CONTAINER SLOT " + slot);
        }
        if (slot >= CONTENTS_SLOT_START && slot < CONTENTS_SLOT_END)
        {
            System.out.println("CONTENT SLOT " + slot);
        }
        super.clicked(slot, button, clickType, player);
    }

    @Override
    protected void addContainerSlots()
    {
        // Result Index 0
        this.addSlot(new ResultSlot(playerInv.player, this.craftSlots, this.resultSlots, 0, 170, 36));

        System.out.println("result slot index: " + this.slots.size());

        // Crafting Slots always on Indexes 1, 2, 4, 5
        // Togglable Crafting Slots Indexes 3, 6, 7, 8, 9
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                if (i == 2 || j == 2)
                {
                    this.addSlot(new ToggledCraftingSlot(this.craftSlots, j + i * 2, 96 + j * 18, 18 + i * 18, player));
                }
                else
                {
                    this.addSlot(new Slot(this.craftSlots, j + i * 2, 96 + j * 18, 18 + i * 18));
                }
            }
        }
        System.out.println("crafting slot index: " + this.slots.size());

        // Scrolling Container Slots Indexes [10, 15)
        for(int i = 0; i < 5; ++i)
        {
            this.addSlot(new ToggledRemappableScrollingContainerSlot(player.getData(DFCAttachments.EXTRA_PLAYER_SLOTS), i, 8, 8 + i * 18, true, true));
        }

        System.out.println("container slot index: " + this.slots.size());

        // Scrolling Container Slots Indexes [15, 30)
        for(int i = 0; i < 5; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                this.addSlot(new ToggledRemappableScrollingSlot(null, j + i * 3, 33 + j * 18, 8 + i * 18, true, false));
            }
        }

        System.out.println("content slot index: " + this.slots.size());

        this.updateContents();
        this.updateContainerSlots();
        this.updateContentSlots();
    }

    @Nullable
    static IItemHandler getItemHandler(ItemStack stack)
    {
        return stack.getCapability(ItemCapabilities.ITEM);
    }

    public class ToggledCraftingSlot extends Slot
    {
        @Setter
        private boolean canUse = false;
        private final Player player;

        public ToggledCraftingSlot(Container container, int slot, int x, int y, Player player)
        {
            super(container, slot, x, y);

            //TODO - add events that can detect when a crafting table is crafted and
            // updates this instantly
            this.player = player;
            canUse = player.getData(DFCAttachments.PLAYER_INFO).isFullCraftingEnabled();
        }

        public boolean canUse()
        {
            return this.player.getData(DFCAttachments.PLAYER_INFO).isFullCraftingEnabled();
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return canUse;
        }

        @Override
        public boolean mayPickup(Player player)
        {
            return canUse;
        }
    }
}
