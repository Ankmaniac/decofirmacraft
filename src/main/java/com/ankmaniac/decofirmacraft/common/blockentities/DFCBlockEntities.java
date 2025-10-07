package com.ankmaniac.decofirmacraft.common.blockentities;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.block.metal.DFCExtendedMetal;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);


    public static final Id<GobletBlockEntity> GOBLET = register("goblet", GobletBlockEntity::new,
        DFCBlocks.DFC_DECORATIVE_METAL_BLOCKS.values().stream().filter(map ->
            map.get(DFCExtendedMetal.DFCMetalBlockType.GOBLET) != null)
            .map(map ->
                map.get(DFCExtendedMetal.DFCMetalBlockType.GOBLET)));


    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block));
    }

    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks));
    }

    public record Id<T extends BlockEntity>(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder)
            implements RegistryHolder<BlockEntityType<?>, BlockEntityType<T>> {}
}
