package com.ankmaniac.decofirmacraft.mixin;

import com.ankmaniac.decofirmacraft.util.DFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.IMinecartCollisionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin
{
    @Accessor("onRails")
    abstract void setOnRails(boolean onRails);

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;onRails:Z", shift = At.Shift.AFTER), cancellable = true)
    private void inject$onRails(CallbackInfo ci)
    {
        AbstractMinecart cart = (AbstractMinecart)(Object)this;
        int i = Mth.floor(cart.getX());
        int j = Mth.floor(cart.getY());
        int k = Mth.floor(cart.getZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        Level level = cart.level();

        if (level.getBlockState(blockpos.below()).is(DFCTags.Blocks.RAIL_BLOCKS))
        {
            BlockState blockstate = level.getBlockState(blockpos.below());
            setOnRails(BaseRailBlock.isRail(blockstate));
    //            System.out.println("DEBUG ONRAILSMIXIN onRails: " + this.onRails);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean redirect$isBlockTag(BlockState state, TagKey<Block> tag)
    {
        if (tag == BlockTags.RAILS && state.is(DFCTags.Blocks.RAIL_BLOCKS))
        {
            return false;
        }

        return state.is(tag);
    }

    @Inject(method = "getPos", at = @At("RETURN"), cancellable = true)
    private void inject$getPos(double x, double y, double z, CallbackInfoReturnable<Vec3> cir)
    {
        AbstractMinecart cart = (AbstractMinecart)(Object)this;
        Level level = cart.level();
        Vec3 result = cir.getReturnValue();
            if (result != null)
            {
                int ii = Mth.floor(x);
                int ij = Mth.floor(y);
                int ik = Mth.floor(z);
                double i = result.x;
                double j = result.y;
                double k = result.z;

                if (level.getBlockState(new BlockPos(ii, ij - 1, ik)).is(DFCTags.Blocks.RAIL_BLOCKS))
                {
                    result = new Vec3(i, j + 1, k);
                    cir.setReturnValue(result);
                }

                if (level.getBlockState(new BlockPos(ii, ij, ik)).is(DFCTags.Blocks.RAIL_BLOCKS))
                {
                    result = new Vec3(i, j + 1, k);
                    cir.setReturnValue(result);
                }
            }
    }

    @ModifyVariable(method = "tick", at = @At(value = "STORE", ordinal = 0), index = 5)
    private BlockState modifyRailBlockState(BlockState state)
    {
        AbstractMinecart cart = (AbstractMinecart)(Object)this;
        Level level = cart.level();
        if (state.is(Blocks.AIR))
        {
            int i = Mth.floor(cart.getX());
            int j = Mth.floor(cart.getY());
            int k = Mth.floor(cart.getZ());
            return level.getBlockState(new BlockPos(i, j - 1, k));
        }
        return state;
    }


//
//    @Inject(method = "moveMinecartOnRail", at = @At("HEAD"), cancellable = true)
//    private void inject$moveMinecartOnRail(BlockPos pos, CallbackInfo ci)
//    {
//        AbstractMinecart cart = (AbstractMinecart)(Object)this;
//        Level level = cart.level();
//
//        if (level.getBlockState(pos).is(DFCTags.Blocks.RAIL_BLOCKS))
//        {
//            System.out.println("DEBUG MOVEMINECART");
//            pos = pos.above();
//            double d24 = cart.isVehicle() ? 0.75D : 1.0D;
//            double d25 = cart.getMaxSpeedWithRail();
//            Vec3 vec3d1 = cart.getDeltaMovement();
//            cart.move(MoverType.SELF, new Vec3(Mth.clamp(d24 * vec3d1.x, -d25, d25), 0.0D, Mth.clamp(d24 * vec3d1.z, -d25, d25)));
//
//            ci.cancel();
//        }
//    }
}
