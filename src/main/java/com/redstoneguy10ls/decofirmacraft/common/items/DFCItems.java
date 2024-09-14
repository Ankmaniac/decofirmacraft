package com.redstoneguy10ls.decofirmacraft.common.items;

import com.redstoneguy10ls.decofirmacraft.common.blocks.DFCFluids;
import com.redstoneguy10ls.decofirmacraft.common.blocks.metal.DFCMetal;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.DFCOre;
import com.redstoneguy10ls.decofirmacraft.common.blocks.rock.DFCRock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft.MOD_ID;

public class DFCItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    public static final Map<DFCRock, RegistryObject<Item>> DFC_BRICKS = Helpers.mapOfKeys(DFCRock.class, type ->
            register("brick/" + type.name())
    );

    public static final Map<DFCMetal.DFCDefault, Map<DFCMetal.DFCDefault.DFCItemType, RegistryObject<Item>>> METAL_ITEMS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
            Helpers.mapOfKeys(DFCMetal.DFCDefault.DFCItemType.class, type ->
                    register("metal/" + type.name() + "/" + metal.name(), () -> type.create(metal))
            )
    );

    public static final Map<DFCMetal.DFCDefault, RegistryObject<BucketItem>> DFC_METAL_FLUID_BUCKETS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
            register("bucket/metal/" + metal.name(), () -> new BucketItem(DFCFluids.METALS.get(metal).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    public static final Map<DFCOre, Map<Ore.Grade, RegistryObject<Item>>> GRADED_ORES = Helpers.mapOfKeys(DFCOre.class, DFCOre::isGraded, ore ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                    register("ore/" + grade.name() + '_' + ore.name())
            )
    );

    public static final Map<DFCOre, RegistryObject<Item>> ORES = Helpers.mapOfKeys(DFCOre.class, ore -> !ore.isGraded(), ore ->
            register("ore/"+ ore.name())
    );


    public static final RegistryObject<Item> UNFIRED_TILE = register("ceramic/unfired_tile");
    public static final RegistryObject<Item> TILE = register("ceramic/tile");
    public static final RegistryObject<Item> UNFIRED_SHINGLE = register("ceramic/unfired_shingle");
    public static final RegistryObject<Item> SHINGLE = register("ceramic/shingle");

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
