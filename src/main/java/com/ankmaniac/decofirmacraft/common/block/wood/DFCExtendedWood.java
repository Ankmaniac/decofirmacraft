package com.ankmaniac.decofirmacraft.common.block.wood;

import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.block.DFCShelfBlock;
import com.ankmaniac.decofirmacraft.common.block.LargeDoor;
import com.ankmaniac.decofirmacraft.common.block.TallDoor;
import com.ankmaniac.decofirmacraft.common.block.rock.DFCExtendedRock;
import com.ankmaniac.decofirmacraft.common.blockentities.DFCBlockEntities;
import com.ankmaniac.decofirmacraft.util.DFCHelpers.DFCWoodRegistry;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.LoomBlockEntity;
import net.dries007.tfc.common.blockentities.SluiceBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blockentities.rotation.WindmillBlockEntity;
import net.dries007.tfc.common.blocks.*;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rotation.*;
import net.dries007.tfc.common.blocks.wood.*;
import net.dries007.tfc.common.items.BarrelBlockItem;
import net.dries007.tfc.common.items.ChestBlockItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum DFCExtendedWood implements DFCWoodRegistry
{
    ACACIA(false, MapColor.TERRACOTTA_ORANGE, MapColor.TERRACOTTA_LIGHT_GRAY, 11, 210, WoodMod.TFC),
    ASH(false, MapColor.TERRACOTTA_PINK, MapColor.TERRACOTTA_ORANGE, 7, 10, WoodMod.TFC),
    ASPEN(false, MapColor.TERRACOTTA_GREEN, MapColor.TERRACOTTA_WHITE, 8, 250, WoodMod.TFC),
    BIRCH(false, MapColor.COLOR_BROWN, MapColor.TERRACOTTA_WHITE, 7, 145, WoodMod.TFC),
    BLACKWOOD(false, MapColor.COLOR_BLACK, MapColor.COLOR_BROWN, 8, 80, WoodMod.TFC),
    CHESTNUT(false, MapColor.TERRACOTTA_RED, MapColor.COLOR_LIGHT_GREEN, 7, 40, WoodMod.TFC),
    DOUGLAS_FIR(true, MapColor.TERRACOTTA_YELLOW, MapColor.TERRACOTTA_BROWN, 7, 0, WoodMod.TFC),
    HICKORY(false, MapColor.TERRACOTTA_BROWN, MapColor.COLOR_GRAY, 10, 230, WoodMod.TFC),
    KAPOK(false, MapColor.COLOR_PURPLE, MapColor.COLOR_BROWN, 7, 30, WoodMod.TFC),
    MANGROVE(false, MapColor.COLOR_RED, MapColor.COLOR_BROWN, 8, 100, WoodMod.TFC),
    MAPLE(false, MapColor.COLOR_ORANGE, MapColor.TERRACOTTA_GRAY, 7, 0, WoodMod.TFC),
    OAK(false, MapColor.WOOD, MapColor.COLOR_BROWN, 10, 120, WoodMod.TFC),
    PALM(false, MapColor.COLOR_ORANGE, MapColor.COLOR_BROWN, 7, 255, WoodMod.TFC),
    PINE(true, MapColor.TERRACOTTA_GRAY, MapColor.COLOR_GRAY, 7, 0, WoodMod.TFC),
    ROSEWOOD(false, MapColor.COLOR_RED, MapColor.TERRACOTTA_LIGHT_GRAY, 8, 170, WoodMod.TFC),
    SEQUOIA(true, MapColor.TERRACOTTA_RED, MapColor.TERRACOTTA_RED, 18, 0, WoodMod.TFC),
    SPRUCE(true, MapColor.TERRACOTTA_PINK, MapColor.TERRACOTTA_BLACK, 7, 0, WoodMod.TFC),
    SYCAMORE(false, MapColor.COLOR_YELLOW, MapColor.TERRACOTTA_LIGHT_GREEN, 8, 200, WoodMod.TFC),
    WHITE_CEDAR(true, MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_LIGHT_GRAY, 7, 0, WoodMod.TFC),
    WILLOW(false, MapColor.COLOR_GREEN, MapColor.TERRACOTTA_BROWN, 11, 225, WoodMod.TFC);

    public static final DFCExtendedWood[] VALUES = values();

    private final String serializedName;
    private final boolean conifer;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final TreeGrower tree;
    private final int defaultTicksToGrow;
    private final BlockSetType blockSet;
    private final WoodType woodType;
    private final int autumnIndex;
    private final WoodMod woodMod;

    DFCExtendedWood(boolean conifer, MapColor woodColor, MapColor barkColor, int daysToGrow, int autumnIndex, WoodMod woodMod)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.conifer = conifer;
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.tree = new TreeGrower(
                Helpers.identifier(serializedName).toString(),
                Optional.empty(),
                Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, Helpers.identifier("tree/" + serializedName))),
                Optional.empty()
        );
        this.defaultTicksToGrow = daysToGrow * ICalendar.CALENDAR_TICKS_IN_DAY;
        this.autumnIndex = autumnIndex;
        this.blockSet = new BlockSetType(serializedName);
        this.woodType = new WoodType(Helpers.identifier(serializedName).toString(), blockSet);
        this.woodMod = woodMod;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public boolean isConifer()
    {
        return conifer;
    }

    @Override
    public BlockSetType getBlockSet()
    {
        return blockSet;
    }

    @Override
    public WoodType getVanillaWoodType()
    {
        return woodType;
    }

    @Override
    public MapColor woodColor()
    {
        return woodColor;
    }

    @Override
    public MapColor barkColor()
    {
        return barkColor;
    }

    @Override
    public TreeGrower tree()
    {
        return tree;
    }

    @Override
    public Supplier<Integer> ticksToGrow()
    {
        return TFCConfig.SERVER.saplingGrowthTicks.get(this);
    }

    @Override
    public int autumnIndex()
    {
        return autumnIndex;
    }

    public int defaultTicksToGrow()
    {
        return defaultTicksToGrow;
    }

    @Override
    public Supplier<Block> getBlock(DFCWoodBlockType type)
    {
        return DFCBlocks.DFC_WOOD_BLOCKS.get(this).get(type);
    }

    @Override
    public Supplier<Block> getBlock(Wood.BlockType blockType)
    {
        return TFCBlocks.WOODS.get(this).get(blockType);
    }

    public WoodMod woodMod()
    {
        return woodMod;
    }

    public enum DFCWoodBlockType
    {
        LOG((self, wood) -> new LogBlock(ExtendedProperties.of(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? wood.woodColor() : wood.barkColor()).strength(8f).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).requiresCorrectToolForDrops().flammableLikeLogs(), wood.getBlock(self.stripped())), WoodMod.TFC_BLOCK),
        STRIPPED_LOG(wood -> new LogBlock(ExtendedProperties.of(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? wood.woodColor() : wood.barkColor()).strength(7.5f).sound(SoundType.WOOD).requiresCorrectToolForDrops().flammableLikeLogs(), null), WoodMod.TFC_BLOCK),
        WOOD((self, wood) -> new LogBlock(properties(wood).strength(8f).requiresCorrectToolForDrops().flammableLikeLogs(), wood.getBlock(self.stripped())), WoodMod.TFC_BLOCK),
        STRIPPED_WOOD(wood -> new LogBlock(properties(wood).strength(7.5f).requiresCorrectToolForDrops().flammableLikeLogs(), null), WoodMod.TFC_BLOCK),
        LEAVES((self, wood) -> new TFCLeavesBlock(ExtendedProperties.of().mapColor(MapColor.PLANT).strength(0.5F).sound(SoundType.GRASS).defaultInstrument().randomTicks().noOcclusion().isViewBlocking(TFCBlocks::never).flammableLikeLeaves(), wood.autumnIndex(), wood.getBlock(self.fallenLeaves()), wood.getBlock(self.twig())), WoodMod.TFC_BLOCK),
        PLANKS(wood -> new ExtendedBlock(properties(wood).strength(1.5f, 3.0F).flammableLikePlanks()), WoodMod.TFC_BLOCK),
        SAPLING((self, wood) -> new TFCSaplingBlock(wood.tree(), ExtendedProperties.of(MapColor.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).flammableLikeLeaves().blockEntity(TFCBlockEntities.TICK_COUNTER), wood.ticksToGrow(), false), TFCSaplingBlock.TFCSaplingBlockItem::new, WoodMod.TFC_BLOCK),
        POTTED_SAPLING(wood -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, wood.getBlock(SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ACACIA_SAPLING)), WoodMod.TFC_BLOCK),
        BOOKSHELF(wood -> new BookshelfBlock(properties(wood).strength(2.0F, 3.0F).flammable(20, 30).enchantPower(BookshelfBlock::getEnchantPower).blockEntity(TFCBlockEntities.BOOKSHELF)), WoodMod.TFC_BLOCK),
        DOOR(wood -> new TFCDoorBlock(properties(wood).strength(3.0F).noOcclusion().flammableLikePlanks(), wood.getBlockSet()), WoodMod.TFC_BLOCK),
        TRAPDOOR(wood -> new TFCTrapDoorBlock(properties(wood).strength(3.0F).noOcclusion().flammableLikePlanks(), wood.getBlockSet()), WoodMod.TFC_BLOCK),
        FENCE(wood -> new TFCFenceBlock(properties(wood).strength(2.0F, 3.0F).flammableLikePlanks()), WoodMod.TFC_BLOCK),
        LOG_FENCE(wood -> new TFCFenceBlock(properties(wood).strength(2.0F, 3.0F).flammableLikeLogs()), WoodMod.TFC_BLOCK),
        FENCE_GATE(wood -> new TFCFenceGateBlock(properties(wood).strength(2.0F, 3.0F).flammableLikePlanks()), WoodMod.TFC_BLOCK),
        BUTTON(wood -> new TFCWoodButtonBlock(ExtendedProperties.of().noCollission().strength(0.5F).sound(SoundType.WOOD).flammableLikePlanks(), wood.getBlockSet()), WoodMod.TFC_BLOCK),
        PRESSURE_PLATE(wood -> new TFCPressurePlateBlock(wood.getBlockSet(), properties(wood).noCollission().strength(0.5F).sound(SoundType.WOOD).flammableLikePlanks()), WoodMod.TFC_BLOCK),
        SLAB(wood -> new TFCSlabBlock(properties(wood).strength(1.5f, 3.0F).flammableLikePlanks()), WoodMod.TFC_BLOCK),
        STAIRS(wood -> new TFCStairBlock(() -> wood.getBlock(PLANKS).get().defaultBlockState(), properties(wood).strength(1.5f, 3.0F).sound(SoundType.WOOD).flammableLikePlanks()), WoodMod.TFC_BLOCK),
        TOOL_RACK(wood -> new ToolRackBlock(properties(wood).strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.TOOL_RACK)), WoodMod.TFC_BLOCK),
        TWIG(wood -> GroundcoverBlock.twig(ExtendedProperties.of().strength(0.05F, 0.0F).sound(SoundType.WOOD).noCollission().flammableLikeWool()), WoodMod.TFC_BLOCK),
        FALLEN_LEAVES((self, wood) -> new FallenLeavesBlock(ExtendedProperties.of().strength(0.05F, 0.0F).noOcclusion().noCollission().isViewBlocking(TFCBlocks::never).sound(SoundType.CROP).flammableLikeWool(), wood.getBlock(self.leaves())), WoodMod.TFC_BLOCK),
        VERTICAL_SUPPORT(wood -> new VerticalSupportBlock(properties(wood).strength(1.0F).noOcclusion().flammableLikeLogs()), WoodMod.TFC_BLOCK),
        HORIZONTAL_SUPPORT(wood -> new HorizontalSupportBlock(properties(wood).strength(1.0F).noOcclusion().flammableLikeLogs()), WoodMod.TFC_BLOCK),
        WORKBENCH(wood -> new TFCCraftingTableBlock(properties(wood).strength(2.5F).flammableLikeLogs()), WoodMod.TFC_BLOCK),
        TRAPPED_CHEST((self, wood) -> new TFCTrappedChestBlock(properties(wood).strength(2.5F).flammableLikeLogs().blockEntity(TFCBlockEntities.TRAPPED_CHEST).clientTicks(ChestBlockEntity::lidAnimateTick), wood.getSerializedName()), ChestBlockItem::new, WoodMod.TFC_BLOCK),
        CHEST((self, wood) -> new TFCChestBlock(properties(wood).strength(2.5F).flammableLikeLogs().blockEntity(TFCBlockEntities.CHEST).clientTicks(ChestBlockEntity::lidAnimateTick), wood.getSerializedName()), ChestBlockItem::new, WoodMod.TFC_BLOCK),
        LOOM((self, wood) -> new TFCLoomBlock(properties(wood).strength(2.5F).noOcclusion().flammableLikePlanks().blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick), self.planksTexture(wood)), WoodMod.TFC_BLOCK),
        SLUICE(wood -> new SluiceBlock(properties(wood).strength(3F).noOcclusion().flammableLikeLogs().blockEntity(TFCBlockEntities.SLUICE).serverTicks(SluiceBlockEntity::serverTick)), WoodMod.TFC_BLOCK),
        SIGN(wood -> new TFCStandingSignBlock(properties(wood).noCollission().strength(1F).flammableLikePlanks().blockEntity(TFCBlockEntities.SIGN).ticks(SignBlockEntity::tick), wood.getVanillaWoodType()), WoodMod.TFC_BLOCK),
        WALL_SIGN(wood -> new TFCWallSignBlock(properties(wood).noCollission().strength(1F).dropsLike(wood.getBlock(SIGN)).flammableLikePlanks().blockEntity(TFCBlockEntities.SIGN).ticks(SignBlockEntity::tick), wood.getVanillaWoodType()), WoodMod.TFC_BLOCK),
        BARREL((self, wood) -> new BarrelBlock(properties(wood).strength(2.5f).flammableLikePlanks().noOcclusion().blockEntity(TFCBlockEntities.BARREL).serverTicks(BarrelBlockEntity::serverTick)), BarrelBlockItem::new, WoodMod.TFC_BLOCK),
        LECTERN(wood -> new TFCLecternBlock(properties(wood).noCollission().strength(2.5F).flammableLikePlanks().blockEntity(TFCBlockEntities.LECTERN)), WoodMod.TFC_BLOCK),
        SCRIBING_TABLE(wood -> new ScribingTableBlock(properties(wood).noOcclusion().strength(2.5F).flammable(20, 30)), WoodMod.TFC_BLOCK),
        SEWING_TABLE(wood -> new SewingTableBlock(properties(wood).noOcclusion().strength(2.5F).flammable(20, 30)), WoodMod.TFC_BLOCK),
        /*lol*/TFC_SHELF(wood -> new ShelfBlock(properties(wood).noOcclusion().strength(2.5f).flammableLikePlanks().blockEntity(TFCBlockEntities.SHELF), false), WoodMod.TFC_BLOCK),
        AXLE((self, wood) -> new AxleBlock(properties(wood).noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.AXLE), getBlock(wood, self.windmill()), self.planksTexture(wood)), WoodMod.TFC_BLOCK),
        BLADED_AXLE((self, wood) -> new BladedAxleBlock(properties(wood).noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.BLADED_AXLE), getBlock(wood, self.axle())), WoodMod.TFC_BLOCK),
        ENCASED_AXLE((self, wood) -> new EncasedAxleBlock(properties(wood).strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.ENCASED_AXLE)), WoodMod.TFC_BLOCK),
        CLUTCH((self, wood) -> new ClutchBlock(properties(wood).strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.CLUTCH), getBlock(wood, self.axle())), WoodMod.TFC_BLOCK),
        GEAR_BOX((self, wood) -> new GearBoxBlock(properties(wood).strength(2f).noOcclusion().blockEntity(TFCBlockEntities.GEAR_BOX), getBlock(wood, self.axle())), WoodMod.TFC_BLOCK),
        WINDMILL((self, wood) -> new WindmillBlock(properties(wood).strength(9f).noOcclusion().blockEntity(TFCBlockEntities.WINDMILL).ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick), getBlock(wood, self.axle())), WoodMod.TFC_BLOCK),
        WATER_WHEEL((self, wood) -> new WaterWheelBlock(properties(wood).strength(9f).noOcclusion().blockEntity(TFCBlockEntities.WATER_WHEEL).ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick), getBlock(wood, self.axle()), self.waterWheelTexture(wood)), WoodMod.TFC_BLOCK),
        SHELF(wood -> new DFCShelfBlock(ExtendedProperties.of(Blocks.OAK_PLANKS).blockEntity(DFCBlockEntities.DFC_SHELVES)), WoodMod.DFC_BLOCK),
        TALL_DOOR(wood -> new TallDoor(properties(wood).strength(3.0F).noOcclusion().flammableLikePlanks()), WoodMod.DFC_BLOCK),
        LARGE_DOOR(wood -> new LargeDoor(properties(wood).strength(3.0F).noOcclusion().flammableLikePlanks()), WoodMod.DFC_BLOCK);

        private static ExtendedProperties properties(DFCWoodRegistry wood)
        {
            return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS);
        }

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(DFCWoodRegistry wood, DFCWoodBlockType type)
        {
            return (Supplier<? extends B>) wood.getBlock(type);
        }

        private final BiFunction<DFCWoodBlockType, DFCWoodRegistry, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, DFCWoodRegistry, ? extends BlockItem> blockItemFactory;
        private final WoodMod woodMod;

        DFCWoodBlockType(Function<DFCWoodRegistry, Block> blockFactory, WoodMod woodMod)
        {
            this((self, wood) -> blockFactory.apply(wood), woodMod);
        }

        DFCWoodBlockType(BiFunction<DFCWoodBlockType, DFCWoodRegistry, Block> blockFactory, WoodMod woodMod)
        {
            this(blockFactory, BlockItem::new, woodMod);
        }

        DFCWoodBlockType(BiFunction<DFCWoodBlockType, DFCWoodRegistry, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory, WoodMod woodMod)
        {
            this(blockFactory, (block, properties, self) -> blockItemFactory.apply(block, properties), woodMod);
        }

        DFCWoodBlockType(BiFunction<DFCWoodBlockType, DFCWoodRegistry, Block> blockFactory, TriFunction<Block, Item.Properties, DFCWoodRegistry, ? extends BlockItem> blockItemFactory, WoodMod woodMod)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.woodMod = woodMod;
        }

        public boolean has(DFCExtendedWood wood){
            return woodMod.hasWood(wood.woodMod);
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(DFCWoodRegistry wood, Item.Properties properties)
        {
            return needsItem() ? block -> blockItemFactory.apply(block, properties, wood) : null;
        }

        public String nameFor(DFCWoodRegistry wood)
        {
            return switch (this)
            {
                // N.B. Only stairs and slabs use the variant of planks, this is consistent with stairs + slabs in the rest of the mod
                // and also with that these stair and slabs are based on the planks block
                case SLAB -> "wood/planks/%s_slab".formatted(wood.getSerializedName());
                case STAIRS -> "wood/planks/%s_stairs".formatted(wood.getSerializedName());
                default -> "wood/%s/%s".formatted(name().toLowerCase(Locale.ROOT), wood.getSerializedName());
            };
        }

        public boolean needsItem()
        {
            return switch (this)
            {
                case VERTICAL_SUPPORT, HORIZONTAL_SUPPORT, SIGN, WALL_SIGN, POTTED_SAPLING, WINDMILL -> false;
                default -> true;
            };
        }

        private DFCWoodBlockType stripped()
        {
            return switch (this)
            {
                case LOG -> STRIPPED_LOG;
                case WOOD -> STRIPPED_WOOD;
                default -> throw new IllegalStateException("Block type " + name() + " does not have a stripped variant");
            };
        }

        private ResourceLocation planksTexture(DFCWoodRegistry wood)
        {
            return Helpers.identifier("block/wood/planks/" + wood.getSerializedName());
        }

        private ResourceLocation waterWheelTexture(DFCWoodRegistry wood)
        {
            return Helpers.identifier("textures/entity/water_wheel/" + wood.getSerializedName() + ".png");
        }

        private DFCWoodBlockType twig() {return TWIG;}

        private DFCWoodBlockType fallenLeaves() {return FALLEN_LEAVES;}

        private DFCWoodBlockType leaves() {return LEAVES;}

        private DFCWoodBlockType axle() {return AXLE;}

        private DFCWoodBlockType windmill() {return WINDMILL;}

        public Supplier<Block> create(DFCWoodRegistry wood)
        {
            return () -> blockFactory.apply(this, wood);
        }
    }

    public static void registerBlockSetTypes()
    {
        for (DFCExtendedWood wood : VALUES)
        {
            if (wood.woodMod == WoodMod.DFC)
            {
                BlockSetType.register(wood.blockSet);
                WoodType.register(wood.woodType);
            }
        }
    }

    enum WoodMod
    {
        TFC, AFC, BENEATH, DFC, TFC_BLOCK, AFC_BLOCK, BENEATH_BLOCK, DFC_BLOCK;

        boolean hasWood(WoodMod wood)
        {
            return switch (this)
            {
                case TFC_BLOCK, AFC_BLOCK, BENEATH_BLOCK -> wood == DFC;
                case DFC_BLOCK -> true;
                default -> throw new AssertionError("Invalid choice for a rock type " + this);
            };
        }
    }
}
