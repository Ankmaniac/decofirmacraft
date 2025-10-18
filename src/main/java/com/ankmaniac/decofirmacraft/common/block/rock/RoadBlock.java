package com.ankmaniac.decofirmacraft.common.block.rock;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class RoadBlock extends Block {
    public RoadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(!level.isClientSide())
        {
            if(entity instanceof LivingEntity)
            {
                if(Helpers.isEntity(entity, TFCTags.Entities.HORSES))
                {
                    LivingEntity livingEntity = ((LivingEntity) entity);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5,1,true,false,false));
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("dfc.tooltip.road").withStyle(ChatFormatting.WHITE));
    }
}
