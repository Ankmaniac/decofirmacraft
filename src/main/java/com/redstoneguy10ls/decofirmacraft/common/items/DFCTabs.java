package com.redstoneguy10ls.decofirmacraft.common.items;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCBlocks;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.CustomDFCRockBlocks;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.CustomRockBlocks;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.DFCRock;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.DecorationBlockRegistryObject;
import net.dries007.tfc.common.blocks.rock.Rock;
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
