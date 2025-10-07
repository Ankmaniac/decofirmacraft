package com.ankmaniac.decofirmacraft.common.block.metal;

import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.capabilities.ItemCapabilities;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.Drinkable;
import net.dries007.tfc.util.loot.CopyFluidFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class GobletBlock extends ExtendedBlock implements EntityBlockExtension {

    protected static final VoxelShape GOBLET_SHAPE = Block.box(6, 0, 6, 10, 7, 10);

    public GobletBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        final @Nullable GobletBlockEntity goblet = level.getBlockEntity(pos, DFCBlockEntities.GOBLET.get()).orElse(null);

        if (goblet != null)
        {
            if (hand.equals(InteractionHand.MAIN_HAND) && stack.isEmpty())
            {
                if (player.isShiftKeyDown())
                {
                    if (!level.isClientSide)
                    {
                        level.destroyBlock(pos, true, player);
                    }

                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }

                final IFluidHandler handler = goblet.getTank(null);

                if (handler != null)
                {
                    final Drinkable drink = Drinkable.get(handler.getFluidInTank(0).getFluid());
                    if (drink == null) // Prevent drinking non-drinkable fluids
                    {
                        return ItemInteractionResult.FAIL;
                    }

                    final FluidStack drained = handler.drain(100, IFluidHandler.FluidAction.EXECUTE);
                    goblet.markForSync();

                    if (!level.isClientSide)
                    {
                        drink.onDrink(player, drained.getAmount());
                    }

                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            else if (FluidHelpers.transferBetweenBlockEntityAndItem(stack, goblet, player, hand))
            {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
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
