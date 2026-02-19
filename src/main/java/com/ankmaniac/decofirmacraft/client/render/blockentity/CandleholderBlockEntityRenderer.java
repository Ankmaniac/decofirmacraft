package com.ankmaniac.decofirmacraft.client.render.blockentity;

import com.ankmaniac.decofirmacraft.common.block.metal.CandleholderBlock;
import com.ankmaniac.decofirmacraft.common.blockentities.CandleholderBlockEntity;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.common.blocks.TFCCandleBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CandleholderBlockEntityRenderer implements BlockEntityRenderer<CandleholderBlockEntity> {
    @Override
    public void render(CandleholderBlockEntity candleholder, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        final ItemStack itemStack = candleholder.getInventory().getStackInSlot(0);
        final BlockState state = candleholder.getBlockState();
        if (candleholder.getBlockState().getValue(CandleholderBlock.CANDLES) > 0 && !itemStack.isEmpty()){
            for (int i = 0; i < itemStack.getCount(); i++){
                poseStack.pushPose();

                final Vec3 offset = CandleholderBlock.PARTICLE_OFFSETS
                        .get(state.getValue(CandleholderBlock.ON_WALL))
                        .get(state.getValue(CandleholderBlock.DIRECTION))
                        .get(state.getValue(CandleholderBlock.CANDLES))
                        .get(i);

                poseStack.translate(offset.x - .5, offset.y - .5, offset.z - .5);

                Item item = itemStack.getItem();
                BlockState candleState = ((BlockItem)item).getBlock().defaultBlockState()
                        .setValue(CandleBlock.CANDLES, 1)
                        .setValue(CandleBlock.LIT, state.getValue(CandleholderBlock.LIT));
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(candleState, poseStack, buffer, combinedLight, combinedOverlay);

                poseStack.popPose();
            }
        }
    }
}
