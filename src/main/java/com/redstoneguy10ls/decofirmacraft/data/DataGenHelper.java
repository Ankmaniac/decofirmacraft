package com.redstoneguy10ls.decofirmacraft.data;

//ripped straight from nifty ships :sob:

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class DataGenHelper {
    private DataGenHelper() {
    }

    /**
     * Takes a string like dark_oak and converts it to Dark Oak.
     */
    public static String langify(final String serializedName) {
        return Arrays.stream(serializedName.split("_")).map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static <T extends Comparable<T> & StringRepresentable, C extends ConditionUserBuilder<? extends C>> C matchProperty(
            final C conditionBuilder, final Block block, final Property<T> property, final T propertyValue) {
        conditionBuilder.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, propertyValue)));
        return conditionBuilder;
    }

    public static <C extends ConditionUserBuilder<? extends C>> C matchProperty(final C conditionBuilder,
                                                                                final Block block, final Property<Integer> property, final int propertyValue) {
        conditionBuilder.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, propertyValue)));
        return conditionBuilder;
    }

    public static <F extends FunctionUserBuilder<? extends F>> F setCount(final F functionBuilder, final int count) {
        functionBuilder.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
        return functionBuilder;
    }

    public static <F extends FunctionUserBuilder<? extends F>> F setCount(final F functionBuilder, final int count,
                                                                          final boolean add) {
        functionBuilder.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count), add));
        return functionBuilder;
    }

    public static <F extends FunctionUserBuilder<? extends F>> F setCountBetween(final F functionBuilder,
                                                                                 final float min, final float max) {
        functionBuilder.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
        return functionBuilder;
    }

    public static <F extends FunctionUserBuilder<? extends F>> F setCountBetween(final F functionBuilder,
                                                                                 final float min, final float max, final boolean add) {
        functionBuilder.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max), add));
        return functionBuilder;
    }

    public static LootPool.Builder lootPoolOf(final ItemLike item) {
        return lootPoolOf(LootItem.lootTableItem(item));
    }

    /**
     * Loot pool with the given item and sets the count
     */
    public static LootPool.Builder lootPoolOf(final ItemLike item, final int count) {
        return lootPoolOf(LootItem.lootTableItem(item), count);
    }

    /**
     * Loot pool with the given item and sets the count
     */
    public static LootPool.Builder lootPoolOf(final ItemLike item, final float min, final float max) {
        return lootPoolOf(LootItem.lootTableItem(item), min, max);
    }

    public static <E extends LootPoolEntryContainer.Builder<? extends E>> LootPool.Builder lootPoolOf(final E entry) {
        return LootPool.lootPool().add(entry);
    }

    public static <E extends LootPoolEntryContainer.Builder<? extends E>> LootPool.Builder lootPoolOf(final E entry,
                                                                                                      final int count) {
        return setCount(LootPool.lootPool().add(entry), count);
    }

    public static <E extends LootPoolEntryContainer.Builder<? extends E>> LootPool.Builder lootPoolOf(final E entry,
                                                                                                      final float min, final float max) {
        return setCountBetween(LootPool.lootPool().add(entry), min, max);
    }
}