package com.ankmaniac.decofirmacraft;

import com.mojang.logging.LogUtils;
import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blocks.DFCFluids;
import com.ankmaniac.decofirmacraft.common.items.DFCItems;
import com.ankmaniac.decofirmacraft.common.items.DFCTabs;
import com.ankmaniac.decofirmacraft.common.recipes.DFCRecipeSerializers;
import com.ankmaniac.decofirmacraft.common.recipes.DFCRecipeTypes;
import com.ankmaniac.decofirmacraft.world.settings.DFCRockSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DecoFirmaCraft.MOD_ID)
public class DecoFirmaCraft {
    public static final String MOD_ID = "dfc";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DecoFirmaCraft()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        DFCBlocks.BLOCKS.register(bus);
        DFCFluids.FLUIDS.register(bus);
        DFCItems.ITEMS.register(bus);
        DFCTabs.CREATIVE_TABS.register(bus);
        DFCRecipeTypes.RECIPE_TYPES.register(bus);
        DFCRecipeSerializers.RECIPE_SERIALIZERS.register(bus);

    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DFCRockSettings.registerDFCRocks();
    }
}
