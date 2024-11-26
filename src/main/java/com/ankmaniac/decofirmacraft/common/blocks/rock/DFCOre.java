package com.ankmaniac.decofirmacraft.common.blocks.rock;

import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public enum DFCOre {
    BAUXITE(true),
    GALENA(true),
    NATIVE_ALUMINUM(true),
    NATIVE_PLATINUM(true),
    ASBESTOS(false);

    private final boolean graded;

    DFCOre(boolean graded)
    {
        this.graded = graded;
    }

    public boolean isGraded()
    {
        return graded;
    }

    public Block create(RegistryRock rock)
    {
        final BlockBehaviour.Properties properties = Block.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops();
        return new Block(properties);
    }
}
