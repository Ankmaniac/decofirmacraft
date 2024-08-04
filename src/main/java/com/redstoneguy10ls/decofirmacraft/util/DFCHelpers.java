package com.redstoneguy10ls.decofirmacraft.util;

import com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft;
import net.minecraft.resources.ResourceLocation;

public class DFCHelpers {
    public static ResourceLocation identifier(String id)
    {
        return new ResourceLocation(DecoFirmaCraft.MOD_ID, id);
    }
}
