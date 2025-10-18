package com.ankmaniac.decofirmacraft.client.render.blockentity;

import com.ankmaniac.decofirmacraft.common.block.DFCShelfBlock;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import static com.ankmaniac.decofirmacraft.common.block.DFCShelfBlock.DIRECTION;
import static com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity.*;

public class DFCShelfBlockEntityRenderer implements BlockEntityRenderer<DFCShelfBlockEntity>
{
    @Override
    public void render(DFCShelfBlockEntity shelf, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        for (int i = SLOT_START; i <= SLOT_END; i++)
        {
            final ItemStack item = shelf.getInventory().getStackInSlot(i);
            if (!item.isEmpty())
            {
                final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                poseStack.pushPose();

                final Vec3 pos = DFCShelfBlock.SLOT_CENTERS.get(shelf.getBlockState().getValue(DIRECTION)).get(i);
                poseStack.translate(pos.x, pos.y, pos.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(shelf.getBlockState().getValue(DIRECTION).toYRot()));

                int stackSize = item.getCount();
                Weight itemWeight = ItemSizeManager.get(item).getWeight(item);
                int maxStackSize = Math.min(itemWeight.stackSize, shelf.getSlotStackLimit(i));
                float filled = (float) stackSize / (float) maxStackSize;
                int perBlock = maxStackSize / 8;
                int stackCount = (int) Math.floor((double) item.getCount() / (double) perBlock);
                int maxStacks = 8;


                switch(itemWeight){
                    case VERY_LIGHT, LIGHT, MEDIUM, HEAVY:
                    default:
                        poseStack.scale(0.3f, 0.3f, 0.3f);
                        if (itemWeight == Weight.HEAVY){
                            maxStacks = 4;
                        }
                        break;
                    case VERY_HEAVY:
                        poseStack.scale(0.5f, 0.5f, 0.5f);
                        poseStack.translate(0f, 0.15f, 0f);
                        maxStacks = 1;
                        break;
                }

                int iteration = 0;
                for (int j = 0; j < stackSize; j++) {
                    poseStack.pushPose();
                    if (iteration >= maxStacks){
                        break;
                    }
                    else if (iterationCounter(itemWeight, j, perBlock)){
                        poseStack.popPose();
                        if (iteration == stackCount && iteration % 2 == 0){
                            poseStack.translate(0, j > 4 ? .5f : 0f, j > 1 ? .5f : 0);
                        }
                        else {
                            poseStack.translate(j % 2 == 0 ? .5f : -.5f, j > 4 ? .5f : 0f, j > 1 ? .5f : -.5f);
                        }
                        poseStack.popPose();
                        iteration++;
                    }
                    poseStack.popPose();
                }

                Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, shelf.getLevel(), 0);
                poseStack.popPose();
            }
        }
    }
    private boolean iterationCounter(Weight weight, int j, int perBlock) {
        switch (weight) {
            case VERY_LIGHT, LIGHT, MEDIUM:
            default:
                return j % perBlock == 0;
            case HEAVY, VERY_HEAVY:
                return true;
        }
    }
}
