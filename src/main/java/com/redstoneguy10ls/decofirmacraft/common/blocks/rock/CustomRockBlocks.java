package com.redstoneguy10ls.decofirmacraft.common.blocks.rock;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum CustomRockBlocks implements StringRepresentable {
    PILLAR((rock, self) -> new RotatedPillarBlock(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false),
    ROAD((rock, self) -> new RoadBlock(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),true),
    TILE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),true),
    THIN_OUTLINE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false),
    THICK_OUTLINE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f),10).requiresCorrectToolForDrops()),false),;
    public static final CustomRockBlocks[] VALUES = values();

    public static CustomRockBlocks valueOf(int i){return i >= 0 && i < VALUES.length ? VALUES[i] : PILLAR;}

    private static BlockBehaviour.Properties properties(RegistryRock rock)
    {
        return BlockBehaviour.Properties.of().mapColor(rock.color()).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM);
    }
    private final boolean variants;
    private final BiFunction<RegistryRock, CustomRockBlocks, Block> blockFactory;
    private final String serializedName;

    CustomRockBlocks(BiFunction<RegistryRock, CustomRockBlocks, Block> blockFactory, boolean variants)
    {
        this.blockFactory = blockFactory;
        this.variants = variants;
        this.serializedName = name().toLowerCase(Locale.ROOT);
    }

    public boolean hasVariants()
    {
        return variants;
    }

    public Block create(RegistryRock rock)
    {
        return blockFactory.apply(rock, this);
    }

    public SlabBlock createSlab(RegistryRock rock)
    {
        final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
        return new SlabBlock(properties);
    }
    public Supplier<? extends Block> getBlocks(RegistryRock rock, CustomRockBlocks type)
    {
        return DFCBlocks.CUSTOM_ROCK_BLOCKS.get(rock).get(type);
    }

    public StairBlock createStairs(RegistryRock rock)
    {
        final Supplier<BlockState> state = () -> getBlocks(rock,this).get().defaultBlockState();
        final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
        return new StairBlock(state, properties);
    }

    public WallBlock createWall(RegistryRock rock)
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
