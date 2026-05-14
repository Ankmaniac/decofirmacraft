package com.ankmaniac.decofirmacraft.network;

import com.ankmaniac.decofirmacraft.client.screen.button.DFCPlayerInventoryTabButton;
import com.ankmaniac.decofirmacraft.common.container.DFCContainerProviders;
import io.netty.buffer.ByteBuf;
import net.dries007.tfc.network.PacketHandler;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record DFCSwitchInventoryTabPacket(DFCPlayerInventoryTabButton.Tab tab) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<DFCSwitchInventoryTabPacket> TYPE = DFCPackets.type("switch_inventory_tab");
    public static final StreamCodec<ByteBuf, DFCSwitchInventoryTabPacket> CODEC = DFCPlayerInventoryTabButton.Tab.STREAM.map(DFCSwitchInventoryTabPacket::new, c -> c.tab);

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    void handle(@Nullable ServerPlayer player)
    {
        if (player != null)
        {
            player.doCloseContainer();
            switch (tab)
            {
                case INVENTORY -> player.openMenu(DFCContainerProviders.INVENTORY);
            }
        }
    }
}
