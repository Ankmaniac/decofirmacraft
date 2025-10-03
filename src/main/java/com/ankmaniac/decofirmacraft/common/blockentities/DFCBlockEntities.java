package com.ankmaniac.decofirmacraft.common.blockentities;

import java.util.function.Supplier;
import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.util.registry.RegistrationHelpers;
import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.*;

public class DFCBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);


    public static final RegistryObject<BlockEntityType<GobletBlockEntity>> GOBLET = register("goblet", GobletBlockEntity::new, DFCBlocks.GOBLET_BLOCK);

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block);
    }
}
