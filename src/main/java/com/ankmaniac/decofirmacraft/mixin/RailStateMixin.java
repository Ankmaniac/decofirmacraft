package com.ankmaniac.decofirmacraft.mixin;

import com.ankmaniac.decofirmacraft.common.block.rock.RockRailBlock;
import com.ankmaniac.decofirmacraft.util.DFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RailState.class)
public abstract class RailStateMixin
{
    @Shadow
    private Level level;

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
    boolean redirect$isRail(Level level, BlockPos pos)
    {
        if(level.getBlockState(pos).is(DFCTags.Blocks.RAIL_BLOCKS) && level.getBlockState(pos).getBlock() instanceof RockRailBlock)
        {
            return false;
        }
        else if(level.getBlockState(pos.below()).is(DFCTags.Blocks.RAIL_BLOCKS) && level.getBlockState(pos.below()).getBlock() instanceof RockRailBlock)
        {
            return true;
        }

        return BaseRailBlock.isRail(this.level, pos);
    }

    @Redirect(method = "connectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BaseRailBlock;isRail(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
    boolean redirect$connectTo(Level level, BlockPos pos)
    {
        if(level.getBlockState(pos).is(DFCTags.Blocks.RAIL_BLOCKS) && level.getBlockState(pos).getBlock() instanceof RockRailBlock)
        {
            return false;
        }
        else if(level.getBlockState(pos.below()).is(DFCTags.Blocks.RAIL_BLOCKS) && level.getBlockState(pos.below()).getBlock() instanceof RockRailBlock)
        {
            return true;
        }

        return BaseRailBlock.isRail(this.level, pos);
    }

    @Redirect(method = "connectTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BaseRailBlock;isValidRailShape(Lnet/minecraft/world/level/block/state/properties/RailShape;)Z"))
    private boolean redirect$isValidRailShape(BaseRailBlock block, RailShape shape)
    {
        RailState railState = (RailState)(Object)this;

        RailShape current = railState.getState().getValue(block.getShapeProperty());

        if (railState.getState().is(DFCTags.Blocks.RAIL_STAIRS_BLOCKS))
        {
            return shape == current;
        }

        return block.isValidRailShape(shape);
    }
}
