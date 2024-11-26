package com.ankmaniac.decofirmacraft.common.items;

import com.mojang.datafixers.util.Either;
import com.ankmaniac.decofirmacraft.common.recipes.PaintingRecipe;
import com.ankmaniac.decofirmacraft.util.DFCTags;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Function;

public class PaintbrushItem extends ToolItem {
    public PaintbrushItem(Tier tier, Properties properties)
    {
        super(tier, calculateVanillaAttackDamage(-0.2f, tier), -2.0F, DFCTags.Blocks.MINEABLE_WITH_PAINTBRUSH,properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        final Player player = context.getPlayer();
        if (player != null && player.getMainHandItem() == context.getItemInHand())
        {
            final Level level = context.getLevel();
            final BlockPos pos = context.getClickedPos();
            final BlockState state = level.getBlockState(pos);
            final Either<BlockState, InteractionResult> result = PaintingRecipe.computeResult(player, state, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside()), true);
            return result.map(resultState -> {
                player.playSound(resultState.getSoundType().getHitSound(), 1f, 1f);

                ItemStack held = player.getMainHandItem();
                level.setBlockAndUpdate(pos, resultState);

                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }, Function.identity()); // returns the interaction result if we are given one
        }
        return InteractionResult.PASS;
    }
}
