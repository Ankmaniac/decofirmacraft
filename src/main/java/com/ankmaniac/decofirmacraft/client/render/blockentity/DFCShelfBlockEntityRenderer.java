package com.ankmaniac.decofirmacraft.client.render.blockentity;

import com.ankmaniac.decofirmacraft.common.block.DFCShelfBlock;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
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

                switch(itemWeight){
                    case VERY_LIGHT:
                        poseStack.scale(0.2f, 0.2f, 0.2f);
                        break;
                    case LIGHT:
                    default:
                        poseStack.scale(0.3f, 0.3f, 0.3f);
                        break;
                    case MEDIUM:
                        poseStack.scale(0.4f, 0.4f, 0.4f);
                        poseStack.translate(0f, 0.1f, 0f);
                        break;
                    case HEAVY:
                        poseStack.scale(0.45f, 0.45f, 0.45f);
                        poseStack.translate(0f, 0.1f, 0f);
                        break;
                    case VERY_HEAVY:
                        poseStack.scale(0.5f, 0.5f, 0.5f);
                        poseStack.translate(0f, 0.15f, 0f);
                        break;
                }

                int iteration = 0;
                for (int j = 0; j < stackSize; j++) {
                    poseStack.pushPose();
                    switch(itemWeight){
                        case HEAVY:
                            if (iteration >= 4){
                                break;
                            }
                            else{
                                iteration++;
                            }
                            poseStack.pushPose();
                            poseStack.scale(0.45f, 0.45f, 0.45f);
                            poseStack.translate(0f, 0.1f, 0f);
                            if (j == stackSize - 1 && j % 2 == 0){
                                poseStack.translate(0, 0, j > 1 ? .5f : 0);
                            }
                            else {
                                poseStack.translate(j % 2 == 0 ? .5f : -.5f, 0f, j > 1 ? .5f : -.5f);
                            }
                            poseStack.popPose();
                        case VERY_HEAVY:
                            if (iteration >= 1){
                                break;
                            }
                            else{
                                iteration++;
                            }
                            poseStack.pushPose();
                            poseStack.scale(0.5f, 0.5f, 0.5f);
                            poseStack.translate(0f, 0.15f, 0f);
                            poseStack.popPose();
                    }
                    poseStack.popPose();
                }

                Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, shelf.getLevel(), 0);
                poseStack.popPose();
            }
        }
    }
}
