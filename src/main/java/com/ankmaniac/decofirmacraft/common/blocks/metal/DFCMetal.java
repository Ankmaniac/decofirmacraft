package com.ankmaniac.decofirmacraft.common.blocks.metal;

import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import com.ankmaniac.decofirmacraft.util.DFCHelpers;

import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.util.NonNullFunction;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DFCMetal {

    public enum DFCDefault implements RegistryMetal {
        ALUMINUM(0xFFD9D8C5, MapColor.COLOR_LIGHT_GRAY, Rarity.UNCOMMON, true, false, false),
        ALUMINA(0xFFE3E3DC, MapColor.COLOR_LIGHT_GRAY, Rarity.COMMON, true, false, false),
        LEAD(0xFF7E789C, MapColor.COLOR_PURPLE, Rarity.COMMON, true, false, false),
        PEWTER(0xFFBBBD9F, MapColor.COLOR_YELLOW, Rarity.COMMON, true, false, false),
        PLATINUM(0xFFDCF2F5, MapColor.COLOR_LIGHT_BLUE, Rarity.COMMON, true, false, false);

        private final String serializedName;
        private final boolean parts, armor, utility;
        private final MapColor mapColor;
        private final Rarity rarity;
        private final int color;
        private final ResourceLocation sheet;

        DFCDefault(int color, MapColor mapColor, Rarity rarity, boolean parts, boolean armor, boolean utility)
        {
            this.serializedName = name().toLowerCase(Locale.ROOT);
            this.rarity = rarity;
            this.color = color;
            this.mapColor = mapColor;
            this.sheet = DFCHelpers.identifier("block/metal/full/" + serializedName);

            this.parts = parts;
            this.armor = armor;
            this.utility = utility;
        }

        public ResourceLocation getSheet()
        {
            return sheet;
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

        public Rarity getRarity()
        {
            return rarity;
        }

        @Override
        public Tier toolTier()
        {
            return TFCTiers.STEEL;
        }

        @Override
        public ArmorMaterial armorTier()
        {
            return TFCArmorMaterials.RED_STEEL;
        }

        @Override
        public Metal.Tier metalTier()
        {
            return Metal.Tier.TIER_VI;
        }

        @Override
        public MapColor mapColor()
        {
            return mapColor;
        }

        @Override
        public Supplier<Block> getFullBlock()
        {
            return DFCBlocks.DFC_METALS.get(this).get(Metal.BlockType.BLOCK);
        }

        public enum DFCItemType
        {
            // Generic
            INGOT(metal -> new Item(new Item.Properties())),
            DOUBLE_INGOT(metal -> new Item(new Item.Properties())),
            SHEET(metal -> new Item(new Item.Properties())),
            DOUBLE_SHEET(metal -> new Item(new Item.Properties())),
            ROD(metal -> new Item(new Item.Properties()));

            private final NonNullFunction<DFCMetal.DFCDefault, Item> itemFactory;

            DFCItemType(NonNullFunction<DFCMetal.DFCDefault, Item> itemFactory)
            {
                this.itemFactory = itemFactory;
            }

            public Item create(DFCMetal.DFCDefault metal)
            {
                return itemFactory.apply(metal);
            }
        }
    }

    public enum DFCBlockType
    {
        SMOOTH(metal -> new Block(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        SMOOTH_SLAB(metal -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        SMOOTH_STAIRS(metal -> new StairBlock(() -> metal.getFullBlock().get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        CUT(metal -> new Block(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        CUT_SLAB(metal -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        CUT_STAIRS(metal -> new StairBlock(() -> metal.getFullBlock().get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BRICKS(metal -> new Block(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BRICKS_SLAB(metal -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BRICKS_STAIRS(metal -> new StairBlock(() -> metal.getFullBlock().get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        PILLAR(metal -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

        private final Function<RegistryMetal, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final String serializedName;

        DFCBlockType(Function<RegistryMetal, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        DFCBlockType(Function<RegistryMetal, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        public Supplier<Block> create(RegistryMetal metal)
        {
            return () -> blockFactory.apply(metal);
        }

        public Function<Block, BlockItem> createBlockItem(Item.Properties properties)
        {
            return block -> blockItemFactory.apply(block, properties);
        }

        public String createName(RegistryMetal metal)
        {
            if (this == SMOOTH_SLAB || this == SMOOTH_STAIRS)
            {
                return SMOOTH.createName(metal) + (this == SMOOTH_SLAB ? "_slab" : "_stairs");
            }
            else if (this == CUT_SLAB || this == CUT_STAIRS)
            {
                return CUT.createName(metal) + (this == CUT_SLAB ? "_slab" : "_stairs");
            }
            else if (this == BRICKS_SLAB || this == BRICKS_STAIRS)
            {
                return BRICKS.createName(metal) + (this == BRICKS_SLAB ? "_slab" : "_stairs");
            }
            else
            {
                return "metal/" + serializedName + "/" + metal.getSerializedName();
            }
        }
    }
}
