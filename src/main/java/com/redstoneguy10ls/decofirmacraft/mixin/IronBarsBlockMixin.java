package com.redstoneguy10ls.decofirmacraft.mixin;

import com.redstoneguy10ls.decofirmacraft.common.blocks.metal.GateBlock;
import com.redstoneguy10ls.decofirmacraft.util.DFCTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.Block.isExceptionForConnection;

@Mixin(IronBarsBlock.class)
public abstract class IronBarsBlockMixin {

    @Inject(method = "attachsTo", at = @At("HEAD"), cancellable = true)
    private void injectAttachsTo(BlockState state, boolean solidSide, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(DFCTags.Blocks.METAL_GATES)) {
            cir.setReturnValue(true);
        }
    }
}