package com.ankmaniac.decofirmacraft.util;

import com.ankmaniac.decofirmacraft.common.block.metal.DFCExtendedMetal;
import com.ankmaniac.decofirmacraft.common.block.rock.DFCExtendedRock;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCHelpers {
    public interface DFCRockHelpers extends StringRepresentable, RegistryRock {

        /**
         * @return A custom material color for this rock type, which affects map color and raw rock unsupported particle colors.
         */
        Supplier<? extends Block> dfcGetBlock(DFCExtendedRock.DFCRockBlockType type);

        Supplier<? extends SlabBlock> dfcGetSlab(DFCExtendedRock.DFCRockBlockType type);

        Supplier<? extends StairBlock> dfcGetStair(DFCExtendedRock.DFCRockBlockType type);

        Supplier<? extends WallBlock> dfcGetWall(DFCExtendedRock.DFCRockBlockType type);
    }

    public interface DFCMetalHelpers extends StringRepresentable{

        Block getBlock(DFCExtendedMetal.DFCMetalBlockType type);

        MapColor mapColor();

        Rarity rarity();

        default boolean weatheredParts()
        {
            return weatheringResistance() != -1;
        }

        float weatheringResistance();
    }

    public static ResourceLocation identifier(String name)
    {
        return resourceLocation(MOD_ID, name);
    }

    public static ResourceLocation identifierMC(String name)
    {
        return resourceLocation("minecraft", name);
    }

    public static ResourceLocation resourceLocation(String name)
    {
        return ResourceLocation.parse(name);
    }

    public static ResourceLocation resourceLocation(String domain, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(domain, path);
    }

}
