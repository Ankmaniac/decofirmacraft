package com.ankmaniac.decofirmacraft.config;

import java.util.function.Function;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import net.dries007.tfc.util.Helpers;

public class DFCConfig
{
    public static final DFCServerConfig SERVER = register(ModConfig.Type.SERVER, DFCServerConfig::new);

    // Although this method is empty, we need to call it to ensure the config actually gets initialized
    public static void init() {}

    private static <C> C register(ModConfig.Type type, Function<ForgeConfigSpec.Builder, C> factory)
    {
        Pair<C, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(factory);
        if(!Helpers.BOOTSTRAP_ENVIRONMENT) ModLoadingContext.get().registerConfig(type, specPair.getRight());
        return specPair.getLeft();
    }
}
