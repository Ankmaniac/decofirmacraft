package com.ankmaniac.decofirmacraft.common.blocks.rock;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RoadBlock extends Block {
    public RoadBlock(Properties pProperties) {
        super(pProperties.forceSolidOff());
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
}
