package com.ankmaniac.decofirmacraft.client.render.blockentity;

import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.capabilities.Capabilities;

public class GobletBlockEntityRenderer implements BlockEntityRenderer<GobletBlockEntity>
{
    @Override
    public void render(GobletBlockEntity goblet, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        goblet.getCapability(Capabilities.FLUID).map(handler -> handler.getFluidInTank(0)).filter(fluid -> !fluid.isEmpty()).ifPresent(fluidStack ->
        {
            final float fillPercent = (float) fluidStack.getAmount() / GobletBlockEntity.GOBLET_CAPACITY;
            RenderHelpers.renderFluidFace(poseStack, fluidStack, buffer,
                0.4375f, 0.4375f,
                0.5625f, 0.5625f,
                0.25f + 0.15625f * fillPercent,
                combinedOverlay, combinedLight);
        });
    }
}
