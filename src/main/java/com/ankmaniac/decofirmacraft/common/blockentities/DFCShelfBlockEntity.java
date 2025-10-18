package com.ankmaniac.decofirmacraft.common.blockentities;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.config.TFCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class DFCShelfBlockEntity extends InventoryBlockEntity<ItemStackHandler> {


    public static final int SLOT_START = 0;
    public static final int SLOT_END = 3;

    public DFCShelfBlockEntity(BlockPos pos, BlockState state){
        super(DFCBlockEntities.DFC_SHELVES.get(), pos, state, defaultInventory(4));
    }

    @Override
    public void setBlockState(BlockState state)
    {
        super.setBlockState(state);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        super.loadAdditional(nbt, provider);
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        super.saveAdditional(nbt, provider);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);
        markForSync();
    }

    @Override
    public int getSlotStackLimit(int slot)
    {
        return 32;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return ItemSizeManager.get(stack).getSize(stack).isEqualOrSmallerThan(TFCConfig.SERVER.chestMaximumItemSize.get());
    }
}
