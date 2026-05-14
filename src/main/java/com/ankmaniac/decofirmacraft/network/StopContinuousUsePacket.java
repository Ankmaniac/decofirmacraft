package com.ankmaniac.decofirmacraft.network;

import com.ankmaniac.decofirmacraft.common.block.IContinuouslyInteractable;
import io.netty.buffer.ByteBuf;
import net.dries007.tfc.network.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public record StopContinuousUsePacket(BlockPos pos) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<StopContinuousUsePacket> TYPE = DFCPackets.type("stop_continuous_use");
    public static final StreamCodec<ByteBuf, StopContinuousUsePacket> CODEC = BlockPos.STREAM_CODEC.map(StopContinuousUsePacket::new, c -> c.pos);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    void handle(@Nullable ServerPlayer player)
    {
        if (player != null)
        {
            final Level level = player.level();
            if (level.isLoaded(pos) && level.getBlockEntity(pos) instanceof IContinuouslyInteractable continuouslyInteractable)
            {
                continuouslyInteractable.stopInteracting(player);
            }
        }
    }
}
