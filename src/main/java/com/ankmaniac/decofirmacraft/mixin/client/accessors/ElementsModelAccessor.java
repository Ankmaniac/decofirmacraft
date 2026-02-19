package com.ankmaniac.decofirmacraft.mixin.client.accessors;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.neoforged.neoforge.client.model.ElementsModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ElementsModel.class)
public interface ElementsModelAccessor {
    @Accessor
    List<BlockElement> getElements();
}

