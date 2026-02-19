package com.ankmaniac.decofirmacraft.common.block.state;

import net.minecraft.util.StringRepresentable;

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
