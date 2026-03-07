package com.ankmaniac.decofirmacraft.common.block.metal;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.common.item.metal.GobletItem;
import com.ankmaniac.decofirmacraft.util.DFCHelpers.DFCMetalRegistry;
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

public enum DFCExtendedMetal implements StringRepresentable, DFCMetalRegistry {
    BISMUTH(0xFF486B72, MapColor.TERRACOTTA_GREEN, Rarity.COMMON, -1, PartType.DEFAULT),
    BISMUTH_BRONZE(0xFF418E4F, MapColor.TERRACOTTA_BLUE, Rarity.COMMON, -1, PartType.ALL_DECORATIVE),
    BLACK_BRONZE(0xFF3B2636, MapColor.TERRACOTTA_PINK, Rarity.COMMON, -1, PartType.ALL_DECORATIVE),
    BRONZE(0xFF96892E, MapColor.TERRACOTTA_ORANGE, Rarity.COMMON, 0.7f, PartType.ALL_DECORATIVE_WEATHERING),
    BRASS(0xFF7C5E33, MapColor.GOLD, Rarity.COMMON, 0.7f, PartType.DEFAULT_WEATHERING),
    COPPER(0xFFB64027, MapColor.COLOR_ORANGE, Rarity.COMMON, 0f, PartType.ALL_DECORATIVE_WEATHERING),
    GOLD(0xFFDCBF1B, MapColor.GOLD, Rarity.COMMON, -1, PartType.DECORATIVE),
    NICKEL(0xFF4E4E3C, MapColor.STONE, Rarity.COMMON, -1, PartType.DEFAULT),
    ROSE_GOLD(0xFFEB7137, MapColor.COLOR_PINK, Rarity.COMMON, -1, PartType.DECORATIVE),
    SILVER(0xFF949495, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, 0.95f, PartType.DECORATIVE_WEATHERING),
    TIN(0xFF90A4BB, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.DEFAULT),
    ZINC(0xFFBBB9C4, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.DEFAULT),
    STERLING_SILVER(0xFFAC927B, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, 0.95f, PartType.DECORATIVE_WEATHERING),
    WROUGHT_IRON(0xFF989897, MapColor.METAL, Rarity.COMMON, 0f, PartType.ALL_WEATHERING),
    CAST_IRON(0xFF989897, MapColor.COLOR_BROWN, Rarity.COMMON, -1, PartType.DEFAULT),
    PIG_IRON(0xFF6A595C, MapColor.COLOR_GRAY, Rarity.COMMON, -1, PartType.INGOT),
    STEEL(0xFF5F5F5F, MapColor.COLOR_LIGHT_GRAY, Rarity.UNCOMMON, 0.9f, PartType.ALL_WEATHERING),
    BLACK_STEEL(0xFF111111, MapColor.COLOR_BLACK, Rarity.RARE, -1, PartType.ALL_WEATHERING),
    BLUE_STEEL(0xFF2D5596, MapColor.COLOR_BLUE, Rarity.EPIC, -1, PartType.ALL_WEATHERING),
    RED_STEEL(0xFF700503, MapColor.COLOR_RED, Rarity.EPIC, -1, PartType.ALL_WEATHERING),
    WEAK_STEEL(0xFF111111, MapColor.COLOR_GRAY, Rarity.COMMON, -1, PartType.INGOT),
    WEAK_BLUE_STEEL(0xFF2D5596, MapColor.COLOR_BLUE, Rarity.COMMON, -1, PartType.INGOT),
    WEAK_RED_STEEL(0xFF700503, MapColor.COLOR_RED, Rarity.COMMON, -1, PartType.INGOT),
    HIGH_CARBON_STEEL(0xFF5F5F5F, MapColor.COLOR_GRAY, Rarity.COMMON, -1, PartType.INGOT),
    HIGH_CARBON_BLACK_STEEL(0xFF111111, MapColor.COLOR_BLACK, Rarity.COMMON, -1, PartType.INGOT),
    HIGH_CARBON_BLUE_STEEL(0xFF2D5596, MapColor.COLOR_BLUE, Rarity.COMMON, -1, PartType.INGOT),
    HIGH_CARBON_RED_STEEL(0xFF700503, MapColor.COLOR_RED, Rarity.COMMON, -1, PartType.INGOT),
    UNKNOWN(0xFF2F2B27, MapColor.COLOR_BLACK, Rarity.COMMON, -1, PartType.DEFAULT),
    //DFC Metals
    ALUMINUM(0xFFD9D8C5, MapColor.COLOR_LIGHT_GRAY, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    ALUMINA(0xFFE3E3DC, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.INGOT),
    LEAD(0xFF7E789C, MapColor.COLOR_PURPLE, Rarity.COMMON, -1, PartType.DEFAULT),
    PEWTER(0xFFBBBD9F, MapColor.COLOR_YELLOW, Rarity.COMMON, -1, PartType.DECORATIVE),
    PLATINUM(0xFFDCF2F5, MapColor.COLOR_LIGHT_BLUE, Rarity.COMMON, -1, PartType.DECORATIVE),
    //Firmalife Metals
    CHROMIUM(0xFFF5FEFF, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.DEFAULT),
    STAINLESS_STEEL(0xFFD9FCFF, MapColor.COLOR_LIGHT_GRAY, Rarity.RARE, -1, PartType.ALL),
    //IE2 Metals
    ELECTRUM(0xFCB74A, MapColor.COLOR_YELLOW, Rarity.EPIC, -1, PartType.DEFAULT),
    CONSTANTAN(0xEC8068, MapColor.COLOR_ORANGE, Rarity.EPIC, -1, PartType.DEFAULT),
    IE_ALUMINUM(0xCCC1BC, MapColor.CLAY, Rarity.COMMON, -1, PartType.DEFAULT),
    IE_LEAD(0x433F4D, MapColor.TERRACOTTA_BLUE, Rarity.RARE, -1, PartType.DEFAULT),
    URANIUM(0x738A6C, MapColor.TERRACOTTA_GREEN, Rarity.EPIC, -1, PartType.DEFAULT),
    //TFC MetaullurgyU2 metals
    COMPRESSED_IRON(0x6F6C6B, MapColor.COLOR_GRAY, Rarity.UNCOMMON, 0f, PartType.DEFAULT_WEATHERING),
    MU_PLATINUM(0x627C8B, MapColor.COLOR_BLUE, Rarity.RARE, -1, PartType.DEFAULT),
    NAQUADAH(0x4D5742, MapColor.COLOR_BLACK, Rarity.RARE, -1, PartType.DEFAULT),
    IRIDIUM(0xADC4CE, MapColor.SNOW, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    OSMIUM(0xA5B4CA, MapColor.SNOW, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    OSMIRIDIUM(0x718383, MapColor.COLOR_LIGHT_GRAY, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    MYTHRIL(0x5383A2, MapColor.COLOR_BLUE, Rarity.COMMON, -1, PartType.DEFAULT),
    ADAMANT(0x5B83A9, MapColor.COLOR_BLUE, Rarity.RARE, -1, PartType.DEFAULT),
    BIOSTEEL(0x6A856A, MapColor.COLOR_GREEN, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    DURATIUM(0x463A57, MapColor.COLOR_PURPLE, Rarity.RARE, -1, PartType.DEFAULT),
    ENERGITE(0xBB57E3, MapColor.COLOR_MAGENTA, Rarity.EPIC, -1, PartType.DEFAULT),
    REFINED_GLOWSTONE(0xE2B446, MapColor.COLOR_YELLOW, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    REFINED_OBSIDIAN(0x473752, MapColor.COLOR_PURPLE, Rarity.RARE, -1, PartType.DEFAULT),
    ANTIMONY(0xB4AFBD, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, -1, PartType.DEFAULT),
    TITANIUM(0x898B93, MapColor.COLOR_BLUE, Rarity.UNCOMMON, -1, PartType.DEFAULT),
    TUNGSTEN(0x585F6B, MapColor.COLOR_BLACK, Rarity.EPIC, -1, PartType.DEFAULT),
    TUNGSTEN_STEEL(0x2F353E, MapColor.COLOR_BLACK, Rarity.EPIC, -1, PartType.DEFAULT),
    NETHERITE(0x3B3230, MapColor.COLOR_BLACK, Rarity.EPIC, -1, PartType.DEFAULT),
    //CREATE
    ANDESITE_ALLOY(0x000000/*placeholder*/, MapColor.STONE, Rarity.COMMON, -1, PartType.DEFAULT);


    private final String serializedName;
    private final PartType partType;
    private final MapColor mapColor;
    private final Rarity rarity;
    private final int color;
    private final float weathering;

    DFCExtendedMetal(int color, MapColor mapColor, Rarity rarity, float weathering, PartType partType)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.rarity = rarity;
        this.mapColor = mapColor;
        this.color = color;
        this.partType = partType;
        this.weathering = weathering;
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
        return partType == PartType.ALL || partType == PartType.ALL_WEATHERING || partType == PartType.ALL_DECORATIVE || partType == PartType.ALL_DECORATIVE_WEATHERING;
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
        SMOOTH(PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_SMOOTH(PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_SMOOTH(PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_SMOOTH(PartType.WEATHERED, block(Age.OXIDIZED)),
        SMOOTH_SLAB(PartType.DEFAULT, slab(Age.NONE)),
        EXPOSED_SMOOTH_SLAB(PartType.WEATHERED, slab(Age.EXPOSED)),
        WEATHERED_SMOOTH_SLAB(PartType.WEATHERED, slab(Age.WEATHERED)),
        OXIDIZED_SMOOTH_SLAB(PartType.WEATHERED, slab(Age.OXIDIZED)),
        SMOOTH_STAIRS(PartType.DEFAULT, stairs(SMOOTH, Age.NONE)),
        EXPOSED_SMOOTH_STAIRS(PartType.WEATHERED, stairs(EXPOSED_SMOOTH, Age.EXPOSED)),
        WEATHERED_SMOOTH_STAIRS(PartType.WEATHERED, stairs(WEATHERED_SMOOTH, Age.WEATHERED)),
        OXIDIZED_SMOOTH_STAIRS(PartType.WEATHERED, stairs(OXIDIZED_SMOOTH, Age.OXIDIZED)),
        CUT(PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_CUT(PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_CUT(PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_CUT(PartType.WEATHERED, block(Age.OXIDIZED)),
        CUT_SLAB(PartType.DEFAULT, slab(Age.NONE)),
        EXPOSED_CUT_SLAB(PartType.WEATHERED, slab(Age.EXPOSED)),
        WEATHERED_CUT_SLAB(PartType.WEATHERED, slab(Age.WEATHERED)),
        OXIDIZED_CUT_SLAB(PartType.WEATHERED, slab(Age.OXIDIZED)),
        CUT_STAIRS(PartType.DEFAULT, stairs(CUT, Age.NONE)),
        EXPOSED_CUT_STAIRS(PartType.WEATHERED, stairs(EXPOSED_CUT, Age.EXPOSED)),
        WEATHERED_CUT_STAIRS(PartType.WEATHERED, stairs(WEATHERED_CUT, Age.WEATHERED)),
        OXIDIZED_CUT_STAIRS(PartType.WEATHERED, stairs(OXIDIZED_CUT, Age.OXIDIZED)),
        BRICKS(PartType.DEFAULT, block(Age.NONE)),
        EXPOSED_BRICKS(PartType.WEATHERED, block(Age.EXPOSED)),
        WEATHERED_BRICKS(PartType.WEATHERED, block(Age.WEATHERED)),
        OXIDIZED_BRICKS(PartType.WEATHERED, block(Age.OXIDIZED)),
        BRICKS_SLAB(PartType.DEFAULT, slab(Age.NONE)),
        EXPOSED_BRICKS_SLAB(PartType.WEATHERED, slab(Age.EXPOSED)),
        WEATHERED_BRICKS_SLAB(PartType.WEATHERED, slab(Age.WEATHERED)),
        OXIDIZED_BRICKS_SLAB(PartType.WEATHERED, slab(Age.OXIDIZED)),
        BRICKS_STAIRS(PartType.DEFAULT, stairs(BRICKS, Age.NONE)),
        EXPOSED_BRICKS_STAIRS(PartType.WEATHERED, stairs(EXPOSED_BRICKS, Age.EXPOSED)),
        WEATHERED_BRICKS_STAIRS(PartType.WEATHERED, stairs(WEATHERED_BRICKS, Age.WEATHERED)),
        OXIDIZED_BRICKS_STAIRS(PartType.WEATHERED, stairs(OXIDIZED_BRICKS, Age.OXIDIZED)),
        PILLAR(PartType.DEFAULT, pillar(Age.NONE)),
        EXPOSED_PILLAR(PartType.WEATHERED, pillar(Age.EXPOSED)),
        WEATHERED_PILLAR(PartType.WEATHERED, pillar(Age.WEATHERED)),
        OXIDIZED_PILLAR(PartType.WEATHERED, pillar(Age.OXIDIZED)),
        GATE(PartType.ALL, metal -> new GateBlock(BlockSetType.IRON, blockProperties(metal).noOcclusion().pushReaction(PushReaction.DESTROY))),
        GOBLET(PartType.DECORATIVE, metal -> new GobletBlock(ExtendedProperties.of().mapColor(metal.mapColor()).noOcclusion().sound(SoundType.METAL).instabreak().pushReaction(PushReaction.DESTROY).blockEntity(DFCBlockEntities.GOBLET)), GobletItem::new),
        CANDLEHOLDER(PartType.DECORATIVE, metal -> new CandleholderBlock(ExtendedProperties.of().mapColor(metal.mapColor()).noOcclusion().sound(SoundType.METAL).strength(2.0F, 6.0F).pushReaction(PushReaction.DESTROY).randomTicks().lightLevel(CandleholderBlock.LIGHTING_SCALE).blockEntity(DFCBlockEntities.CANDLEHOLDER)));

        private static Function<DFCMetalRegistry, Block> block(Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringBlock(blockProperties(metal), age, metal.weatheringResistance())
                    : new Block(blockProperties(metal));
        }

        private static Function<DFCMetalRegistry, Block> slab(Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringSlabBlock(blockProperties(metal), age, metal.weatheringResistance())
                    : new SlabBlock(blockProperties(metal));
        }

        private static Function<DFCMetalRegistry, Block> stairs(DFCMetalBlockType block, Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringStairBlock(metal.getBlock(block).defaultBlockState(), blockProperties(metal), age, metal.weatheringResistance())
                    : new StairBlock(metal.getBlock(block).defaultBlockState(), blockProperties(metal));
        }

        private static Function<DFCMetalRegistry, Block> pillar(Age age)
        {
            return metal -> metal.weatheredParts()
                    ? new WeatheringRotatedPillarBlock(blockProperties(metal), age, metal.weatheringResistance())
                    : new RotatedPillarBlock(blockProperties(metal));
        }

        private static BlockBehaviour.Properties blockProperties(DFCMetalRegistry metal)
        {
            return BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL);
        }

        private final Function<DFCMetalRegistry, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final PartType type;
        private final String serializedName;

        DFCMetalBlockType(PartType type, Function<DFCMetalRegistry, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this.type = type;
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        DFCMetalBlockType(PartType type, Function<DFCMetalRegistry, Block> blockFactory)
        {
            this(type, blockFactory, BlockItem::new);
        }

        public Supplier<Block> create(DFCMetalRegistry metal)
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

        public String createName(DFCMetalRegistry metal)
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

        private static Item.Properties base(DFCMetalRegistry metal)
        {
            return new Item.Properties().rarity(metal.rarity());
        }

        private final Function<DFCMetalRegistry, Item> itemFactory;
        private final PartType type;

        DFCMetalItemType(PartType type, Function<DFCMetalRegistry, Item> itemFactory)
        {
            this.type = type;
            this.itemFactory = itemFactory;
        }

        public Item create(DFCMetalRegistry metal)
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
        INGOT,
        DEFAULT,
        DEFAULT_WEATHERING,
        DECORATIVE,
        DECORATIVE_WEATHERING,
        ALL,
        ALL_WEATHERING,
        ALL_DECORATIVE,
        ALL_DECORATIVE_WEATHERING,
        WEATHERED,
        ALL_WEATHERED;

        /**
         * Assuming {@code this} represents a block or item type, which must be one of four values, does the {@code metal}
         * create a block / item for this part?
         */
        boolean hasMetal(PartType metal)
        {
            return switch (this)
            {
                case WEATHERED -> metal == DEFAULT_WEATHERING || metal == ALL_WEATHERING || metal == DECORATIVE_WEATHERING;
                case ALL_WEATHERED -> metal == ALL_WEATHERING || metal == ALL_DECORATIVE_WEATHERING;
                case ALL -> metal.ordinal() >= ALL.ordinal();
                case DEFAULT -> metal.ordinal() >= DEFAULT.ordinal();
                case DECORATIVE -> metal == DECORATIVE || metal == DECORATIVE_WEATHERING || metal == ALL_DECORATIVE || metal == ALL_DECORATIVE_WEATHERING;
                case INGOT -> true;
                default -> throw new AssertionError("Invalid choice for a metal type " + this);
            };
        }
    }

}
