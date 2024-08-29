package com.redstoneguy10ls.decofirmacraft.common.blocks.rock;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.function.Supplier;

public enum DFCRock implements RegistryRock {
    TRAVERTINE(RockDisplayCategory.SEDIMENTARY, MapColor.STONE, SandBlockType.WHITE),
    SERPENTINE(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_GREEN, SandBlockType.GREEN),
    ARKOSE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_ORANGE, SandBlockType.RED),
    BLUESCHIST(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_BLUE, SandBlockType.GREEN),
    TUFF(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, MapColor.TERRACOTTA_WHITE, SandBlockType.BROWN);
    public static final DFCRock[] VALUES = values();
    private final String serializedName;
    private final RockDisplayCategory category;
    private final MapColor color;
    private final SandBlockType sandType;

    DFCRock(RockDisplayCategory category, MapColor color, SandBlockType sandType)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = color;
        this.sandType = sandType;
    }
    public SandBlockType getSandType()
    {
        return sandType;
    }

    @Override
    public RockDisplayCategory displayCategory()
    {
        return category;
    }

    @Override
    public MapColor color()
    {
        return color;
    }

    @Override
    public Supplier<? extends Block> getBlock(Rock.BlockType type)
    {
        return DFCBlocks.CUSTOM_ROCK_TYPES.get(this).get(type);
    }
    @Override
    public Supplier<? extends Block> getAnvil()
    {
        return DFCBlocks.DFC_ROCK_ANVILS.get(this);
    }

    @Override
    public Supplier<? extends SlabBlock> getSlab(Rock.BlockType type)
    {
        return DFCBlocks.CUSTOM_DFC_ROCK_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> getStair(Rock.BlockType type)
    {
        return DFCBlocks.CUSTOM_DFC_ROCK_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> getWall(Rock.BlockType type)
    {
        return DFCBlocks.CUSTOM_DFC_ROCK_DECORATIONS.get(this).get(type).wall();
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }



}
