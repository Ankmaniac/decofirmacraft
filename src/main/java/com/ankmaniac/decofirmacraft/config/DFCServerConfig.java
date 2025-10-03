package com.ankmaniac.decofirmacraft.config;


import java.util.function.Function;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.*;

public class DFCServerConfig
{
    public final ForgeConfigSpec.IntValue gobletCapacity;

    DFCServerConfig(Builder innerBuilder)
    {
        Function<String, Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.server." + name);

        innerBuilder.push("general");

        gobletCapacity = builder.apply("gobletCapacity")
            .comment("Amount of mb a goblet can hold. Default is 100.")
            .defineInRange("gobletCapacity",100, 1, Integer.MAX_VALUE);
        innerBuilder.pop();
    }

}
