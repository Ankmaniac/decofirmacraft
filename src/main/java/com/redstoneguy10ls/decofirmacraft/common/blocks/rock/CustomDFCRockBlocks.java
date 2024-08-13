package com.redstoneguy10ls.decofirmacraft.common.blocks.rock;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum CustomDFCRockBlocks implements StringRepresentable {
    PILLAR((rock, self) -> new RotatedPillarBlock(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false),
    ROAD((rock, self) -> new RoadBlock(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),true),
    TILE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),true),
    THIN_OUTLINE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false),
    THICK_OUTLINE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false),
    COLUMN((rock, self) -> new ColumnBlock(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false);
    public static final CustomDFCRockBlocks[] VALUES = values();

    public static CustomDFCRockBlocks valueOf(int i){return i >= 0 && i < VALUES.length ? VALUES[i] : PILLAR;}

    private static BlockBehaviour.Properties properties(DFCRock rock)
    {
        return BlockBehaviour.Properties.of().mapColor(rock.color()).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM);
    }
    private final boolean variants;
    private final BiFunction<DFCRock, CustomDFCRockBlocks, Block> blockFactory;
    private final String serializedName;

    CustomDFCRockBlocks(BiFunction<DFCRock, CustomDFCRockBlocks, Block> blockFactory, boolean variants)
    {
        this.blockFactory = blockFactory;
        this.variants = variants;
        this.serializedName = name().toLowerCase(Locale.ROOT);
    }

    public boolean hasVariants()
    {
        return variants;
    }

    public Block create(DFCRock rock)
    {
        return blockFactory.apply(rock, this);
    }

    public SlabBlock createSlab(DFCRock rock)
    {
        final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
        return new SlabBlock(properties);
    }
    public Supplier<? extends Block> getBlocks(DFCRock rock, CustomDFCRockBlocks type)
    {
        return DFCBlocks.CUSTOM_DFC_ROCK_BLOCKS.get(rock).get(type);
    }

    public StairBlock createStairs(DFCRock rock)
    {
        final Supplier<BlockState> state = () -> getBlocks(rock,this).get().defaultBlockState();
        final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
        return new StairBlock(state, properties);
    }

    public WallBlock createWall(DFCRock rock)
    {
        final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
        return new WallBlock(properties);
    }
    @Override
    public String getSerializedName()
    {
        return serializedName;
    }
}
