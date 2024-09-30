package com.redstoneguy10ls.decofirmacraft.util;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class PouredLeadedGlassColors {
    public static Item getLeadedStainedGlass(DyeColor color)
    {
        return switch (color)
        {
            case WHITE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.WHITE).get().asItem();
            case ORANGE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.ORANGE).get().asItem();
            case MAGENTA -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.MAGENTA).get().asItem();
            case LIGHT_BLUE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.LIGHT_BLUE).get().asItem();
            case YELLOW -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.YELLOW).get().asItem();
            case LIME -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.LIME).get().asItem();
            case PINK -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.PINK).get().asItem();
            case GRAY -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.GRAY).get().asItem();
            case LIGHT_GRAY -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.LIGHT_GRAY).get().asItem();
            case CYAN -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.CYAN).get().asItem();
            case PURPLE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.PURPLE).get().asItem();
            case BLUE -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.BLUE).get().asItem();
            case BROWN -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.BROWN).get().asItem();
            case GREEN -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.GREEN).get().asItem();
            case RED -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.RED).get().asItem();
            default -> DFCBlocks.STAINED_LEADED_GLASS_PANE.get(DyeColor.BLACK).get().asItem();
        };
    }
}
