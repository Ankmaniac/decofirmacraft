package com.ankmaniac.decofirmacraft.common.blockentities;

import com.ankmaniac.decofirmacraft.common.item.metal.GobletItem;
import com.ankmaniac.decofirmacraft.config.DFCConfig;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.common.capabilities.*;
import net.dries007.tfc.common.component.fluid.FluidContainerInfo;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class GobletBlockEntity extends TFCBlockEntity implements FluidTankCallback
{
    protected final GobletTank tank;

    public GobletBlockEntity(BlockPos pos, BlockState state)
    {
        this(DFCBlockEntities.GOBLET.get(), pos, state);
    }

    protected GobletBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);

        this.tank = new GobletTank(this);
    }

    public GobletTank getTank() {
        return tank;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        tank.deserializeNBT(provider, tag.getCompound("tank"));
        super.loadAdditional(tag, provider);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        tag.put("tank", tank.serializeNBT(provider));
        super.saveAdditional(tag, provider);
    }

    /**
     * Custom internal tank class, used to make the internal tank of the {@code GobletBlockEntity} and of the {@link GobletItem} act as one and the same
     */
    public static class GobletTank implements DelegateFluidHandler, INBTSerializable<CompoundTag>, FluidTankCallback
    {
        public static final FluidContainerInfo INFO = new FluidContainerInfo() {
            @Override
            public boolean canContainFluid(Fluid input)
            {
                return Helpers.isFluid(input, TFCTags.Fluids.USABLE_IN_BARREL);
            }

            @Override
            public int fluidCapacity()
            {
                return DFCConfig.SERVER.gobletCapacity.get();
            }
        };

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
        public CompoundTag serializeNBT(HolderLookup.Provider holder)
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.put("tank", tank.writeToNBT(holder, new CompoundTag()));
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider holder, CompoundTag tag)
        {
            tank.readFromNBT(holder, tag.getCompound("tank"));
        }

        @Override
        public void fluidTankChanged()
        {
            callback.fluidTankChanged();
        }
    }
}