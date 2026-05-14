package com.ankmaniac.decofirmacraft;

import com.ankmaniac.decofirmacraft.common.player.DFCPlayerInfo;
//import com.ankmaniac.decofirmacraft.util.tracker.DFCWorldTracker;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.TFCAttachments;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class DFCAttachments
{
    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DecoFirmaCraft.MOD_ID);

//    public static final Id<DFCWorldTracker> DFC_WORLD_TRACKER = register("world", () -> AttachmentType.builder(
//                    holder -> new DFCWorldTracker((Level) holder)).build());

    public static final Id<DFCPlayerInfo> PLAYER_INFO = register("player_info", () ->
            AttachmentType.serializable(DFCPlayerInfo::new).build());

    // first 8 are for containers, next 8 are for accessories
    public static final Id<ItemStackHandler> EXTRA_PLAYER_SLOTS = register("extra_player_slots", () ->
            AttachmentType.serializable(() -> new ItemStackHandler(16)).build());

    private static <T> Id<T> register(String name, Supplier<AttachmentType<T>> type)
    {
        return new Id<>(TYPES.register(name, type));
    }

    public record Id<T>(DeferredHolder<AttachmentType<?>, AttachmentType<T>> holder)
            implements RegistryHolder<AttachmentType<?>, AttachmentType<T>> {}
}
