package com.ankmaniac.decofirmacraft.data.providers;

//Pretty much taken straight from NiftyShips, including the smart language provider and datagen helper


import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.common.blocks.DFCBlocks;
import com.ankmaniac.decofirmacraft.common.items.DFCItems;
import com.ankmaniac.decofirmacraft.data.SmartLanguageProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class DFCLanguageProvider extends SmartLanguageProvider {

    public DFCLanguageProvider(final PackOutput output) {
        super(output, DecoFirmaCraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addTranslationsForBlocks();
//        this.addTranslationsForItems();

        this.add("dfc.creativetab.rock_tab", "DFC Rocks and Variants");
        this.add("dfc.creativetab.ore_tab", "DFC Ores");
        this.add("dfc.creativetab.metal_tab", "DFC Metal Items");
    }

    private void addTranslationsForBlocks() {
//        DFCBlocks.CUSTOM_ROCK_TYPES.forEach((rock, registryObject) -> this.addBlock((Supplier<? extends Block>) registryObject,
//                String.format(Locale.ROOT, "%s" ,
//                        DataGenHelper.langify(rock.getSerializedName()))));
//        this.addBlock(DFCBlocks., "");
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DFCBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    @Override
    protected Iterable<Item> getKnownItems() {
        return DFCItems.ITEMS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}









