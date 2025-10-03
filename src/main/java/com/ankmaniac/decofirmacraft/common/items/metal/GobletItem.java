package com.ankmaniac.decofirmacraft.common.items.metal;

import java.util.List;
import java.util.function.Supplier;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.DelegateFluidHandler;
import net.dries007.tfc.common.capabilities.FluidTankCallback;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Drinkable;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Tooltips;

/**
 * We want the GobletItem to mostly act like the {@link net.dries007.tfc.common.items.JugItem} class.
 * However, that class is a child on {@link net.minecraft.world.item.Item} instead of {@link BlockItem}, so we have to remake (some of) its functionality from scratch
 */
public class GobletItem extends BlockItem
{
    protected final TagKey<Fluid> whitelist;
    protected final Supplier<Integer> capacity;

    public GobletItem(Properties properties, Supplier<Integer> capacity, TagKey<Fluid> whitelist)
    {
        super(DFCBlocks.GOBLET_BLOCK.get(), properties);
        this.whitelist = whitelist;
        this.capacity = capacity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        final ItemStack stack = player.getItemInHand(hand);
        final BlockHitResult hit = Helpers.rayTracePlayer(level, player, ClipContext.Fluid.SOURCE_ONLY);

        // Try to interact with liquid from the world (i.e. take water sources, fill up / empty out barrels, etc.)
        if (FluidHelpers.transferBetweenWorldAndItem(stack, level, hit, player, hand, false, false, false))
        {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        // If nothing could be done in-world, try to empty the goblet
        final IFluidHandler handler = Helpers.getCapability(stack, Capabilities.FLUID_ITEM);
        if (handler != null && !handler.getFluidInTank(0).isEmpty())
        {
            return attemptEmptying(handler, level, player, stack, hand);
        }

        return InteractionResultHolder.pass(stack);
    }

    /**
     * Try to empty the goblet. This can either be by dumping the contents (if the player is sneaking), or by drinking the contents (by using the goblet for an extended period)
     */
    protected InteractionResultHolder<ItemStack> attemptEmptying(IFluidHandler handler, Level level, Player player, ItemStack stack, InteractionHand hand)
    {
        if (player.isShiftKeyDown())
        {
            level.playSound(player, player.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.5f, 1.2f);
            handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            return InteractionResultHolder.consume(stack);
        }

        final Drinkable drinkable = Drinkable.get(handler.getFluidInTank(0).getFluid());
        if (drinkable != null)
        {
            if (!drinkable.mayDrinkWhenFull() && player.getFoodData() instanceof TFCFoodData food && food.getThirst() >= TFCFoodData.MAX_THIRST)
            {
                return InteractionResultHolder.fail(stack);
            }
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResultHolder.pass(stack);
    }

    /**
     * Actual drinking functionality of the {@code GobletItem}, consuming the liquid held by the goblet if the player finishes using it
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        final IFluidHandler handler = stack.getCapability(Capabilities.FLUID_ITEM).resolve().orElse(null);
        if (handler != null)
        {
            final FluidStack drained = handler.drain(GobletBlockEntity.DRINK_SIZE, IFluidHandler.FluidAction.EXECUTE);
            if (entity instanceof Player player)
            {
                final Drinkable drinkable = Drinkable.get(drained.getFluid());
                if (drinkable != null && !level.isClientSide)
                {
                    drinkable.onDrink(player, drained.getAmount());
                }
            }
        }

        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return PotionItem.EAT_DURATION;
    }

    /**
     * Change the name to include the contained liquid, if there is one
     */
    @Override
    public Component getName(ItemStack stack)
    {
        final FluidStack fluid = stack.getCapability(Capabilities.FLUID_ITEM)
            .map(cap -> cap.getFluidInTank(0))
            .orElse(FluidStack.EMPTY);

        if (!fluid.isEmpty())
        {
            return Component.translatable(getDescriptionId(stack) + ".filled", fluid.getDisplayName());
        }

        return super.getName(stack);
    }

    /**
     * Adding a tooltip to the item for the amount of liquid contained, as well as the total capacity
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag isAdvanced)
    {
        stack.getCapability(Capabilities.FLUID_ITEM).ifPresent(cap -> {
            final FluidStack fluid = cap.getFluidInTank(0);
            if (!fluid.isEmpty() && fluid.getAmount() < capacity.get())
            {
                tooltips.add(Tooltips.fluidUnitsAndCapacityOf(fluid, capacity.get()));
            }
        });
    }

    /**
     * For crafting recipes that use items only for the liquid they hold, make sure that the goblet isn't consumed
     */
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack)
    {
        return new ItemStack(this);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return stack.getCapability(Capabilities.FLUID_ITEM).map(cap -> !cap.getFluidInTank(0).isEmpty()).orElse(false);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new GobletItemStackTank(stack);
    }

    /**
     * Custom internal tank class, used to make the internal tank of the {@link GobletBlockEntity} and of the {@code GobletItem} act as one and the same
     */
    private static class GobletItemStackTank implements ICapabilityProvider, DelegateFluidHandler, IFluidHandlerItem, FluidTankCallback
    {
        private final LazyOptional<GobletItem.GobletItemStackTank> capability;
        private final ItemStack stack;
        private final GobletBlockEntity.GobletTank tank;

        GobletItemStackTank(ItemStack stack)
        {
            this.capability = LazyOptional.of(() -> this);
            this.stack = stack;
            this.tank = new GobletBlockEntity.GobletTank(this);

            load();
        }

        @Override
        public void fluidTankChanged()
        {
            save();
        }

        @Override
        public IFluidHandler getFluidHandler()
        {
            return tank;
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
        {
            if (cap == Capabilities.FLUID_ITEM || cap == Capabilities.FLUID)
            {
                return capability.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public @NotNull ItemStack getContainer()
        {
            return stack.copy();
        }

        private void load()
        {
            final CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(Helpers.BLOCK_ENTITY_TAG, Tag.TAG_COMPOUND))
            {
                final CompoundTag blockEntityTag = tag.getCompound(Helpers.BLOCK_ENTITY_TAG);
                tank.deserializeNBT(blockEntityTag.getCompound("tank"));
            }
        }

        private void save()
        {
            if (tank.isEmpty())
            {
                stack.removeTagKey(Helpers.BLOCK_ENTITY_TAG);
            }
            else
            {
                stack.getOrCreateTagElement(Helpers.BLOCK_ENTITY_TAG).put("tank", tank.serializeNBT());
            }
        }
    }
}
