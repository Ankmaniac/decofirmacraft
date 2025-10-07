package com.ankmaniac.decofirmacraft.client.render.blockentity;


import com.ankmaniac.decofirmacraft.common.blockentities.GobletBlockEntity;
import com.ankmaniac.decofirmacraft.config.DFCConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.RenderHelpers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.neoforged.neoforge.fluids.FluidStack;

public class GobletBlockEntityRenderer implements BlockEntityRenderer<GobletBlockEntity>
{
    @Override
    public void render(GobletBlockEntity goblet, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        final GobletBlockEntity.GobletTank tank = goblet.getTank();
        final FluidStack fluidStack = tank.getFluidInTank(0);
        if (!fluidStack.isEmpty())
        {
            final float fillPercent = (float) fluidStack.getAmount() / DFCConfig.SERVER.gobletCapacity.get();
            RenderHelpers.renderFluidFace(poseStack, fluidStack, buffer,
                    0.4375f,
                    0.4375f,
                    0.5625f,
                    0.5625f,
                    0.25F +  0.15625F * fillPercent, combinedOverlay, combinedLight);
        }
    }
}