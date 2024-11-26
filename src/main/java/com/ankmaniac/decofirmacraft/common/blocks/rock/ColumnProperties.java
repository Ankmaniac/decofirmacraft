package com.ankmaniac.decofirmacraft.common.blocks.rock;

import net.minecraft.util.StringRepresentable;

public enum ColumnProperties implements StringRepresentable {
    DORIC("doric"),
    IONIC("ionic"),
    CORINTHIAN("corinthian"),
    TUSCAN("tuscan");

    private final String name;

    private ColumnProperties(String name) {
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

