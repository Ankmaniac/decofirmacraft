package com.ankmaniac.decofirmacraft;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.item.DFCCreativeTabs;
import com.ankmaniac.decofirmacraft.common.item.DFCItems;
import com.ankmaniac.decofirmacraft.config.DFCConfig;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.*;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

import net.minecraft.resources.ResourceLocation;

@Mod(DecoFirmaCraft.MOD_ID)
public final class DecoFirmaCraft {

	public static final Logger LOG = LogUtils.getLogger();
	public static final String MOD_ID = "dfc";
	public static final String MOD_NAME = "DecoFirmaCraft";

	public DecoFirmaCraft(final ModContainer modContainer, final IEventBus modBus, final Dist dist) {

		// You likely don't want all of these.
		modContainer.registerConfig(Type.COMMON, DFCConfig.COMMON.spec());
		modContainer.registerConfig(Type.CLIENT, DFCConfig.CLIENT.spec());
		modContainer.registerConfig(Type.SERVER, DFCConfig.SERVER.spec());
		modContainer.registerConfig(Type.STARTUP, DFCConfig.STARTUP.spec());

		modBus.register(DecoFirmaCraft.class);
		DFCItems.ITEMS.register(modBus);
		DFCBlocks.BLOCKS.register(modBus);
		DFCBlockEntities.BLOCK_ENTITIES.register(modBus);
		DFCCreativeTabs.CREATIVE_TABS.register(modBus);

		DecoFirmaCraftForgeEvents.init(NeoForge.EVENT_BUS);

		if (FMLEnvironment.dist == Dist.CLIENT)
		{
			ClientEventHandler.init(modContainer, modBus);
		}
	}

	@SubscribeEvent
	private static void onCreativeTabBuild(final BuildCreativeModeTabContentsEvent event) {
	}

	/**
	 * Shorthand for {@code ResourceLocation.fromNamespaceAndPath(MOD_ID, path)}
	 */
	public static ResourceLocation location(final String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	/**
	 * Helper for creating modid prepended lang keys
	 */
	public static String lang(final String langKey) {
		return MOD_ID + "." + langKey;
	}
}