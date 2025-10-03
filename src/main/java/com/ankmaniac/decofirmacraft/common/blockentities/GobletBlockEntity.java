package com.ankmaniac.decofirmacraft.common.blockentities;

import com.ankmaniac.decofirmacraft.common.items.metal.GobletItem;
import com.ankmaniac.decofirmacraft.config.DFCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.DelegateFluidHandler;
import net.dries007.tfc.common.capabilities.FluidTankCallback;
import net.dries007.tfc.common.capabilities.InventoryFluidTank;
import net.dries007.tfc.util.Helpers;

public class GobletBlockEntity extends TFCBlockEntity implements FluidTankCallback
{
    private GobletTank tank;
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public GobletBlockEntity(BlockPos pos, BlockState state)
    {
        this(DFCBlockEntities.GOBLET.get(), pos, state);
    }

    protected GobletBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        this.tank = new GobletTank(this);
    }

    @Override
    public void loadAdditional(CompoundTag tag)
    {
        tank.deserializeNBT(tag.getCompound("tank"));
        super.loadAdditional(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        tag.put("tank", tank.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == Capabilities.FLUID)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    /**
     * Custom internal tank class, used to make the internal tank of the {@code GobletBlockEntity} and of the {@link GobletItem} act as one and the same
     */
    public static class GobletTank implements DelegateFluidHandler, INBTSerializable<CompoundTag>, FluidTankCallback
    {
        private final FluidTankCallback callback;
        public final InventoryFluidTank tank;

        GobletTank(GobletBlockEntity entity)
        {
            this((FluidTankCallback) entity);
        }

        public GobletTank(FluidTankCallback callback)
        {
            this.callback = callback;
            this.tank = new InventoryFluidTank(DFCConfig.SERVER.gobletCapacity.get(), stack -> Helpers.isFluid(stack.getFluid(), TFCTags.Fluids.USABLE_IN_JUG), this);
        }

        @NotNull
        @Override
        public IFluidHandler getFluidHandler()
        {
            return tank;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return tank.fill(resource, action);
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            return tank.drain(resource, action);
        }

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            return tank.drain(maxDrain, action);
        }

        public boolean isEmpty()
        {
            return tank.isEmpty();
        }

        @Override
        public CompoundTag serializeNBT()
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.put("tank", tank.writeToNBT(new CompoundTag()));
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt)
        {

            tank.readFromNBT(nbt.getCompound("tank"));
        }

        @Override
        public void fluidTankChanged()
        {
            callback.fluidTankChanged();
        }
    }
}
