package com.ankmaniac.decofirmacraft.common.blocks.metal;

import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Drinkable;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.loot.CopyFluidFunction;

public class GobletBlock extends ExtendedBlock implements EntityBlockExtension
{
    protected static final VoxelShape GOBLET_SHAPE = Block.box(6, 0, 6, 9, 7, 9);

    public GobletBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        final @Nullable GobletBlockEntity goblet = level.getBlockEntity(pos, DFCBlockEntities.GOBLET.get()).orElse(null);

        if (goblet != null)
        {
            final ItemStack item = player.getItemInHand(hand);

            if (hand.equals(InteractionHand.MAIN_HAND) && item.isEmpty())
            {
                final IFluidHandler handler = goblet.getCapability(Capabilities.FLUID).resolve().orElse(null);

                if (handler != null)
                {
                    final Drinkable drink = Drinkable.get(handler.getFluidInTank(0).getFluid());
                    if (drink == null) // Prevent drinking non-drinkable fluids
                    {
                        return InteractionResult.PASS;
                    }

                    final FluidStack drained = handler.drain(GobletBlockEntity.DRINK_SIZE, IFluidHandler.FluidAction.EXECUTE);
                    goblet.markForSync();

                    if (!level.isClientSide)
                    {
                        drink.onDrink(player, drained.getAmount());
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            else
            {
                IFluidHandlerItem itemHandler = Helpers.getCapability(item, Capabilities.FLUID_ITEM);
                if (itemHandler != null && Helpers.isFluid(itemHandler.getFluidInTank(0).getFluid(), TFCTags.Fluids.USABLE_IN_JUG))
                {
                    if (FluidHelpers.transferBetweenBlockEntityAndItem(item, goblet, player, hand))
                    {
                        goblet.markForSync();
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        CopyFluidFunction.copyToItem(stack, level.getBlockEntity(pos));
        return stack;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new GobletBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return GOBLET_SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return Block.canSupportCenter(level, pos.relative(Direction.DOWN), Direction.UP);
    }
}
