package com.ankmaniac.decofirmacraft.mixin;

import com.ankmaniac.decofirmacraft.util.DFCTags;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronBarsBlock.class)
public abstract class IronBarsBlockMixin {

    @Inject(method = "attachsTo", at = @At("HEAD"), cancellable = true)
    private void injectAttachsTo(BlockState state, boolean solidSide, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(DFCTags.Blocks.METAL_GATES)) {
            cir.setReturnValue(true);
        }
    }
}