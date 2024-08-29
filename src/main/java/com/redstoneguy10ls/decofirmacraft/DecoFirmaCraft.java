package com.redstoneguy10ls.decofirmacraft;

import com.mojang.logging.LogUtils;
import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import com.redstoneguy10ls.decofirmacraft.common.items.DFCItems;
import com.redstoneguy10ls.decofirmacraft.common.items.DFCTabs;
import com.redstoneguy10ls.decofirmacraft.world.settings.DFCRockSettings;
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
        DFCItems.ITEMS.register(bus);
        DFCTabs.CREATIVE_TABS.register(bus);

    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DFCRockSettings.registerDFCRocks();
    }
}
