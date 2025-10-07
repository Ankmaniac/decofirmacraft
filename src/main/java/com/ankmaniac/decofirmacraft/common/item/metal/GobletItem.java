package com.ankmaniac.decofirmacraft.common.item.metal;


import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.ankmaniac.decofirmacraft.config.DFCConfig;
import net.dries007.tfc.common.capabilities.DelegateFluidHandler;
import net.dries007.tfc.common.capabilities.FluidTankCallback;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.player.IPlayerInfo;
import net.dries007.tfc.common.player.PlayerInfo;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.Drinkable;
import net.dries007.tfc.util.tooltip.Tooltips;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class GobletItem extends BlockItem
{

    public GobletItem(Block block, Properties properties, Supplier<Integer> capacity, TagKey<Fluid> whitelist)
    {
        super(block, properties.component(TFCComponents.FLUID, FluidComponent.EMPTY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        final InteractionResult result = tryInteractWithFluid(level, player, hand);
        if (result != InteractionResult.PASS)
        {
            return new InteractionResultHolder<>(result, player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        // We override both `use` and `useOn`, as we want fluid filling to take priority, if we can place a block too.
        // The `super` call here is what eventually does block placement, and will call `use` if not.
        // `use` is called directly in the event we don't target a block at all.
        final Player player = context.getPlayer();
        if (player != null)
        {
            final InteractionResult result = tryInteractWithFluid(context.getLevel(), player, context.getHand());
            if (result != InteractionResult.PASS)
            {
                return result;
            }
        }
        return super.useOn(context);
    }

    public InteractionResult tryInteractWithFluid(Level level, Player player, InteractionHand hand)
    {
        final ItemStack stack = player.getItemInHand(hand);
        final IFluidHandler handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null)
        {
            return InteractionResult.PASS;
        }

        // Try to interact with liquid from the world (i.e. take water sources, fill up / empty out barrels, etc.)
        final BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (FluidHelpers.transferBetweenWorldAndItem(stack, level, hit, player, hand, false, false, true))
        {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // If nothing could be done in-world, try to empty the goblet
        if (handler != null && !handler.getFluidInTank(0).isEmpty())
        {
            return attemptEmptying(handler, level, player, stack, hand);
        }

        return InteractionResult.PASS;
    }

    /**
     * Try to empty the goblet. This can either be by dumping the contents (if the player is sneaking), or by drinking the contents (by using the goblet for an extended period)
     */
    protected InteractionResult attemptEmptying(IFluidHandler handler, Level level, Player player, ItemStack stack, InteractionHand hand)
    {
        if (player.isShiftKeyDown())
        {
            level.playSound(player, player.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.5f, 1.2f);
            handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            return InteractionResult.CONSUME;
        }

        final Drinkable drinkable = Drinkable.get(handler.getFluidInTank(0).getFluid());
        if (drinkable != null)
        {
            if (!drinkable.mayDrinkWhenFull() && IPlayerInfo.get(player).getThirst() >= PlayerInfo.MAX_THIRST)
            {
                return InteractionResult.FAIL;
            }
            return startUsingInstantly(level, player, hand);
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult startUsingInstantly(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    /**
     * Actual drinking functionality of the {@code GobletItem}, consuming the liquid held by the goblet if the player finishes using it
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        final IFluidHandler handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        final FluidStack drained = handler.drain(100, IFluidHandler.FluidAction.EXECUTE);
        if (entity instanceof Player player)
        {
            final Drinkable drinkable = Drinkable.get(drained.getFluid());
            if (drinkable != null && !level.isClientSide)
            {
                drinkable.onDrink(player, drained.getAmount());
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
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return 32;
    }

    /**
     * Change the name to include the contained liquid, if there is one
     */
    @Override
    public Component getName(ItemStack stack)
    {
        final FluidStack fluid = FluidHelpers.getContainedFluid(stack);
        if (!fluid.isEmpty())
        {
            return Component.translatable(getDescriptionId(stack) + ".filled", fluid.getHoverName());
        }
        return super.getName(stack);
    }

    /**
     * Adding a tooltip to the item for the amount of liquid contained, as well as the total capacity
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced)
    {
        final FluidStack fluid = FluidHelpers.getContainedFluid(stack);
        if (!fluid.isEmpty() && fluid.getAmount() < DFCConfig.SERVER.gobletCapacity.get())
        {
            tooltip.add(Tooltips.fluidUnitsAndCapacityOf(fluid, DFCConfig.SERVER.gobletCapacity.get()));
        }
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
        return !FluidHelpers.getContainedFluid(stack).isEmpty();
    }
}