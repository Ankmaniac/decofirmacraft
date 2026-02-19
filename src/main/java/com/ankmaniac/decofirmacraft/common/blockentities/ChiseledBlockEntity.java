package com.ankmaniac.decofirmacraft.common.blockentities;

import com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.*;
import com.ankmaniac.decofirmacraft.client.model.DynamicTextureRecords.DynamicTextureEither;
import com.ankmaniac.decofirmacraft.common.DecoFirmaCraftRegistries;
import com.ankmaniac.decofirmacraft.common.block.ChiseledBlock;
import com.ankmaniac.decofirmacraft.util.DynamicTextureData;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import lombok.Getter;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ankmaniac.decofirmacraft.common.block.ChiseledBlock.properties;

public class ChiseledBlockEntity extends TFCBlockEntity
{
    //Stores blockstate ids for each boolean property
    @Getter
    private int[] stateMap = new int[8];

    //Stores Holders for each boolean property
    private Map<BooleanProperty, Holder<DynamicTextureData>> holderMap;

    final private int chunkCount = properties.size();

    private Holder<DynamicTextureData> defaultHolder;

    public ChiseledBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(DFCBlockEntities.CHISELED_BLOCK_ENTITY.get(), pos, blockState);

        //sets up default keys
        for (int i = 0; i < chunkCount; i++)
        {
            this.stateMap[i] = 0;
        }
//        for (BooleanProperty property : properties)
//        {
//            System.out.println("DEBUG 1 " + defaultHolder);
//            this.holderMap.put(property, defaultHolder);
//        }
        this.holderMap = new HashMap<>(chunkCount);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        this.stateMap = tag.getIntArray("chunk map");
        for (int i = 0; i < chunkCount; i++)
        {
            final int currentI = i;
            final Tag holderTag = tag.get("boolean " + currentI);
            if (holderTag != null)
            {
                DynamicTextureData.CODEC.decode(provider.createSerializationContext(NbtOps.INSTANCE),
                        holderTag).ifSuccess(holder -> holderMap.put(properties.get(currentI), holder.getFirst()));
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        tag.putIntArray("chunk map", stateMap);
        for (int i = 0; i < chunkCount; i++)
        {
            Holder<DynamicTextureData> holder = this.holderMap.get(properties.get(i));
            if (holder != null)
            {
                final int currentI = i;
                DynamicTextureData.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE),
                        holder).ifSuccess(holderTag -> tag.put("boolean " + currentI, holderTag));
            }
        }
        super.saveAdditional(tag, provider);
    }

    public Map<String, @Nullable Either<DynamicTextureSet, List<WeightedDynamicTextureSet>>> getDynamicTextureDataMap()
    {
        List<BooleanProperty> properties = ChiseledBlock.properties;
        Map<String, @Nullable Either<DynamicTextureSet, List<WeightedDynamicTextureSet>>> dynamicTextureDataMap = new HashMap<>(chunkCount);

        for (BooleanProperty property : properties)
        {
            @Nullable Holder<DynamicTextureData> dynamicTextureHolder = holderMap.get(property);
            if (dynamicTextureHolder != null)
            {
                dynamicTextureDataMap.put(property.getName().toLowerCase(), getDynamicTextureSet(dynamicTextureHolder, properties.indexOf(property)));
            }
        }
        return dynamicTextureDataMap;
    }

    private Either<DynamicTextureSet, List<WeightedDynamicTextureSet>> getDynamicTextureSet(Holder<DynamicTextureData> dynamicTextureHolder, int index)
    {
        return dynamicTextureHolder.value().dynamicTextureData().map(DynamicTextureEither::dynamicTextureEither,
                        right -> right.get(Block.stateById(
                                this.stateMap[index])).dynamicTextureEither());
    }

    @Override
    public void setLevel(final Level level) {
        super.setLevel(level);
        final var dynamicTextureRegistry = level.registryAccess().registryOrThrow(DecoFirmaCraftRegistries.DYNAMIC_TEXTURE_DATA);
        this.defaultHolder = dynamicTextureRegistry.getHolder(DynamicTextureData.DEFAULT).or(dynamicTextureRegistry::getAny).orElseThrow();
        for (BooleanProperty property : properties)
        {
            System.out.println("DEBUG 2 " + defaultHolder);
            this.holderMap.putIfAbsent(property, defaultHolder);
        }
    }

    @Override
    public ModelData getModelData() {
        return ModelData.of(DynamicTextureModelData.PROPERTY,
                new DynamicTextureModelData(ImmutableMap.copyOf(getDynamicTextureDataMap())));
    }

    public void updateBlock(BlockState newBlock, Holder<DynamicTextureData> dataHolder)
    {
        for (int i = 0; i < 8; i++)
        {
            this.stateMap[i] = Block.getId(newBlock);
        }

        for(BooleanProperty property : properties)
        {
            this.holderMap.replace(property, dataHolder);
        }

        this.requestModelDataUpdate();
    }

    public void addBlock(BlockState newBlock, Holder<DynamicTextureData> dataHolder, Set<BooleanProperty> replacedProperties)
    {
        for (BooleanProperty property : replacedProperties)
        {
            this.stateMap[properties.indexOf(property)] = Block.getId(newBlock);

            this.holderMap.replace(property, dataHolder);
        }

        this.requestModelDataUpdate();
    }

    public void removeChunk(BooleanProperty removedChunk)
    {
        final int chunkInt = properties.indexOf(removedChunk);

        assert this.stateMap[chunkInt] != 0;

        this.stateMap[chunkInt] = 0;

        this.holderMap.replace(removedChunk, defaultHolder);

        this.requestModelDataUpdate();
    }

    public void replaceChunkMap(ChiseledBlockMaps maps)
    {
        this.stateMap = maps.chunkMap;
        this.holderMap = maps.dynamicTextureHolderMap;
    }

    public ChiseledBlockMaps oldMap()
    {
        return new ChiseledBlockMaps(this.stateMap, this.holderMap);
    }

    public record ChiseledBlockMaps(int[] chunkMap, Map<BooleanProperty, @Nullable Holder<DynamicTextureData>> dynamicTextureHolderMap)
    {

    }
}
