package com.ankmaniac.decofirmacraft.common.block;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public interface IContinuouslyInteractable
{
    void startInteracting(Player player);

    void stopInteracting(Player player);

    boolean canInteract(LocalPlayer player);
}
