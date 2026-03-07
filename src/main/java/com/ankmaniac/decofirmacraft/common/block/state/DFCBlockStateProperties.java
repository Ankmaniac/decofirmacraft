package com.ankmaniac.decofirmacraft.common.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class    DFCBlockStateProperties {
    public static final BooleanProperty RIGHT;
    public static final BooleanProperty LEFT;
    public static final BooleanProperty ON_WALL;
    public static final BooleanProperty TOP_NORTHEAST;
    public static final BooleanProperty TOP_NORTHWEST;
    public static final BooleanProperty TOP_SOUTHEAST;
    public static final BooleanProperty TOP_SOUTHWEST;
    public static final BooleanProperty BOTTOM_NORTHEAST;
    public static final BooleanProperty BOTTOM_NORTHWEST;
    public static final BooleanProperty BOTTOM_SOUTHEAST;
    public static final BooleanProperty BOTTOM_SOUTHWEST;
    public static final IntegerProperty CANDLES;
    public static final EnumProperty<ColumnStyles> STYLES;
    public static final EnumProperty<ThreeSections> THREE_SECTIONS;
    public static final EnumProperty<DoorSide> DOOR_SIDE;

    public DFCBlockStateProperties() {
    }

    static{
        RIGHT = BooleanProperty.create("right");
        LEFT = BooleanProperty.create("left");
        TOP_NORTHEAST = BooleanProperty.create("top_northeast");
        TOP_NORTHWEST = BooleanProperty.create("top_northwest");
        TOP_SOUTHEAST = BooleanProperty.create("top_southeast");
        TOP_SOUTHWEST = BooleanProperty.create("top_southwest");
        BOTTOM_NORTHEAST = BooleanProperty.create("bottom_northeast");
        BOTTOM_NORTHWEST = BooleanProperty.create("bottom_northwest");
        BOTTOM_SOUTHEAST = BooleanProperty.create("bottom_southeast");
        BOTTOM_SOUTHWEST = BooleanProperty.create("bottom_southwest");
        ON_WALL = BooleanProperty.create("on_wall");
        CANDLES = IntegerProperty.create("candles", 0, 4);
        STYLES = EnumProperty.create("style", ColumnStyles.class);
        THREE_SECTIONS = EnumProperty.create("section", ThreeSections.class);
        DOOR_SIDE = EnumProperty.create("door_side", DoorSide.class);
    }

    public enum ColumnStyles implements StringRepresentable {
        DORIC("doric"),
        IONIC("ionic"),
        CORINTHIAN("corinthian"),
        TUSCAN("tuscan");

        private final String name;

        private ColumnStyles(String name)
        {
            this.name = name;
        }


        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum ThreeSections implements StringRepresentable {
        TOP("top"),
        MIDDLE("middle"),
        BOTTOM("bottom");

        private final String name;

        private ThreeSections(String name)
        {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static BlockPos getBasePos(ThreeSections section, BlockPos pos)
        {
            return switch (section)
            {
                case TOP -> pos.below(2);
                case MIDDLE -> pos.below();
                case BOTTOM -> pos;
            };
        }

        public static ThreeSections getSectionFromInt(int i)
        {
            return switch (i % 3) {
                case 0 -> BOTTOM;
                case 1 -> MIDDLE;
                case 2 -> TOP;
                default -> throw new IllegalStateException("how is this possible");
            };
        }
    }

    public enum DoorSide implements StringRepresentable {
        KNOB("knob"),
        HINGE("hinge");

        private final String name;

        private DoorSide(String name)
        {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static BlockPos getBasePos(DoorSide side, Direction facing, DoorHingeSide hinge, Boolean open, BlockPos pos)
        {
            return side == HINGE ? pos : pos.relative(open ? facing.getOpposite() : hinge.ordinal() == 0 ? facing.getCounterClockWise() : facing.getClockWise());
        }
    }
}
