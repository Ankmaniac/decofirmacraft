package com.ankmaniac.decofirmacraft.common.block.metal;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.item.metal.GobletItem;
import com.ankmaniac.decofirmacraft.config.DFCConfig;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.*;
import net.dries007.tfc.common.blocks.IWeatheringBlock.Age;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum DFCExtendedMetal implements StringRepresentable, DFCHelpers.DFCMetalHelpers {

    BISMUTH(0xFF486B72, MapColor.TERRACOTTA_GREEN, Rarity.COMMON, -1, PartType.DEFAULT, false),
    BISMUTH_BRONZE(0xFF418E4F, MapColor.TERRACOTTA_BLUE, Rarity.COMMON, -1, PartType.ALL, true),
    BLACK_BRONZE(0xFF3B2636, MapColor.TERRACOTTA_PINK, Rarity.COMMON, -1, PartType.ALL, true),
    BRONZE(0xFF96892E, MapColor.TERRACOTTA_ORANGE, Rarity.COMMON, 0.7f, PartType.ALL_WEATHERING, true),
    BRASS(0xFF7C5E33, MapColor.GOLD, Rarity.COMMON, 0.7f, PartType.DEFAULT_WEATHERING, false),
    COPPER(0xFFB64027, MapColor.COLOR_ORANGE, Rarity.COMMON, 0f, PartType.ALL_WEATHERING, true),
    GOLD(0xFFDCBF1B, MapColor.GOLD, Rarity.COMMON, -1, PartType.DEFAULT, true),
    NICKEL(0xFF4E4E3C, MapColor.STONE, Rarity.COMMON, -1, PartType.DEFAULT, false),
    ROSE_GOLD(0xFFEB7137, MapColor.COLOR_PINK, Rarity.COMMON, -1, PartType.DEFAULT, true),
    SILVER(0xFF949495, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, 0.95f, PartType.DEFAULT_WEATHERING, true),
    TIN(0xFF90A4BB, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.DEFAULT, false),
    ZINC(0xFFBBB9C4, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.DEFAULT, false),
    STERLING_SILVER(0xFFAC927B, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, 0.95f, PartType.DEFAULT_WEATHERING, true),
    WROUGHT_IRON(0xFF989897, MapColor.METAL, Rarity.COMMON, 0f, PartType.ALL_WEATHERING, false),
    CAST_IRON(0xFF989897, MapColor.COLOR_BROWN, Rarity.COMMON, -1, PartType.DEFAULT, false),
    PIG_IRON(0xFF6A595C, MapColor.COLOR_GRAY, Rarity.COMMON, -1, PartType.INGOT, false),
    STEEL(0xFF5F5F5F, MapColor.COLOR_LIGHT_GRAY, Rarity.UNCOMMON, 0.9f, PartType.ALL_WEATHERING, false),
    BLACK_STEEL(0xFF111111, MapColor.COLOR_BLACK, Rarity.RARE, -1, PartType.ALL_WEATHERING, false),
    BLUE_STEEL(0xFF2D5596, MapColor.COLOR_BLUE, Rarity.EPIC, -1, PartType.ALL_WEATHERING, false),
    RED_STEEL(0xFF700503, MapColor.COLOR_RED, Rarity.EPIC, -1, PartType.ALL_WEATHERING, false),
    WEAK_STEEL(0xFF111111, MapColor.COLOR_GRAY, Rarity.COMMON, -1, PartType.INGOT, false),
    WEAK_BLUE_STEEL(0xFF2D5596, MapColor.COLOR_BLUE, Rarity.COMMON, -1, PartType.INGOT, false),
    WEAK_RED_STEEL(0xFF700503, MapColor.COLOR_RED, Rarity.COMMON, -1, PartType.INGOT, false),
    HIGH_CARBON_STEEL(0xFF5F5F5F, MapColor.COLOR_GRAY, Rarity.COMMON, -1, PartType.INGOT, false),
    HIGH_CARBON_BLACK_STEEL(0xFF111111, MapColor.COLOR_BLACK, Rarity.COMMON, -1, PartType.INGOT, false),
    HIGH_CARBON_BLUE_STEEL(0xFF2D5596, MapColor.COLOR_BLUE, Rarity.COMMON, -1, PartType.INGOT, false),
    HIGH_CARBON_RED_STEEL(0xFF700503, MapColor.COLOR_RED, Rarity.COMMON, -1, PartType.INGOT, false),
    UNKNOWN(0xFF2F2B27, MapColor.COLOR_BLACK, Rarity.COMMON, -1, PartType.DEFAULT, false),
    ALUMINUM(0xFFD9D8C5, MapColor.COLOR_LIGHT_GRAY, Rarity.UNCOMMON, -1, PartType.DEFAULT, false),
    ALUMINA(0xFFE3E3DC, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.INGOT, false),
    LEAD(0xFF7E789C, MapColor.COLOR_PURPLE, Rarity.COMMON, -1, PartType.DEFAULT, false),
    PEWTER(0xFFBBBD9F, MapColor.COLOR_YELLOW, Rarity.COMMON, -1, PartType.DEFAULT, true),
    PLATINUM(0xFFDCF2F5, MapColor.COLOR_LIGHT_BLUE, Rarity.COMMON, -1, PartType.DEFAULT, true),
    CHROMIUM(0xFFF5FEFF, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.INGOT, false),
    STAINLESS_STEEL(0xFFD9FCFF, MapColor.COLOR_LIGHT_GRAY, Rarity.RARE, -1, PartType.ALL, false),
    ELECTRUM(0xFCB74A, MapColor.COLOR_YELLOW, Rarity.EPIC, -1, PartType.DEFAULT, false),
    CONSTANTAN(0xEC8068, MapColor.COLOR_ORANGE, Rarity.EPIC, -1, PartType.DEFAULT, false),
    IE_ALUMINUM(0xCCC1BC, MapColor.CLAY, Rarity.COMMON, -1, PartType.DEFAULT, false),
    IE_LEAD(0x433F4D, MapColor.TERRACOTTA_BLUE, Rarity.RARE, -1, PartType.DEFAULT, false),
    URANIUM(0x738A6C, MapColor.TERRACOTTA_GREEN, Rarity.EPIC, -1, PartType.DEFAULT, false);

    private final String serializedName;
    private final PartType partType;
    private final MapColor mapColor;
    private final Rarity rarity;
    private final int color;
    private final float weathering;
    private final boolean decorative;

    DFCExtendedMetal(int color, MapColor mapColor, Rarity rarity, float weathering, PartType partType, boolean decorative)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.rarity = rarity;
        this.mapColor = mapColor;
        this.color = color;
        this.partType = partType;
        this.weathering = weathering;
        this.decorative = decorative;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public int getColor()
    {
        return color;
    }

    @Override
    public Rarity rarity()
    {
        return rarity;
    }

    @Override
    public float weatheringResistance()
    {
        return weathering;
    }

    public boolean defaultParts()
    {
        return partType != PartType.INGOT;
    }

    public boolean allParts()
    {
        return partType == PartType.ALL || partType == PartType.ALL_WEATHERING;
    }

    public boolean hasDecorations()
    {
        return decorative;
    }

    @Override
    public MapColor mapColor()
    {
        return mapColor;
    }

    @Override
    public Block getBlock(DFCMetalBlockType type)
    {
        return DFCBlocks.DFC_METAL_BLOCKS.get(this).get(type).get();
    }

    public enum DFCMetalBlockType
    {
        SMOOTH(false, PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_SMOOTH(false, PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_SMOOTH(false, PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_SMOOTH(false, PartType.WEATHERED, block(Age.OXIDIZED)),
        SMOOTH_SLAB(false, PartType.DEFAULT, slab(Age.NONE)),
        EXPOSED_SMOOTH_SLAB(false, PartType.WEATHERED, slab(Age.EXPOSED)),
        WEATHERED_SMOOTH_SLAB(false, PartType.WEATHERED, slab(Age.WEATHERED)),
        OXIDIZED_SMOOTH_SLAB(false, PartType.WEATHERED, slab(Age.OXIDIZED)),
        SMOOTH_STAIRS(false, PartType.DEFAULT, stairs(SMOOTH, Age.NONE)),
        EXPOSED_SMOOTH_STAIRS(false, PartType.WEATHERED, stairs(EXPOSED_SMOOTH, Age.EXPOSED)),
        WEATHERED_SMOOTH_STAIRS(false, PartType.WEATHERED, stairs(WEATHERED_SMOOTH, Age.WEATHERED)),
        OXIDIZED_SMOOTH_STAIRS(false, PartType.WEATHERED, stairs(OXIDIZED_SMOOTH, Age.OXIDIZED)),
        CUT(false, PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_CUT(false, PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_CUT(false, PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_CUT(false, PartType.WEATHERED, block(Age.OXIDIZED)),
        CUT_SLAB(false, PartType.DEFAULT, slab(Age.NONE)),
        EXPOSED_CUT_SLAB(false, PartType.WEATHERED, slab(Age.EXPOSED)),
        WEATHERED_CUT_SLAB(false, PartType.WEATHERED, slab(Age.WEATHERED)),
        OXIDIZED_CUT_SLAB(false, PartType.WEATHERED, slab(Age.OXIDIZED)),
        CUT_STAIRS(false, PartType.DEFAULT, stairs(CUT, Age.NONE)),
        EXPOSED_CUT_STAIRS(false, PartType.WEATHERED, stairs(EXPOSED_CUT, Age.EXPOSED)),
        WEATHERED_CUT_STAIRS(false, PartType.WEATHERED, stairs(WEATHERED_CUT, Age.WEATHERED)),
        OXIDIZED_CUT_STAIRS(false, PartType.WEATHERED, stairs(OXIDIZED_CUT, Age.OXIDIZED)),
        BRICKS(false, PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_BRICKS(false, PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_BRICKS(false, PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_BRICKS(false, PartType.WEATHERED, block(Age.OXIDIZED)),
        BRICKS_SLAB(false, PartType.DEFAULT, slab(Age.NONE)),
        EXPOSED_BRICKS_SLAB(false, PartType.WEATHERED, slab(Age.EXPOSED)),
        WEATHERED_BRICKS_SLAB(false, PartType.WEATHERED, slab(Age.WEATHERED)),
        OXIDIZED_BRICKS_SLAB(false, PartType.WEATHERED, slab(Age.OXIDIZED)),
        BRICKS_STAIRS(false, PartType.DEFAULT, stairs(BRICKS, Age.NONE)),
        EXPOSED_BRICKS_STAIRS(false, PartType.WEATHERED, stairs(EXPOSED_BRICKS, Age.EXPOSED)),
        WEATHERED_BRICKS_STAIRS(false, PartType.WEATHERED, stairs(WEATHERED_BRICKS, Age.WEATHERED)),
        OXIDIZED_BRICKS_STAIRS(false, PartType.WEATHERED, stairs(OXIDIZED_BRICKS, Age.OXIDIZED)),
        PILLAR(false, PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_PILLAR(false, PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_PILLAR(false, PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_PILLAR(false, PartType.WEATHERED, block(Age.OXIDIZED)),
        GATE(false, PartType.ALL, metal -> new GateBlock(BlockSetType.IRON, blockProperties(metal).noOcclusion().pushReaction(PushReaction.DESTROY))),
        GOBLET(true, PartType.DEFAULT, metal -> new GobletBlock(ExtendedProperties.of().mapColor(metal.mapColor()).noOcclusion().sound(SoundType.METAL).instabreak().blockEntity(DFCBlockEntities.GOBLET), TFCTags.Fluids.USABLE_IN_JUG), (block, properties) -> new GobletItem(block, new Item.Properties().stacksTo(1), DFCConfig.SERVER.gobletCapacity, TFCTags.Fluids.USABLE_IN_JUG));

        private static Function<DFCHelpers.DFCMetalHelpers, Block> block(Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringBlock(blockProperties(metal), age, metal.weatheringResistance())
                    : new Block(blockProperties(metal));
        }

        private static Function<DFCHelpers.DFCMetalHelpers, Block> slab(Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringSlabBlock(blockProperties(metal), age, metal.weatheringResistance())
                    : new SlabBlock(blockProperties(metal));
        }

        private static Function<DFCHelpers.DFCMetalHelpers, Block> stairs(DFCMetalBlockType block, Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringStairBlock(metal.getBlock(block).defaultBlockState(), blockProperties(metal), age, metal.weatheringResistance())
                    : new StairBlock(metal.getBlock(block).defaultBlockState(), blockProperties(metal));
        }

        private static Function<DFCHelpers.DFCMetalHelpers, Block> pillar(Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringRotatedPillarBlock(blockProperties(metal), age, metal.weatheringResistance())
                    : new RotatedPillarBlock(blockProperties(metal));
        }

        private static BlockBehaviour.Properties blockProperties(DFCHelpers.DFCMetalHelpers metal)
        {
            return BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL);
        }

        private final Function<DFCHelpers.DFCMetalHelpers, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final PartType type;
        private final String serializedName;
        private final boolean requiresDecorative;

        DFCMetalBlockType(boolean requiresDecorative, PartType type, Function<DFCHelpers.DFCMetalHelpers, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this.type = type;
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.serializedName = name().toLowerCase(Locale.ROOT);
            this.requiresDecorative = requiresDecorative;
        }

        DFCMetalBlockType(boolean requiresDecorative, PartType type, Function<DFCHelpers.DFCMetalHelpers, Block> blockFactory)
        {
            this(requiresDecorative, type, blockFactory, BlockItem::new);
        }

        public boolean requiresDecorative() {
            return requiresDecorative;
        }

        public Supplier<Block> create(DFCHelpers.DFCMetalHelpers metal)
        {
            return () -> blockFactory.apply(metal);
        }

        public Function<Block, BlockItem> createBlockItem(Item.Properties properties)
        {
            return block -> blockItemFactory.apply(block, properties);
        }

        public boolean has(DFCExtendedMetal metal)
        {
            return type.hasMetal(metal.partType);
        }

        public String createName(DFCHelpers.DFCMetalHelpers metal)
        {
            String slab = "_slab";
            if (serializedName.contains(slab))
            {
                return "metal/" + serializedName.split(slab)[0] + "/" + metal.getSerializedName() + slab;
            }
            String stairs = "_stairs";
            if (serializedName.contains(stairs))
            {
                return "metal/" + serializedName.split(stairs)[0] + "/" + metal.getSerializedName() + stairs;
            }
            return "metal/" + serializedName + "/" + metal.getSerializedName();
        }
    }

    public enum DFCMetalItemType
    {
        POWDER(PartType.DEFAULT, metal -> new Item(base(metal)));

        private static Item.Properties base(DFCHelpers.DFCMetalHelpers metal)
        {
            return new Item.Properties().rarity(metal.rarity());
        }

        private final Function<DFCHelpers.DFCMetalHelpers, Item> itemFactory;
        private final PartType type;

        DFCMetalItemType(PartType type, Function<DFCHelpers.DFCMetalHelpers, Item> itemFactory)
        {
            this.type = type;
            this.itemFactory = itemFactory;
        }

        public Item create(DFCHelpers.DFCMetalHelpers metal)
        {
            return itemFactory.apply(metal);
        }

        public boolean has(DFCExtendedMetal metal)
        {
            return type.hasMetal(metal.partType);
        }

        public boolean isCommonTagPart()
        {
            return type == PartType.INGOT || type == PartType.DEFAULT;
        }
    }

    enum PartType
    {
        INGOT, DEFAULT, DEFAULT_WEATHERING, ALL, ALL_WEATHERING, WEATHERED, ALL_WEATHERED;

        /**
         * Assuming {@code this} represents a block or item type, which must be one of four values, does the {@code metal}
         * create a block / item for this part?
         */
        boolean hasMetal(PartType metal)
        {
            return switch (this)
            {
                case WEATHERED -> metal == DEFAULT_WEATHERING || metal == ALL_WEATHERING;
                case ALL_WEATHERED -> metal == ALL_WEATHERING;
                case ALL -> metal.ordinal() >= ALL.ordinal();
                case DEFAULT -> metal.ordinal() >= DEFAULT.ordinal();
                case INGOT -> true;
                default -> throw new AssertionError("Invalid choice for a metal type " + this);
            };
        }
    }

}
