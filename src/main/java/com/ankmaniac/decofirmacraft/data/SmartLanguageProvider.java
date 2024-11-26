package com.ankmaniac.decofirmacraft.data;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class SmartLanguageProvider implements DataProvider {
    private final Map<String, String> data = new TreeMap<>();
    private final PackOutput output;
    private final String modid;
    private final String locale;

    public SmartLanguageProvider(final PackOutput output, final String modid, final String locale) {
        this.output = output;
        this.modid = modid;
        this.locale = locale;
    }

    protected abstract void addTranslations();

    protected abstract Iterable<Block> getKnownBlocks();

    protected abstract Iterable<Item> getKnownItems();

    @Override
    public CompletableFuture<?> run(final CachedOutput cache) {
        this.addTranslations();

        for (final Block block : this.getKnownBlocks()) {
            this.validateEntry(block.getDescriptionId(), ForgeRegistries.BLOCKS.getKey(block));
        }

        for (final Item item : this.getKnownItems()) {
            this.validateEntry(item.getDescriptionId(), ForgeRegistries.ITEMS.getKey(item));
        }

        if (!this.data.isEmpty()) return this.save(cache,
                this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang")
                        .resolve(this.locale + ".json"));

        return CompletableFuture.allOf();
    }

    private void validateEntry(final String langKey, final @Nullable ResourceLocation name) {
        if (!this.data.containsKey(langKey)) {
            throw new IllegalStateException(String.format(Locale.ROOT, "Missing lang entry for '%s'", name));
        }
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }

    private CompletableFuture<?> save(final CachedOutput cache, final Path target) {
        final JsonObject json = new JsonObject();
        this.data.forEach(json::addProperty);

        return DataProvider.saveStable(cache, json, target);
    }

    @SuppressWarnings("unused")
    public void addBlock(final Supplier<? extends Block> key, final String name) {
        this.add(key.get(), name);
    }

    public void add(final Block key, final String name) {
        this.add(key.getDescriptionId(), name);
    }

    @SuppressWarnings("unused")
    public void addItem(final Supplier<? extends Item> key, final String name) {
        this.add(key.get(), name);
    }

    public void add(final Item key, final String name) {
        this.add(key.getDescriptionId(), name);
    }

    @SuppressWarnings("unused")
    public void addItemStack(final Supplier<ItemStack> key, final String name) {
        this.add(key.get(), name);
    }

    public void add(final ItemStack key, final String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(final String key, final String value) {
        if (this.data.put(key, value) != null) throw new IllegalStateException("Duplicate translation key " + key);
    }
}
