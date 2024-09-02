package com.redstoneguy10ls.decofirmacraft.common.items;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import com.redstoneguy10ls.decofirmacraft.common.blocks.metal.DFCMetal;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.CustomDFCRockBlocks;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.CustomRockBlocks;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.DFCOre;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.DFCRock;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.DecorationBlockRegistryObject;
import net.dries007.tfc.common.blocks.OreDeposit;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.SelfTests;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

import static com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DFCTabs.CreativeTabHolder ROCKS =
            register("rock_tab", () -> new ItemStack(DFCBlocks.CUSTOM_ROCK_BLOCKS.get(Rock.ANDESITE).get(CustomRockBlocks.PILLAR).get()), DFCTabs::fillRock);
    public static final DFCTabs.CreativeTabHolder ORES =
            register("ore_tab", () -> new ItemStack(DFCBlocks.DFC_ROCK_ORES.get(DFCRock.ARKOSE).get(Ore.DIAMOND).get()), DFCTabs::fillOre);
    public static final DFCTabs.CreativeTabHolder METAL =
            register("metal_tab", () -> new ItemStack(DFCBlocks.METAL_GATES.get(Metal.Default.WROUGHT_IRON).get()), DFCTabs::fillMetal);


    private static void fillRock(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for(Rock rock : Rock.VALUES)
        {
            for (CustomRockBlocks type : CustomRockBlocks.VALUES)
            {
                accept(out, DFCBlocks.CUSTOM_ROCK_BLOCKS, rock, type);
                if(type.hasVariants())
                {
                    accept(out, DFCBlocks.ROCK_DECORATIONS.get(rock).get(type));
                }

            }
        }
        for(DFCRock rock : DFCRock.VALUES)
        {
            for (CustomDFCRockBlocks type : CustomDFCRockBlocks.VALUES)
            {
                accept(out, DFCBlocks.CUSTOM_DFC_ROCK_BLOCKS, rock, type);
                if(type.hasVariants())
                {
                    accept(out, DFCBlocks.DFC_ROCK_DECORATIONS.get(rock).get(type));
                }

            }
        }
        for(DFCRock rock : DFCRock.VALUES)
        {
            for (Rock.BlockType type : Rock.BlockType.VALUES)
            {
                accept(out, DFCBlocks.CUSTOM_ROCK_TYPES, rock, type);
                if(type.hasVariants())
                {
                    accept(out, DFCBlocks.CUSTOM_DFC_ROCK_DECORATIONS.get(rock).get(type));
                }
            }
            accept(out, DFCItems.DFC_BRICKS, rock);
        }
    }

    private static void fillOre(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for(DFCOre ore : DFCOre.values())
        {
            if (ore.isGraded())
            {
                DFCBlocks.DFC_GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                DFCBlocks.DFC_ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
        for(Ore ore : Ore.values())
        {
            if (ore.isGraded())
            {
                DFCBlocks.DFC_ROCK_GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                DFCBlocks.DFC_ROCK_ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
        for(DFCOre ore : DFCOre.values())
        {
            if (ore.isGraded())
            {
                DFCBlocks.DFC_ROCK_DFC_GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                DFCBlocks.DFC_ROCK_DFC_ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
        for (OreDeposit deposit : OreDeposit.values())
        {
            DFCBlocks.DFC_ORE_DEPOSITS.values().forEach(map -> accept(out, map, deposit));
        }
    }

    private static void fillMetal(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for(Metal.Default metal : Metal.Default.values())
        {
            if(metal.hasUtilities()) {
                accept(out, DFCBlocks.METAL_GATES.get(metal));
            }
        }
        for(Metal.Default metal : Metal.Default.values())
        {
            for(DFCMetal.DFCBlockType type : DFCMetal.DFCBlockType.values())
            {
                accept(out, DFCBlocks.METALS_DFC_BLOCKS, metal, type);
            }
        }
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
            for (DFCMetal.DFCBlockType type : DFCMetal.DFCBlockType.values())
            {
                accept(out, DFCBlocks.DFC_METALS_DFC_BLOCKS, dfcmetal, type);
            }
        }
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
            for (Metal.BlockType type : Metal.BlockType.values())
            {
                accept(out, DFCBlocks.DFC_METALS, dfcmetal, type);
            }
        }
        for(DFCMetal.DFCDefault dfcmetal : DFCMetal.DFCDefault.values())
        {
            for (DFCMetal.DFCDefault.DFCItemType type : DFCMetal.DFCDefault.DFCItemType.values())
            {
                accept(out, DFCItems.METAL_ITEMS, dfcmetal, type);
            }
        }
    }


    private static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == Items.AIR)
        {
            TerraFirmaCraft.LOGGER.error("BlockItem with no Item added to creative tab: " + reg);
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }
    private static <T extends ItemLike, R extends Supplier<T>, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key)
    {
        if (map.containsKey(key))
        {
            out.accept(map.get(key).get());
        }
    }
    private static <T extends ItemLike, R extends Supplier<T>, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2)
    {
        if (map.containsKey(key1) && map.get(key1).containsKey(key2))
        {
            out.accept(map.get(key1).get(key2).get());
        }
    }
    private static void accept(CreativeModeTab.Output out, DecorationBlockRegistryObject decoration)
    {
        out.accept(decoration.stair().get());
        out.accept(decoration.slab().get());
        out.accept(decoration.wall().get());
    }

    private static CreativeTabHolder register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final RegistryObject<CreativeModeTab> reg = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("dfc.creative_tab." + name))
                .displayItems(displayItems)
                .build());
        return new DFCTabs.CreativeTabHolder(reg, displayItems);
    }

    public record CreativeTabHolder(RegistryObject<CreativeModeTab> tab, CreativeModeTab.DisplayItemsGenerator generator) {}
}
