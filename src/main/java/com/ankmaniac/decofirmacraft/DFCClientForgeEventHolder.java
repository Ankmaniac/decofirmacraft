package com.ankmaniac.decofirmacraft;

import com.ankmaniac.decofirmacraft.client.screen.button.DFCPlayerInventoryTabButton;
import com.ankmaniac.decofirmacraft.common.DecoFirmaCraftDataMaps;
import com.ankmaniac.decofirmacraft.common.block.ChiseledBlock;
import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.block.IContinuouslyInteractable;
import com.ankmaniac.decofirmacraft.common.block.metal.CandleholderBlock;
import com.ankmaniac.decofirmacraft.common.blockentities.CandleholderBlockEntity;
import com.ankmaniac.decofirmacraft.common.blockentities.ChiseledBlockEntity;
import com.ankmaniac.decofirmacraft.common.blockentities.ChiseledBlockEntity.ChiseledBlockMaps;
import com.ankmaniac.decofirmacraft.common.player.DFCChiselMode;
import com.ankmaniac.decofirmacraft.network.DFCSwitchInventoryTabPacket;
import com.ankmaniac.decofirmacraft.network.StartContinuousUsePacket;
import com.ankmaniac.decofirmacraft.network.StopContinuousUsePacket;
import com.ankmaniac.decofirmacraft.util.DFCTags;
import com.ankmaniac.decofirmacraft.util.DynamicTextureDataMap;
import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.client.screen.button.PlayerInventoryTabButton;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.player.IPlayerInfo;
import net.dries007.tfc.network.SwitchInventoryTabPacket;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

public final class DFCClientForgeEventHolder {

	public static void init() {
		final IEventBus bus = NeoForge.EVENT_BUS;

		bus.register(DFCClientForgeEventHolder.class);
		bus.addListener(DFCClientForgeEventHolder::onScreenOpening);
		bus.addListener(DFCClientForgeEventHolder::onClientPlayerLoggedOut);
		bus.addListener(DFCClientForgeEventHolder::onClientTick);
	}

	@SubscribeEvent
	public static void onScreenOpening(ScreenEvent.Opening event)
	{
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player != null)
		{
			if (event.getNewScreen() instanceof InventoryScreen && !player.hasInfiniteMaterials())
			{
				event.setCanceled(true);

				PacketDistributor.sendToServer(new DFCSwitchInventoryTabPacket(DFCPlayerInventoryTabButton.Tab.INVENTORY));
			}
		}
	}

	@SubscribeEvent
	public static void onClientPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event)
	{
		if (currentHit != null)
		{
			PacketDistributor.sendToServer(new StopContinuousUsePacket(currentHit));
			currentHit = null;
		}
	}

	@Nullable
	private static BlockPos currentHit = null;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event)
	{
		Minecraft mc = Minecraft.getInstance();
		@Nullable
		final Level level = mc.level;

		if (level != null)
		{
			LocalPlayer player = mc.player;


			if (player != null)
			{
//				if (mc.screen == null && mc.options.keyInventory.consumeClick() && !player.hasInfiniteMaterials())
//				{
//					System.out.println("INVENTORY");
//					player.closeContainer();
//					PacketDistributor.sendToServer(new DFCSwitchInventoryTabPacket(DFCPlayerInventoryTabButton.Tab.INVENTORY));
//				}

				boolean using = mc.options.keyUse.isDown();

				if (using)
				{
					HitResult hit = mc.hitResult;

					if (hit instanceof BlockHitResult blockHit)
					{
						BlockPos hitPos = blockHit.getBlockPos();

						if (level.getBlockEntity(hitPos) instanceof IContinuouslyInteractable interactable)
						{
							if (interactable.canInteract(player))
							{
								if (currentHit == null)
								{
									PacketDistributor.sendToServer(new StartContinuousUsePacket(hitPos));
									currentHit = hitPos;
								}
								if (currentHit != null && !currentHit.equals(hitPos))
								{
									PacketDistributor.sendToServer(new StopContinuousUsePacket(currentHit));

									currentHit = hitPos;
									PacketDistributor.sendToServer(new StartContinuousUsePacket(currentHit));
								}
								return;
							}
						}
					}
				}
				if (currentHit != null)
				{
					PacketDistributor.sendToServer(new StopContinuousUsePacket(currentHit));
					currentHit = null;
				}
			}
		}
	}
}