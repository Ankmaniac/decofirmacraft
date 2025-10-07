package com.ankmaniac.decofirmacraft.common.block.rock;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;
import net.dries007.tfc.common.Lore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum DFCExtendedRock implements DFCHelpers.DFCRockHelpers {
    GRANITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, MapColor.RAW_IRON, false),
    DIORITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, MapColor.METAL, false),
    GABBRO(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, MapColor.COLOR_GRAY, false),
    SHALE(RockDisplayCategory.SEDIMENTARY, MapColor.COLOR_GRAY, false),
    CLAYSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_YELLOW, false),
    LIMESTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_WHITE, false),
    CONGLOMERATE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_LIGHT_GRAY, false),
    DOLOMITE(RockDisplayCategory.SEDIMENTARY, MapColor.COLOR_GRAY, false),
    CHERT(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_ORANGE, false),
    CHALK(RockDisplayCategory.SEDIMENTARY, MapColor.QUARTZ, false),
    TUFF(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_GRAY, false),
    RHYOLITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, MapColor.TERRACOTTA_LIGHT_GRAY, false),
    BASALT(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, MapColor.COLOR_BLACK, false),
    ANDESITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, MapColor.TERRACOTTA_CYAN, false),
    DACITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, MapColor.STONE, false),
    QUARTZITE(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_WHITE, false),
    SLATE(RockDisplayCategory.METAMORPHIC, MapColor.WOOD, false),
    PHYLLITE(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_LIGHT_BLUE, false),
    SCHIST(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_LIGHT_GREEN, false),
    GNEISS(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_LIGHT_GRAY, false),
    MARBLE(RockDisplayCategory.METAMORPHIC, MapColor.WOOL, false),
    ARKOSE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_RED, true),
    BLUESCHIST(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_BLUE, true),
    FLINT(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_ORANGE, true),
    OBSIDIAN(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, MapColor.COLOR_BLACK, true),
    PERIDOTITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, MapColor.COLOR_LIGHT_GREEN, true),
    SERPENTINE(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_GREEN, true),
    SOAPSTONE(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_WHITE, true),
    TRAVERTINE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_WHITE, true);

    public static final DFCExtendedRock[] VALUES = values();

    private final String serializedName;
    private final RockDisplayCategory category;
    private final MapColor color;
    private final boolean dfcRock;

    DFCExtendedRock(RockDisplayCategory category, MapColor color, boolean dfcRock) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = color;
        this.dfcRock = dfcRock;
    }

    public Item.Properties createItemProperties() {
        return new Item.Properties().component(Lore.TYPE, Lore.ROCK_DISPLAY_CATEGORIES.get(category));
    }

    @Override
    public RockDisplayCategory displayCategory() {
        return category;
    }

    @Override
    public MapColor color() {
        return color;
    }

    public boolean isDFCRock() {
        return dfcRock;
    }

    @Override
    public Supplier<? extends Block> getBlock(Rock.BlockType type) {
        return DFCBlocks.DFC_ROCK_BLOCKS.get(this).get(type);
    }

    @Override
    public Supplier<? extends Block> getAnvil() {
        return DFCBlocks.DFC_ROCK_ANVILS.get(this);
    }

    @Override
    public Supplier<? extends SlabBlock> getSlab(Rock.BlockType type) {
        return DFCBlocks.DFC_ROCK_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> getStair(Rock.BlockType type) {
        return DFCBlocks.DFC_ROCK_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> getWall(Rock.BlockType type) {
        return DFCBlocks.DFC_ROCK_DECORATIONS.get(this).get(type).wall();
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    @Override
    public Supplier<? extends Block> dfcGetBlock(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_TYPES.get(this).get(type);
    }

    @Override
    public Supplier<? extends SlabBlock> dfcGetSlab(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_TYPE_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> dfcGetStair(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_TYPE_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> dfcGetWall(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_TYPE_DECORATIONS.get(this).get(type).wall();
    }

    public enum DFCRockBlockType implements StringRepresentable {
        PILLAR((rock, self) -> new RotatedPillarBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false),
        ROAD((rock, self) -> new RoadBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false),
        TILE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        OUTLINED((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false),
        POLISHED((rock, self) -> new PolishedBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false),
        SMALL_BRICKS((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        LARGE_BRICKS((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false),
        COLUMN((rock, self) -> new ColumnBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false),
        RAIL((rock, self) -> new RailBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false);

        public static final DFCRockBlockType[] VALUES = DFCRockBlockType.values();

        public static DFCRockBlockType valueOf(int i) {
            return i >= 0 && i < VALUES.length ? VALUES[i] : PILLAR;
        }

        private static BlockBehaviour.Properties properties(RegistryRock rock) {
            return BlockBehaviour.Properties.of()
                    .mapColor(rock.color())
                    .sound(SoundType.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM);
        }

        private final boolean variants;
        private final BiFunction<RegistryRock, DFCRockBlockType, Block> blockFactory;
        private final String serializedName;

        DFCRockBlockType(BiFunction<RegistryRock, DFCRockBlockType, Block> blockFactory, boolean variants) {
            this.blockFactory = blockFactory;
            this.variants = variants;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        public boolean hasVariants() {
            return variants;
        }

        public Block create(RegistryRock rock) {
            return blockFactory.apply(rock, this);
        }

        public SlabBlock dfcCreateSlab(DFCHelpers.DFCRockHelpers rock) {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            return new SlabBlock(properties);
        }

        public StairBlock dfcCreateStairs(DFCHelpers.DFCRockHelpers rock) {
            final Supplier<BlockState> state = () -> rock.dfcGetBlock(this).get().defaultBlockState();
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            return new StairBlock(state.get(), properties);
        }

        public WallBlock dfcCreateWall(DFCHelpers.DFCRockHelpers rock) {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            return new WallBlock(properties);
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }
    }
}