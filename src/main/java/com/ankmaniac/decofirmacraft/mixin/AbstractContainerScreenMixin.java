package com.ankmaniac.decofirmacraft.mixin;

import com.ankmaniac.decofirmacraft.common.container.DFCInventoryContainer;
import com.ankmaniac.decofirmacraft.mixin.client.accessors.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin
{
    @Inject(method = "isHovering(Lnet/minecraft/world/inventory/Slot;DD)Z", at = @At("head"), cancellable = true)
    private void inject$isHovering(Slot slot, double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir)
    {
        AbstractContainerScreen screen = (AbstractContainerScreen)(Object)this;
        if (slot instanceof DFCInventoryContainer.RemappableSlot remappableSlot)
        {
            DFCInventoryContainer.Scroller scroller = remappableSlot.getScroller();
            int x1 = slot.x - 1;
            int y1 = slot.y - 1;
            int x2 = slot.x + 17;
            int y2 = slot.y + 17;

            int windowSize = scroller.getWindowSize() - 2;
            int windowOffset = scroller.getWindowOffset() + 1;
            if (scroller.isVertical())
            {
                int cutoffSize = scroller.getCutoffSize(y1, y2);

                y1 -= remappableSlot.getOffset() % 18;
                y2 -= remappableSlot.getOffset() % 18;

                if (cutoffSize != y2 - y1)
                {
                    if (scroller.getCutoffSide(y1, y2))
                    {
                        y1 = windowSize + windowOffset - cutoffSize;
                        y2 = windowSize + windowOffset;
                    }
                    else
                    {
                        y1 = windowOffset;
                        y2 = windowOffset + cutoffSize;
                    }
                }
            }
            else
            {
                int cutoffSize = scroller.getCutoffSize(x1, x2);

                x1 -= remappableSlot.getOffset() % 18;
                x2 -= remappableSlot.getOffset() % 18;

                if (cutoffSize != x2 - x1)
                {
                    if (scroller.getCutoffSide(x1, x2))
                    {
                        x1 = windowSize + windowOffset - cutoffSize;
                        x2 = windowSize + windowOffset;
                    }
                    else
                    {
                        x1 = windowOffset;
                        x2 = windowOffset + cutoffSize;
                    }
                }
            }
            int i = ((AbstractContainerScreenAccessor) screen).getLeftPos();
            int j = ((AbstractContainerScreenAccessor) screen).getTopPos();
            mouseX -= i;
            mouseY -= j;
            boolean returnValue = mouseX >= (double)(x1) && mouseX < (double)(x2) && mouseY >= (double)(y1) && mouseY < (double)(y2);

            cir.setReturnValue(returnValue);
        }
    }
}
