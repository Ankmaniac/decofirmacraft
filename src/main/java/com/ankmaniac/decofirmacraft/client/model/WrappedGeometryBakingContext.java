package com.ankmaniac.decofirmacraft.client.model;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;

@AllArgsConstructor
public class WrappedGeometryBakingContext implements IGeometryBakingContext {
    @Delegate
    private final IGeometryBakingContext delegate;
}

