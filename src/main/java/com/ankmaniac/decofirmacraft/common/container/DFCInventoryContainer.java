package com.ankmaniac.decofirmacraft.common.container;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import lombok.Setter;
import net.dries007.tfc.common.container.ISlotCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckForNull;
import javax.swing.*;
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

    public class ScrollingSlot extends Slot implements Scrollable
    {
        private final boolean vertical;
        private int offset = 0;

        public ScrollingSlot(Container container, int slot, int x, int y, boolean vertical)
        {
            this(container, slot, x, y, vertical, 0);
        }

        public ScrollingSlot(Container container, int slot, int x, int y, boolean vertical, int startingOffset)
        {
            super(container, slot, x, y);

            this.vertical = vertical;
            this.setOffset(startingOffset);
        }

        @Override
        public int getOffset()
        {
            return offset;
        }

        @Override
        public void setOffset(int offset)
        {
            this.offset = offset < 16 ? offset > 0 ? Mth.clamp(offset, 0, 16) : offset + 16 : offset - 16;
        }

        @Override
        public void addToOffset(int offset)
        {
            this.setOffset(this.getOffset() + offset);
        }

        @Override
        public boolean isVertical()
        {
            return vertical;
        }
    }

    public class ScrollingSlotItemHandler extends SlotItemHandler implements Scrollable
    {
        private final boolean vertical;
        private int offset = 0;

        public ScrollingSlotItemHandler(IItemHandler itemHandler, int slot, int x, int y, boolean vertical)
        {
            this(itemHandler, slot, x, y, vertical, 0);
        }

        public ScrollingSlotItemHandler(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset)
        {
            super(itemHandler, slot, x, y);

            this.vertical = vertical;
            this.setOffset(startingOffset);
        }

        @Override
        public int getOffset()
        {
            return offset;
        }

        @Override
        public void setOffset(int offset)
        {
            this.offset = offset < 16 ? offset > 0 ? Mth.clamp(offset, 0, 16) : offset + 16 : offset - 16;
        }

        @Override
        public void addToOffset(int offset)
        {
            this.setOffset(this.getOffset() + offset);
        }

        @Override
        public boolean isVertical()
        {
            return vertical;
        }
    }

    public class ToggledScrollingSlot extends ScrollingSlot implements Togglable
    {
        private boolean enabled;

        public ToggledScrollingSlot(Container container, int slot, int x, int y, boolean vertical)
        {
            super(container, slot, x, y, vertical);
        }

        public ToggledScrollingSlot(Container container, int slot, int x, int y, boolean vertical, int startingOffset)
        {
            super(container, slot, x, y, vertical, startingOffset);
        }

        public ToggledScrollingSlot(Container container, int slot, int x, int y, boolean vertical, boolean enabled)
        {
            super(container, slot, x, y, vertical);

            this.enabled = enabled;
        }

        public ToggledScrollingSlot(Container container, int slot, int x, int y, boolean vertical, int startingOffset, boolean enabled)
        {
            super(container, slot, x, y, vertical, startingOffset);

            this.enabled = enabled;
        }

        @Override
        public boolean isActive()
        {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean enable)
        {
            this.enabled = enable;
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return this.enabled;
        }

        @Override
        public boolean mayPickup(Player player)
        {
            return this.enabled;
        }

        @Override
        public boolean isHighlightable()
        {
            return this.enabled;
        }
    }

    public class ToggledScrollingSlotItemHandler extends ScrollingSlotItemHandler implements Togglable
    {
        private boolean enabled;

        public ToggledScrollingSlotItemHandler(IItemHandler itemHandler, int slot, int x, int y, boolean vertical)
        {
            super(itemHandler, slot, x, y, vertical);
        }

        public ToggledScrollingSlotItemHandler(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset)
        {
            super(itemHandler, slot, x, y, vertical, startingOffset);
        }

        public ToggledScrollingSlotItemHandler(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, boolean enabled)
        {
            super(itemHandler, slot, x, y, vertical);

            this.enabled = enabled;
        }

        public ToggledScrollingSlotItemHandler(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset, boolean enabled)
        {
            super(itemHandler, slot, x, y, vertical, startingOffset);

            this.enabled = enabled;
        }

        @Override
        public boolean isActive()
        {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean enable)
        {
            this.enabled = enable;
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return this.enabled;
        }

        @Override
        public boolean mayPickup(Player player)
        {
            return this.enabled;
        }

        @Override
        public boolean isHighlightable()
        {
            return this.enabled;
        }
    }

    public class ToggledRemappableScrollingSlot extends RemappableContainerSlot implements Togglable, Scrollable
    {
        private final boolean vertical;
        private int offset = 0;
        private boolean enabled;

        public ToggledRemappableScrollingSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical)
        {
            this(itemHandler, slot, x, y, vertical, 0);
        }

        public ToggledRemappableScrollingSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset)
        {
            this(itemHandler, slot, x, y,  vertical, startingOffset, false);
        }

        public ToggledRemappableScrollingSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, boolean enabled)
        {
            this(itemHandler, slot, x, y, vertical, 0, enabled);
        }

        public ToggledRemappableScrollingSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset, boolean enabled)
        {
            super(itemHandler, slot, x, y);

            this.enabled = enabled;
            this.vertical = vertical;
            this.setOffset(startingOffset);
        }

        @Override
        public boolean isActive()
        {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean enable)
        {
            this.enabled = enable;
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return this.enabled && super.mayPlace(stack);
        }

        @Override
        public boolean mayPickup(Player player)
        {
            return this.enabled && super.mayPickup(player);
        }

        @Override
        public int getOffset()
        {
            return offset;
        }

        @Override
        public void setOffset(int offset)
        {
            this.offset = offset < 16 ? offset > 0 ? Mth.clamp(offset, 0, 16) : offset + 16 : offset - 16;
        }

        @Override
        public void addToOffset(int offset)
        {
            this.setOffset(this.getOffset() + offset);
        }

        @Override
        public boolean isVertical()
        {
            return vertical;
        }

        @Override
        public boolean isHighlightable()
        {
            return this.enabled;
        }
    }

    public class ToggledRemappableScrollingContainerSlot extends RemappableSlot implements Togglable, Scrollable
    {
        private final boolean vertical;
        private int offset = 0;
        private boolean enabled;

        public ToggledRemappableScrollingContainerSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical)
        {
            this(itemHandler, slot, x, y, vertical, 0);
        }

        public ToggledRemappableScrollingContainerSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset)
        {
            this(itemHandler, slot, x, y,  vertical, startingOffset, false);
        }

        public ToggledRemappableScrollingContainerSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, boolean enabled)
        {
            this(itemHandler, slot, x, y, vertical, 0, enabled);
        }

        public ToggledRemappableScrollingContainerSlot(IItemHandler itemHandler, int slot, int x, int y, boolean vertical, int startingOffset, boolean enabled)
        {
            super(itemHandler, slot, x, y);

            this.enabled = enabled;
            this.vertical = vertical;
            this.setOffset(startingOffset);
        }

        @Override
        public boolean isActive()
        {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean enable)
        {
            this.enabled = enable;
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return this.enabled && super.mayPlace(stack);
        }

        @Override
        public boolean mayPickup(Player player)
        {
            return this.enabled && super.mayPickup(player);
        }

        @Override
        public int getOffset()
        {
            return offset;
        }

        @Override
        public void setOffset(int offset)
        {
            this.offset = offset < 16 ? offset > 0 ? Mth.clamp(offset, 0, 16) : offset + 16 : offset - 16;
        }

        @Override
        public void addToOffset(int offset)
        {
            this.setOffset(this.getOffset() + offset);
        }

        @Override
        public boolean isVertical()
        {
            return vertical;
        }

        @Override
        public boolean isHighlightable()
        {
            return this.enabled;
        }
    }


    public interface Scrollable
    {
        int getOffset();
        void setOffset(int offset);
        void addToOffset(int offset);
        boolean isVertical();
    }

    public interface Togglable
    {
        void setEnabled(boolean enable);
    }

    public class RemappableContainerSlot extends Slot
    {
        private static Container emptyInventory = new SimpleContainer(0);
        @Nullable
        protected IItemHandler itemHandler;
        protected int slot;

        public RemappableContainerSlot(@Nullable IItemHandler itemHandler, int slot, int xPosition, int yPosition) {
            super(emptyInventory, slot, xPosition, yPosition);
            this.itemHandler = itemHandler;
            this.slot = slot;
        }

        /**
        / if you are setting itemHandler to null, set itemSlot to -1
        */
        public void setItemHandler(IItemHandler itemHandler, int slot)
        {
            if (itemHandler != null)
            {
                this.itemHandler = itemHandler;
                this.slot = slot;
            }
            else
            {
                this.itemHandler = null;
                this.slot = -1;
            }
        }

        public boolean mayPlace(ItemStack stack)
        {
            if (this.itemHandler != null)
            {
                return stack.isEmpty() ? false : this.itemHandler.isItemValid(this.slot, stack);
            }
            return false;
        }

        public ItemStack getItem()
        {
            if (this.itemHandler != null)
            {
                return this.getItemHandler().getStackInSlot(this.slot);
            }
            return ItemStack.EMPTY;
        }

        public void set(ItemStack stack)
        {
            if (this.itemHandler != null)
            {
                ((IItemHandlerModifiable)this.getItemHandler()).setStackInSlot(this.index, stack);
                this.setChanged();
            }
        }

        public void initialize(ItemStack stack)
        {
            if (this.itemHandler != null)
            {
                ((IItemHandlerModifiable)this.getItemHandler()).setStackInSlot(this.index, stack);
                this.setChanged();
            }
        }

        public void onQuickCraft(ItemStack oldStackIn, ItemStack newStackIn)
        {
        }

        public int getMaxStackSize()
        {
            if (this.itemHandler != null)
            {
                return this.itemHandler.getSlotLimit(this.index);
            }
            return 0;
        }

        public int getMaxStackSize(ItemStack stack)
        {
            if (this.itemHandler != null)
            {
                return Math.min(stack.getMaxStackSize(), this.itemHandler.getSlotLimit(this.index));
            }
            return 0;
        }

        public boolean mayPickup(Player playerIn)
        {
            if (this.itemHandler != null)
            {
                return !this.getItemHandler().extractItem(this.index, 1, true).isEmpty();
            }
            return false;
        }

        public ItemStack remove(int amount)
        {
            if (this.itemHandler != null)
            {
                return this.getItemHandler().extractItem(this.index, amount, false);
            }
            return ItemStack.EMPTY;
        }

        public IItemHandler getItemHandler()
        {
            return this.itemHandler;
        }
    }
    
    public class RemappableSlot extends SlotItemHandler
    {
        @Setter
        @Getter
        protected int slot;
        private final IItemHandler itemHandler;

        public RemappableSlot(IItemHandler itemHandler, int slot, int xPosition, int yPosition) {
            super(itemHandler, slot, xPosition, yPosition);
            this.itemHandler = itemHandler;
            this.slot = slot;
        }

        public boolean mayPlace(ItemStack stack) 
        {
            return stack.isEmpty() ? false : this.itemHandler.isItemValid(this.slot, stack);
        }

        public ItemStack getItem() 
        {
            return this.getItemHandler().getStackInSlot(this.slot);
        }

        public void set(ItemStack stack) 
        {
            ((IItemHandlerModifiable)this.getItemHandler()).setStackInSlot(this.slot, stack);
            this.setChanged();
        }

        public void initialize(ItemStack stack) 
        {
            ((IItemHandlerModifiable)this.getItemHandler()).setStackInSlot(this.slot, stack);
            this.setChanged();
        }

        public int getMaxStackSize() 
        {
            return this.itemHandler.getSlotLimit(this.slot);
        }

        public int getMaxStackSize(ItemStack stack) 
        {
            return Math.min(stack.getMaxStackSize(), this.itemHandler.getSlotLimit(this.slot));
        }

        public boolean mayPickup(Player playerIn) 
        {
            return !this.getItemHandler().extractItem(this.slot, 1, true).isEmpty();
        }

        public ItemStack remove(int amount) 
        {
            return this.getItemHandler().extractItem(this.slot, amount, false);
        }
    }
}
