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
import org.apache.commons.lang3.Range;

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

                final int angle = switch (shelf.getBlockState().getValue(DIRECTION))
                {
                    case SOUTH -> 0;
                    case EAST -> 90;
                    case WEST, DOWN, UP -> 270;
                    case NORTH -> 180;
                };

                final Vec3 pos = DFCShelfBlock.SLOT_CENTERS.get(shelf.getBlockState().getValue(DIRECTION)).get(i);
                poseStack.translate(pos.x, pos.y, pos.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(angle));

                int stackSize = item.getCount();
                Weight itemWeight = ItemSizeManager.get(item).getWeight(item);
                int maxStackSize = Math.min(itemWeight.stackSize, shelf.getSlotStackLimit(i));
                int perBlock = maxStackSize / 8;
                int stackCount = (int) Math.floor((double) (item.getCount() - 1) / (double) perBlock);
                int maxStacks = 8;


                switch(itemWeight){
                    case VERY_LIGHT, LIGHT, MEDIUM, HEAVY:
                    default:
                        poseStack.scale(0.3f, 0.3f, 0.3f);
                        if (itemWeight == Weight.HEAVY){
                            maxStacks = 4;
                            perBlock = 1;
                        }
                        break;
                    case VERY_HEAVY:
                        poseStack.scale(0.5f, 0.5f, 0.5f);
                        poseStack.translate(0f, 0.15f, 0f);
                        maxStacks = 1;
                        perBlock = 1;
                        break;
                }

                int iteration = 0;
                for (int j = 0; j < stackSize; j++) {
                    if (iteration >= maxStacks){
                        break;
                    }
                    else if (iterationCounter(itemWeight, j, perBlock)){
                        poseStack.pushPose();
                        float translateX = 0;
                        float translateY = 0;
                        float translateZ = 0;
                        if (iteration == stackCount && iteration % 2 == 0) {
                            if (iteration > 3) translateY = .5f;
                            if (iteration == 2 || iteration == 6) translateZ = -.25f;
                        }
                        else {
                            if (iteration % 2 == 0) {translateX = .25f;}
                            else {translateX = -.25f;}
                            if (iteration % 3 == 0) {
                                if (iteration % 2 == 0) {
                                    translateZ = .01f;
                                }
                                else {
                                    translateZ = -.01f;
                                }
                            }
                            if (iteration > 3) translateY = .5f;
                            if ((stackCount > 1 && iteration < 4) || stackCount > 5) {
                                if ((iteration > 1 && iteration < 4) || iteration > 5) {
                                    translateZ -= .25f;
                                } else {
                                    translateZ += .25f;
                                }
                            }
                        }
                        poseStack.translate(translateX, translateY, translateZ);

                        itemRenderer.renderStatic(item,
                                ItemDisplayContext.FIXED,
                                combinedLight,
                                combinedOverlay,
                                poseStack,
                                buffer,
                                shelf.getLevel(),
                                0
                        );
                        poseStack.popPose();
                        iteration++;
                    }
                }
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
