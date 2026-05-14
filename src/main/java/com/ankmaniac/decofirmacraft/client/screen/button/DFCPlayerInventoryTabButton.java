package com.ankmaniac.decofirmacraft.client.screen.button;

import io.netty.buffer.ByteBuf;
import net.dries007.tfc.client.screen.button.PlayerInventoryTabButton;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

//sanity screen
//skills screen
//extended clothing/jewelry screen
//extended inventory screen has extra container slots + back slot + larger crafting slots again?
public class DFCPlayerInventoryTabButton
{

    public enum Tab
    {
        INVENTORY;
        public static final DFCPlayerInventoryTabButton.Tab[] VALUES = values();
        public static final StreamCodec<ByteBuf, DFCPlayerInventoryTabButton.Tab> STREAM = ByteBufCodecs.BYTE.map(c -> VALUES[c], c -> (byte) c.ordinal());
    }
}
