package com.ankmaniac.decofirmacraft.common.container;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import lombok.Setter;
import net.dries007.tfc.common.container.ISlotCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DFCInventoryContainer extends AbstractContainerMenu
{
    public static DFCInventoryContainer create(MenuType<?> type, int windowId, Inventory playerInv)
    {
        return new DFCInventoryContainer(type, windowId).init(playerInv);
    }

    protected int containerSlots; // The number of slots in the container (not including the player inventory)
    protected static final SimpleContainer CONTAINER = new SimpleContainer(1);
    @Nullable
    protected Player player;
    @Nullable protected ISlotCallback callback;

    protected DFCInventoryContainer(@Nullable ISlotCallback callback, MenuType<?> type, int windowId)
    {
        super(type, windowId);
        this.callback = callback;
    }

    protected DFCInventoryContainer(MenuType<?> type, int windowId)
    {
        this(null, type, windowId);
    }

    /**
     * Problem: calling add slots from the container superclass, means that we cannot access subclass parameters, such as an item fluid or tile entity, which are necessary in order to do some things such as setup container slots.
     * Solutions for running this at the right time are very difficult.
     * So, we have an explicit post-constructor-initialization method, which needs to be ran externally, but will always run after final fields have been initialized.
     *
     * @return The current container, casted down as required.
     */
    @SuppressWarnings("unchecked")
    public <C extends DFCInventoryContainer> C init(Inventory playerInventory, int yOffset)
    {
        addContainerSlots();
        containerSlots = slots.size();
        addPlayerInventorySlots(playerInventory, yOffset);
        player = playerInventory.player;
        return (C) this;
    }

    public <C extends DFCInventoryContainer> C init(Inventory playerInventory)
    {
        return init(playerInventory, 0);
    }

    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player)
    {
        // Handles trash slot behavior
        // You are able to move items in and out of it but if you place an item in it and
        // it already has a different item within it itll replace the old item, trashing it
        if (slot == this.containerSlots + 37 && clickType == ClickType.PICKUP && (button == 0 || button == 1))
        {
            System.out.println("TRASH");
            ClickAction clickaction = button == 0 ? ClickAction.PRIMARY : ClickAction.SECONDARY;
            final Slot trashSlot = (Slot) this.slots.get(slot);
            ItemStack trashStack = trashSlot.getItem();
            ItemStack heldStack = this.getCarried();

            if (heldStack.isEmpty()) {
                int moveCount = clickaction == ClickAction.PRIMARY ? trashStack.getCount() : (trashStack.getCount() + 1) / 2;
                Optional<ItemStack> optionalStack = trashSlot.tryRemove(moveCount, Integer.MAX_VALUE, player);
                optionalStack.ifPresent((newStack) ->
                {
                    this.setCarried(newStack);
                    trashSlot.onTake(player, newStack);
                });
            }
            else if (trashSlot.mayPickup(player))
            {
                if (trashStack.isEmpty())
                {
                    int moveCount = clickaction == ClickAction.PRIMARY ? heldStack.getCount() : 1;
                    this.setCarried(trashSlot.safeInsert(heldStack, moveCount));
                }
                else
                {
                    if (ItemStack.isSameItemSameComponents(trashStack, heldStack))
                    {
                        int moveCount = clickaction == ClickAction.PRIMARY ? heldStack.getCount() : 1;
                        this.setCarried(trashSlot.safeInsert(heldStack, moveCount));
                    }
                    else if (heldStack.getCount() <= trashSlot.getMaxStackSize(heldStack))
                    {
                        this.setCarried(ItemStack.EMPTY);
                        trashSlot.setByPlayer(heldStack);
                    }
                }
            }
            trashSlot.setChanged();
        }
        super.clicked(slot, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        final Slot slot = slots.get(index);
        if (slot.hasItem()) // Only move an item when the index clicked has any contents
        {
            final ItemStack stack = slot.getItem(); // The item in the current slot
            final ItemStack original = stack.copy(); // The original amount in the slot
            if (moveStack(stack, index))
            {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == original.getCount())
            {
                return ItemStack.EMPTY;
            }

            // Handle updates
            if (stack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            slot.onTake(player, stack);
            return original;
        }
        return ItemStack.EMPTY;
    }

    /**
     * In {@link AbstractContainerMenu#doClick} there is a call path through which {@link Slot#onTake} is not called. It just directly sets the slot, and the carried in the container.
     * We call the callback's slotless version here, as it's all we can realistically do.
     *
     * @param stack The stack that is set to be carried.
     */
    @Override
    public void setCarried(ItemStack stack)
    {
        if (callback != null)
        {
            callback.onCarried(stack);
        }
        super.setCarried(stack);
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return true;
    }

    /**
     * Handles the actual movement of stacks in {@link #quickMoveStack(Player, int)} with as little boilerplate as possible.
     * The default implementation only moves stacks between the main inventory and the hotbar.
     *
     * @return {@code true} if no movement is possible, or the result of {@code !moveItemStackTo(...) || ...}
     */
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
        {
            case CONTAINER -> true;
            case HOTBAR -> !moveItemStackTo(stack, containerSlots, containerSlots + 27, false);
            case MAIN_INVENTORY -> !moveItemStackTo(stack, containerSlots + 27, containerSlots + 36, false);
            case OFFHAND, TRASH -> !moveItemStackTo(stack, containerSlots, containerSlots + 36, false);
        };
    }

    /**
     * Adds container slots.
     * These are added before the player inventory (and as such, the player inventory will be shifted upwards by the number of slots added here.
     */
    protected void addContainerSlots() {}

    /**
     * Adds the player inventory slots to the container.
     */
    protected final void addPlayerInventorySlots(Inventory playerInv, int yOffset)
    {
        // Main Inventory. Indexes [0, 27)
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOffset));
            }
        }

        // Hotbar. Indexes [27, 36)
        for (int k = 0; k < 9; k++)
        {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142 + yOffset));
        }

        // Offhand. Index 36
        this.addSlot(new Slot(playerInv, 40, 172, 102 + yOffset)
        {
            public void setByPlayer(ItemStack oldItem, ItemStack newItem)
            {
                playerInv.player.onEquipItem(EquipmentSlot.OFFHAND, newItem, oldItem);
                super.setByPlayer(oldItem, newItem);
            }

            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon()
            {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });

        // Trash. Index 37
        addSlot( new Slot(CONTAINER, 0, 172, 142 + yOffset));
    }

    /**
     * Container specific implementation, mimicking {@link AbstractContainerMenu#removed(Player)}'s logic, which handles dead and disconnected players properly. See TerraFirmaCraft#2407
     */
    protected final void giveItemStackToPlayerOrDrop(Player player, ItemStack stack)
    {
        if (player instanceof ServerPlayer serverPlayer)
        {
            if (player.isAlive() && !serverPlayer.hasDisconnected())
            {
                player.getInventory().placeItemBackInInventory(stack);
            }
            else
            {
                player.drop(stack, false);
            }
        }
    }

    public final IndexType typeOf(int index)
    {
        if (index < containerSlots)
        {
            return IndexType.CONTAINER;
        }
        else if (index < containerSlots + 27)
        {
            return IndexType.MAIN_INVENTORY;
        }
        else if (index < containerSlots + 36)
        {
            return IndexType.HOTBAR;
        }
        else if (index < containerSlots + 37)
        {
            return IndexType.OFFHAND;
        }
        return IndexType.TRASH;
    }

    public enum IndexType
    {
        CONTAINER,
        MAIN_INVENTORY,
        HOTBAR,
        OFFHAND,
        TRASH
    }

    public class Scroller
    {
        @Getter
        @Setter
        private int offset = 0;
        @Getter
        private final boolean vertical;
        @Getter
        @Setter
        private int maxScrollPixels;
        @Getter
        private final int windowSize;
        @Getter
        private final int windowOffset;

        public Scroller(boolean vertical, int startingMaxPixels, int windowSize, int windowOffset)
        {
            this(vertical, 0, startingMaxPixels, windowSize, windowOffset);
        }

        public Scroller(boolean vertical, int startingOffset, int startingMaxPixels, int windowSize, int windowOffset)
        {
            this.vertical = vertical;
            this.offset = startingOffset;
            this.maxScrollPixels = startingMaxPixels;
            this.windowSize = windowSize;
            this.windowOffset = windowOffset;
        }

        public int getCutoffSize(int firstPosition, int lastPosition)
        {
            firstPosition -= offset % 18;
            lastPosition -= offset % 18;
            final int firstEdge = this.windowOffset;
            final int lastEdge = this.windowOffset + this.windowSize;

            if (firstPosition < firstEdge)
            {
                return Math.clamp(lastPosition - firstEdge, 0, lastPosition - firstPosition);
            }
            if (lastPosition > lastEdge)
            {
                return Math.clamp(lastEdge - firstPosition, 0, lastPosition - firstPosition);
            }
            return lastPosition - firstPosition;
        }

        /**
        /false is the first edge, as in top/left depending on orientation
        */
        public boolean getCutoffSide(int firstPosition, int lastPosition)
        {
            firstPosition += offset % 18;
            lastPosition += offset % 18;
            final int firstEdge = this.windowOffset;
            final int lastEdge = this.windowOffset + this.windowSize;

            return lastPosition > lastEdge;
        }
    }
    
    public class RemappableSlot extends Slot
    {
        @Getter
        private final Scroller scroller;
        protected final int slot;
        private final SlotWrappingContainer wrapper;

        public RemappableSlot(SlotWrappingContainer wrapper, int slot, int xPosition, int yPosition, Scroller scroller) {
            super(wrapper, slot, xPosition, yPosition);
            this.wrapper = wrapper;
            this.slot = slot;
            this.scroller = scroller;
        }

        public boolean hasItem()
        {
            return !this.wrapper.getItem(slot).isEmpty();
        }

        public boolean mayPlace(ItemStack stack) 
        {
            return this.wrapper.isItemValid(this.slot, stack);
        }

        public ItemStack getItem() 
        {
            return this.wrapper.getItem(this.slot);
        }

        public void set(ItemStack stack) 
        {
            this.wrapper.setItem(this.slot, stack);
            this.setChanged();
        }

        public void initialize(ItemStack stack) 
        {
            this.wrapper.setItem(this.slot, stack);
            this.setChanged();
        }

        public int getMaxStackSize() 
        {
            return this.wrapper.getSlotLimit(this.slot);
        }

        public int getMaxStackSize(ItemStack stack) 
        {
            return Math.min(stack.getMaxStackSize(), this.wrapper.getSlotLimit(this.slot));
        }

        public boolean mayPickup(Player playerIn) 
        {
            return this.wrapper.mayPickup(this.slot);
        }

        public ItemStack remove(int amount) 
        {
            return this.wrapper.extractItem(this.slot, amount);
        }

        @Override
        public boolean isActive()
        {
            return this.wrapper.isActive(this.slot);
        }

        @Override
        public boolean isHighlightable()
        {
            return this.wrapper.isActive(this.slot);
        }

        public int getOffset()
        {
            return this.scroller.getOffset();
        }

        public boolean isVertical()
        {
            return this.scroller.isVertical();
        }
    }

    public class SlotWrappingContainer extends SimpleContainer
    {
        @Getter
        private final SlotMap slotMap;

        SlotWrappingContainer(int size, SlotMap slotMap)
        {
            super(size);
            this.slotMap = slotMap;
        }

        public boolean isItemValid(int slot, ItemStack stack)
        {
            return slotMap.mayPlace(stack, slot);
        }

        public int getSlotLimit(int slot)
        {
            return this.slotMap.getMaxStackSize(slot);
        }

        public boolean mayPickup(int slot)
        {
            return this.slotMap.mayPickup(slot);
        }

        public ItemStack extractItem(int slot,  int amount)
        {
            return this.slotMap.remove(slot, amount);
        }

        public boolean isActive(int slot)
        {
            return this.slotMap.isActive(slot);
        }

        @Override
        public ItemStack getItem(int index)
        {
            if (index >= 0 && index < this.getItems().size())
            {
                return slotMap.getItem(index);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int slot, ItemStack stack)
        {
            this.slotMap.set(stack, slot);
        }
    }
}
