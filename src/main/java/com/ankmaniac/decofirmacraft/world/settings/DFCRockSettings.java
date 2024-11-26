package com.ankmaniac.decofirmacraft.world.settings;

import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blocks.rock.DFCRock;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Optional;

import static net.dries007.tfc.world.settings.RockSettings.register;

public class DFCRockSettings{

    public static void registerDFCRocks()
    {
        for (DFCRock rock : DFCRock.values())
        {
            final ResourceLocation id = DFCHelpers.identifier(rock.getSerializedName());
            final Map<Rock.BlockType, RegistryObject<Block>> blocks = DFCBlocks.CUSTOM_ROCK_TYPES.get(rock);

            register(id, new RockSettings(
                    blocks.get(Rock.BlockType.RAW).get(),
                    blocks.get(Rock.BlockType.HARDENED).get(),
                    blocks.get(Rock.BlockType.GRAVEL).get(),
                    blocks.get(Rock.BlockType.COBBLE).get(),
                    TFCBlocks.SAND.get(rock.getSandType()).get(),
                    TFCBlocks.SANDSTONE.get(rock.getSandType()).get(SandstoneBlockType.RAW).get(),
                    Optional.of(blocks.get(Rock.BlockType.SPIKE).get()),
                    Optional.of(blocks.get(Rock.BlockType.LOOSE).get()),
                    Optional.of(blocks.get(Rock.BlockType.MOSSY_LOOSE).get())
            ));
        }
    }
}
