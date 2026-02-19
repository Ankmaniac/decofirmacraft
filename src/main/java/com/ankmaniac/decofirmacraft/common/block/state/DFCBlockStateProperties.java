package com.ankmaniac.decofirmacraft.common.block.state;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
    }
}
