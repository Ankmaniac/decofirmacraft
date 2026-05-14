package com.ankmaniac.decofirmacraft.common.block.rock;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;
import net.dries007.tfc.common.Lore;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.*;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum DFCExtendedRock implements DFCHelpers.DFCRockRegistry {
    GRANITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, MapColor.RAW_IRON, RockType.TFC),
    DIORITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, MapColor.METAL, RockType.TFC),
    GABBRO(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, MapColor.COLOR_GRAY, RockType.TFC),
    SHALE(RockDisplayCategory.SEDIMENTARY, MapColor.COLOR_GRAY, RockType.TFC),
    CLAYSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_YELLOW, RockType.TFC),
    LIMESTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_WHITE, RockType.TFC),
    CONGLOMERATE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_LIGHT_GRAY, RockType.TFC),
    DOLOMITE(RockDisplayCategory.SEDIMENTARY, MapColor.COLOR_GRAY, RockType.TFC),
    CHERT(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_ORANGE, RockType.TFC),
    CHALK(RockDisplayCategory.SEDIMENTARY, MapColor.QUARTZ, RockType.TFC),
    TUFF(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_GRAY, RockType.TFC),
    RHYOLITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, MapColor.TERRACOTTA_LIGHT_GRAY, RockType.TFC),
    BASALT(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, MapColor.COLOR_BLACK, RockType.TFC),
    ANDESITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, MapColor.TERRACOTTA_CYAN, RockType.TFC),
    DACITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, MapColor.STONE, RockType.TFC),
    QUARTZITE(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_WHITE, RockType.TFC),
    SLATE(RockDisplayCategory.METAMORPHIC, MapColor.WOOD, RockType.TFC),
    PHYLLITE(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_LIGHT_BLUE, RockType.TFC),
    SCHIST(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_LIGHT_GREEN, RockType.TFC),
    GNEISS(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_LIGHT_GRAY, RockType.TFC),
    MARBLE(RockDisplayCategory.METAMORPHIC, MapColor.WOOL, RockType.TFC),
    BROWN_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.DIRT, RockType.SANDSTONE),
    WHITE_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.QUARTZ, RockType.SANDSTONE),
    BLACK_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_BLACK, RockType.SANDSTONE),
    RED_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_RED, RockType.SANDSTONE),
    YELLOW_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.SAND, RockType.SANDSTONE),
    GREEN_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.COLOR_GREEN, RockType.SANDSTONE),
    PINK_SANDSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_PINK, RockType.SANDSTONE),
    ARKOSE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_RED, RockType.DFC),
    BLUESCHIST(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_BLUE, RockType.DFC),
    FLINT(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_ORANGE, RockType.DFC),
    IGNIMBRITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, MapColor.COLOR_LIGHT_GRAY, RockType.DFC),
    OBSIDIAN(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, MapColor.COLOR_BLACK, RockType.DFC),
    PERIDOTITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, MapColor.COLOR_LIGHT_GREEN, RockType.DFC),
    PUMICE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_WHITE, RockType.DFC),
    SERPENTINE(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_GREEN, RockType.DFC),
    SOAPSTONE(RockDisplayCategory.METAMORPHIC, MapColor.TERRACOTTA_WHITE, RockType.DFC),
    TRAVERTINE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_WHITE, RockType.DFC);
    public static final DFCExtendedRock[] VALUES = values();

    private final String serializedName;
    private final RockDisplayCategory category;
    private final MapColor color;
    private final RockType rockType;

    DFCExtendedRock(RockDisplayCategory category, MapColor color, RockType rockType) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = color;
        this.rockType = rockType;
    }

    public Item.Properties createItemProperties() {
        return new Item.Properties().component(Lore.TYPE, Lore.ROCK_DISPLAY_CATEGORIES.get(category));
    }

    public boolean isDFCRock()
    {
        return rockType == RockType.DFC;
    }

    @Override
    public RockDisplayCategory displayCategory() {
        return category;
    }

    @Override
    public MapColor color() {
        return color;
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
        return DFCBlocks.DFC_ROCK_BLOCKS.get(this).get(type);
    }

    @Override
    public Supplier<? extends SlabBlock> dfcGetSlab(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> dfcGetStair(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> dfcGetWall(DFCRockBlockType type) {
        return DFCBlocks.DFC_ROCK_DECORATIONS.get(this).get(type).wall();
    }

    public enum DFCRockBlockType implements StringRepresentable {
        RAW((rock, self) -> RockConvertableToAnvilBlock.createForIgneousOnly(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops(), rock, false), true, RockType.TFC_BLOCK),
        HARDENED((rock, self) -> RockConvertableToAnvilBlock.createForIgneousOnly(properties(rock).strength(rock.category().hardness(8f), 10).requiresCorrectToolForDrops(), rock, true), false, RockType.SANDSTONE_BLOCK),
        SMOOTH((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true, RockType.TFC_BLOCK),
        COBBLE((rock, self) -> new MossGrowingBlock(properties(rock).strength(rock.category().hardness(5.5f), 10).requiresCorrectToolForDrops(), rock.dfcGetBlock(Objects.requireNonNull(self.mossy()))), true, RockType.SANDSTONE_BLOCK),
        BRICKS((rock, self) -> new MossGrowingBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops(), rock.dfcGetBlock(Objects.requireNonNull(self.mossy()))), true, RockType.SANDSTONE_BLOCK),
        GRAVEL((rock, self) -> new Block(Block.Properties.of().mapColor(rock.color()).sound(SoundType.GRAVEL).instrument(NoteBlockInstrument.SNARE).strength(rock.category().hardness(2.0f))), false, RockType.SANDSTONE_BLOCK),
        SPIKE((rock, self) -> new RockSpikeBlock(properties(rock).strength(rock.category().hardness(4f), 10).requiresCorrectToolForDrops().lightLevel(TFCBlocks.lavaLoggedBlockEmission())), false, RockType.SANDSTONE_BLOCK),
        CRACKED_BRICKS((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true, RockType.SANDSTONE_BLOCK),
        MOSSY_BRICKS((rock, self) -> new MossSpreadingBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true, RockType.SANDSTONE_BLOCK),
        MOSSY_COBBLE((rock, self) -> new MossSpreadingBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true, RockType.SANDSTONE_BLOCK),
        CHISELED((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(8f), 10).requiresCorrectToolForDrops()), false, RockType.SANDSTONE_BLOCK),
        LOOSE((rock, self) -> new LooseRockBlock(properties(rock).strength(0.05f, 0.0f).noCollission()), false, RockType.SANDSTONE_BLOCK),
        MOSSY_LOOSE((rock, self) -> new LooseRockBlock(properties(rock).strength(0.05f, 0.0f).noCollission()), false, RockType.SANDSTONE_BLOCK),
        PRESSURE_PLATE((rock, self) -> new PressurePlateBlock(BlockSetType.STONE, properties(rock).requiresCorrectToolForDrops().noCollission().strength(0.5f)), false, RockType.SANDSTONE_BLOCK),
        BUTTON((rock, self) -> new ButtonBlock(BlockSetType.STONE, 20, properties(rock).noCollission().strength(0.5f)), false, RockType.SANDSTONE_BLOCK),
        AQUEDUCT((rock, self) -> new AqueductBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops().lightLevel(TFCBlocks.lavaLoggedBlockEmission())), false, RockType.SANDSTONE_BLOCK),
        PILLAR((rock, self) -> new RotatedPillarBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        ROAD((rock, self) -> new RoadBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        TILE((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true, RockType.DFC_BLOCK),
        OUTLINED((rock, self) -> new SwappableCTMBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        POLISHED((rock, self) -> new SwappableCTMBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        SMALL_BRICKS((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true, RockType.DFC_BLOCK),
        LARGE_BRICKS((rock, self) -> new Block(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        COLUMN((rock, self) -> new ColumnBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        RAIL((rock, self) -> new RailBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        RAIL_BLOCK((rock, self) -> new RockRailBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK),
        RAIL_STAIRS_BLOCK((rock, self) -> new RockRailStairsBlock(properties(rock).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), false, RockType.DFC_BLOCK);

        public static final DFCRockBlockType[] VALUES = DFCRockBlockType.values();

        public static DFCRockBlockType valueOf(int i) {
            return i >= 0 && i < VALUES.length ? VALUES[i] : PILLAR;
        }

        private static BlockBehaviour.Properties properties(DFCHelpers.DFCRockRegistry rock) {
            return BlockBehaviour.Properties.of()
                    .mapColor(rock.color())
                    .sound(SoundType.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM);
        }

        private final boolean variants;
        private final BiFunction<DFCHelpers.DFCRockRegistry, DFCRockBlockType, Block> blockFactory;
        private final String serializedName;
        private final RockType type;

        DFCRockBlockType(BiFunction<DFCHelpers.DFCRockRegistry, DFCRockBlockType, Block> blockFactory, boolean variants, RockType type) {
            this.blockFactory = blockFactory;
            this.variants = variants;
            this.serializedName = name().toLowerCase(Locale.ROOT);
            this.type = type;
        }

        public boolean hasVariants() {
            return variants;
        }
        
        public boolean has(DFCExtendedRock rock){
            return type.hasRock(rock.rockType);
        }

        public Block create(DFCHelpers.DFCRockRegistry rock) {
            return blockFactory.apply(rock, this);
        }

        public SlabBlock dfcCreateSlab(DFCHelpers.DFCRockRegistry rock) {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final DFCRockBlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingSlabBlock(properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingSlabBlock(properties, rock.dfcGetSlab(mossy));
            }
            return new SlabBlock(properties);
        }

        public StairBlock dfcCreateStairs(DFCHelpers.DFCRockRegistry rock) {
            final Supplier<BlockState> state = () -> rock.dfcGetBlock(this).get().defaultBlockState();
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final DFCRockBlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingStairBlock(state, properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingStairsBlock(state, properties, rock.dfcGetStair(mossy));
            }
            return new StairBlock(state.get(), properties);
        }

        public WallBlock dfcCreateWall(DFCHelpers.DFCRockRegistry rock) {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final DFCRockBlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingWallBlock(properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingWallBlock(properties, rock.dfcGetWall(mossy));
            }
            return new WallBlock(properties);
        }

        @Nullable
        private DFCRockBlockType mossy()
        {
            return switch (this)
            {
                case COBBLE, MOSSY_COBBLE -> MOSSY_COBBLE;
                case BRICKS, MOSSY_BRICKS -> MOSSY_BRICKS;
                default -> null;
            };
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }
    }

    enum RockType
    {
        TFC, BENEATH, SANDSTONE, DFC, TFC_BLOCK, BENEATH_BLOCK, DFC_BLOCK, SANDSTONE_BLOCK;

        boolean hasRock(RockType rock)
        {
            return switch (this)
            {
                case TFC_BLOCK -> rock == DFC;
                case SANDSTONE_BLOCK -> rock.ordinal() >= SANDSTONE.ordinal();
                case DFC_BLOCK -> true;
                default -> throw new AssertionError("Invalid choice for a rock type " + this);
            };
        }
    }
}