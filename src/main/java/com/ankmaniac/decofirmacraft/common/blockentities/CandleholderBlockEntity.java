package com.ankmaniac.decofirmacraft.common.blockentities;

import com.ankmaniac.decofirmacraft.util.DFCTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;

public class CandleholderBlockEntity extends InventoryBlockEntity<ItemStackHandler> {


    public CandleholderBlockEntity(BlockPos pos, BlockState state){
        super(DFCBlockEntities.CANDLEHOLDER.get(), pos, state, defaultInventory(1));
    }

    protected CandleholderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state, defaultInventory(1));
    }

    /*Have to copy the functions from TickCounterBlockEntity because its easier than
     extending tickcounterblockentity but adding the inventory support (im lazy)*/
    public static void reset(Level level, BlockPos pos)
    {
        level.getBlockEntity(pos, DFCBlockEntities.CANDLEHOLDER.get()).ifPresent(CandleholderBlockEntity::resetCounter);
    }

    public static void addTicks(Level level, BlockPos pos, long ticks)
    {
        Optional<CandleholderBlockEntity> entity = level.getBlockEntity(pos, DFCBlockEntities.CANDLEHOLDER.get());
        if (entity.isPresent())
        {
            entity.get().increaseCounter(ticks);
        }
    }

    protected long lastUpdateTick = Integer.MIN_VALUE;


    public long getTicksSinceUpdate()
    {
        assert level != null;
        return Calendars.get(level).getTicks() - lastUpdateTick;
    }

    public void setLastUpdateTick(long tick)
    {
        lastUpdateTick = tick;
        setChanged();
    }

    public long getLastUpdateTick()
    {
        return lastUpdateTick;
    }

    public void resetCounter()
    {
        lastUpdateTick = Calendars.SERVER.getTicks();
        setChanged();
    }

    /**
     * Reduces the amount of time counted by setting the lastUpdateTick to be more recent
     */
    public void reduceCounter(long amount)
    {
        lastUpdateTick += amount;
        setChanged();
    }

    /**
     * Adds to the amount of time counted by setting the lastUpdateTick farther in the past
     */
    public void increaseCounter(long amount)
    {
        lastUpdateTick -= amount;
        setChanged();
    }

    @Override
    public void setBlockState(BlockState state)
    {
        super.setBlockState(state);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        lastUpdateTick = nbt.getLong("tick");
        super.loadAdditional(nbt, provider);
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        nbt.putLong("tick", lastUpdateTick);
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
        return 4;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return Helpers.isItem(stack, DFCTags.Items.CANDLES);
    }
}
