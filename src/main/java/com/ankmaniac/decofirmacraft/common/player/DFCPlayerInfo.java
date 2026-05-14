package com.ankmaniac.decofirmacraft.common.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class DFCPlayerInfo implements INBTSerializable<CompoundTag>
{
    @Setter
    @Getter
    private boolean fullCraftingEnabled;


    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("fullCrafting", fullCraftingEnabled);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag)
    {
        fullCraftingEnabled = tag.getBoolean("fullCrafting");
    }
}
