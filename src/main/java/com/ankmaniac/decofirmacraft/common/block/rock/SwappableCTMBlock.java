package com.ankmaniac.decofirmacraft.common.block.rock;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.Random;

public class SwappableCTMBlock extends Block {
    public static final BooleanProperty STATE = BooleanProperty.create("state");

    private static final Random RANDOM = new Random();

    public SwappableCTMBlock(Properties properties){
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(STATE, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(STATE));
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (Helpers.isItem(player.getItemInHand(hand), TFCTags.Items.TOOLS_HAMMER))
        {
            level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

            int particleCount = 100;
            for (int i = 0; i < particleCount; i++) {
                double speedX = RANDOM.nextDouble() * 20 - 10;
                double speedZ = RANDOM.nextDouble() * 20 - 10;
                double offsetY = RANDOM.nextDouble() * 1;
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), pos.getX() + 0.5, pos.getY() + offsetY, pos.getZ() + 0.5, speedX, -0.1, speedZ);
            }

            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).swing(hand, true);
            }

            Boolean currentState = blockState.getValue(STATE);
            Boolean newState = !currentState;

            level.setBlockAndUpdate(pos, blockState.setValue(STATE, newState));
            return ItemInteractionResult.SUCCESS;
        }
        else
        {
            return ItemInteractionResult.FAIL;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("dfc.tooltip.swappablectmblock").withStyle(ChatFormatting.WHITE));
    }
}
