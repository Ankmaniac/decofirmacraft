package com.ankmaniac.decofirmacraft.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IDFCRail
{
    //Both of these may be made dynamically to say, add optional brakes by dramatically increasing
    //the friction coefficient depending on powered state
    float getKineticFrictionCoefficient();

    float getStaticFrictionCoefficient();

    //overwrite if your rail grants the cart any boosts
    //is added to the cart every tick as blocks of movement (1 being a block)
    //try to increase friction coefficient instead of a flat force reduction to simulate brakes
    default float boostCart()
    {
        return 0f;
    }

    BlockPos nextRailPos(boolean negative, BlockPos currentRailPos);

    double getLength();
}
