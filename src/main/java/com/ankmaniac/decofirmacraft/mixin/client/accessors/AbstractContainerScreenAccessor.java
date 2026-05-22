package com.ankmaniac.decofirmacraft.mixin.client.accessors;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor
{
    @Accessor("leftPos")
    int getLeftPos();

    @Accessor("topPos")
    int getTopPos();
}
