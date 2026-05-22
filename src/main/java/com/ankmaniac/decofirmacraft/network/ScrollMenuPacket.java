package com.ankmaniac.decofirmacraft.network;

import com.ankmaniac.decofirmacraft.client.screen.button.DFCPlayerInventoryTabButton;
import com.ankmaniac.decofirmacraft.common.container.Scrollable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ScrollMenuPacket(int scrollerId, int offset) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ScrollMenuPacket> TYPE = DFCPackets.type("scroll_menu");
    public static final StreamCodec<ByteBuf, ScrollMenuPacket> CODEC = StreamCodec.composite(ByteBufCodecs.INT, ScrollMenuPacket::scrollerId, ByteBufCodecs.INT, ScrollMenuPacket::offset, ScrollMenuPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    void handle(@Nullable ServerPlayer player)
    {
        if (player != null)
        {
            if (player.containerMenu instanceof Scrollable scrollable)
            {
                scrollable.scroll(scrollerId, offset);
            }
        }
    }
}
