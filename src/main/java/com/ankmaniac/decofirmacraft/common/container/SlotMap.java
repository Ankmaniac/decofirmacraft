package com.ankmaniac.decofirmacraft.common.container;

import com.ankmaniac.decofirmacraft.common.container.DFCInventoryContainer.Scroller;
import com.ankmaniac.decofirmacraft.common.container.DFCInventoryContainer.SlotWrappingContainer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Setter;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import static com.ankmaniac.decofirmacraft.common.container.DFCInventoryMenu.*;

/**
/ This is meant to facilitate mapping scrolling slots to itemhandlers
*/
public class SlotMap
{
    private final int width;
    private final int height;
    private final Int2ObjectMap<ItemHandlerPair> totalPairs;
    private final Int2ObjectMap<ItemHandlerPair> positions;
    private final ItemStackHandler inventory;
    private final Scroller scroller;
    @Setter
    private SlotWrappingContainer container;
    private int maxContainers = 8;

    public SlotMap(int width, int height, ItemStackHandler inventory, Scroller scrollable)
    {
        this.width = width;
        this.height = height;
        this.totalPairs = new Int2ObjectOpenHashMap<>();
        this.positions = new Int2ObjectOpenHashMap<>();
        this.inventory = inventory;
        this.scroller = scrollable;
    }

    //pick one for the object AND STICK TO IT!!!
    //i will probably separate these later....
    public void updateContainers()
    {
        this.totalPairs.clear();

        for (int i = 0; i < maxContainers; ++i)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            // TODO - add support for components that add more container slots when used

            ItemHandlerPair pair = new ItemHandlerPair(-1, i);
            this.totalPairs.put(i, pair);
        }

        this.scroller.setMaxScrollPixels(Math.max(Math.ceilDiv(maxContainers, width) * 18 - this.scroller.getWindowSize(), 0));
        if (this.scroller.getOffset() > this.scroller.getMaxScrollPixels())
        {
            this.scroller.setOffset(this.scroller.getMaxScrollPixels());
        }
        updatePositions();
    }

    public void updateContents()
    {
        this.totalPairs.clear();

        int openContentSlots = 0;
        for (int i = 0; i < maxContainers; ++i)
        {
            ItemStack stack = inventory.getStackInSlot(i);

            IItemHandler container = getItemHandler(stack);
            if (container != null)
            {
                for (int j = 0; j < container.getSlots(); ++j)
                {
                    ItemHandlerPair pair = new ItemHandlerPair(i, j);
                    this.totalPairs.put(openContentSlots, pair);
                    openContentSlots++;
                }
            }
        }
        this.scroller.setMaxScrollPixels(Math.max(Math.ceilDiv(openContentSlots, width) * 18 - this.scroller.getWindowSize(), 0));
        if (this.scroller.getOffset() > this.scroller.getMaxScrollPixels())
        {
            this.scroller.setOffset(this.scroller.getMaxScrollPixels());
        }
        updatePositions();
    }

    public void updatePositions()
    {
        final int offset = this.scroller.getOffset();
        final int slotOffset = Math.floorDiv(offset, 18) * width;

        this.positions.clear();

        for (int i = 0; i < this.width * this.height; ++i)
        {
            final ItemHandlerPair pair = this.totalPairs.get(slotOffset + i);

            this.positions.put(i, pair);
        }
        this.container.setChanged();
    }

    public ItemStack getItem(int slot)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        if (pair != null)
        {
            int inventorySlot = pair.inventorySlot;
            int containerSlot = pair.containerSlot;
            if (inventorySlot == -1)
            {
                return this.inventory.getStackInSlot(containerSlot);
            }
            else
            {
                IItemHandler container = getItemHandler(this.inventory.getStackInSlot(inventorySlot));

                if (container != null)
                {
                    return container.getStackInSlot(containerSlot);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean mayPlace(ItemStack stack, int slot)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        if (pair != null)
        {
            int inventorySlot = pair.inventorySlot;
            int containerSlot = pair.containerSlot;
            if (inventorySlot == -1)
            {
                return this.inventory.isItemValid(containerSlot, stack);
            }
            else
            {
                IItemHandler container = getItemHandler(this.inventory.getStackInSlot(inventorySlot));

                if (container != null)
                {
                    return container.isItemValid(containerSlot, stack);
                }
            }
        }
        return false;
    }

    public boolean mayPickup(int slot)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        return pair != null;
    }

    public void set(ItemStack stack, int slot)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        if (pair != null)
        {
            int inventorySlot = pair.inventorySlot;
            int containerSlot = pair.containerSlot;
            if (inventorySlot == -1)
            {
                this.inventory.setStackInSlot(containerSlot, stack);
            }
            else
            {
                //i know this is dogshit code sorry ill fix it later.......
                IItemHandlerModifiable container = (IItemHandlerModifiable) getItemHandler(this.inventory.getStackInSlot(inventorySlot));

                if (container != null)
                {
                    container.setStackInSlot(containerSlot, stack);
                }
            }
        }
    }

    public int getMaxStackSize(int slot)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        if (pair != null)
        {
            int inventorySlot = pair.inventorySlot;
            int containerSlot = pair.containerSlot;
            if (inventorySlot == -1)
            {
                return this.inventory.getSlotLimit(containerSlot);
            }
            else
            {
                IItemHandler container = getItemHandler(this.inventory.getStackInSlot(inventorySlot));

                if (container != null)
                {
                    return container.getSlotLimit(containerSlot);
                }
            }
        }
        return 0;
    }

    public ItemStack remove(int slot, int amount)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        if (pair != null)
        {
            int inventorySlot = pair.inventorySlot;
            int containerSlot = pair.containerSlot;
            if (inventorySlot == -1)
            {
                return this.inventory.extractItem(containerSlot, amount, false);
            }
            else
            {
                IItemHandler container = getItemHandler(this.inventory.getStackInSlot(inventorySlot));

                if (container != null)
                {
                    return container.extractItem(containerSlot, amount, false);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public record ItemHandlerPair(int inventorySlot, int containerSlot)
    {

    }

    public boolean isActive(int slot)
    {
        ItemHandlerPair pair = this.positions.get(slot);
        return pair != null;
    }
}
