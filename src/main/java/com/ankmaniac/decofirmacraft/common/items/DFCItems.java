package com.ankmaniac.decofirmacraft.common.items;

import com.ankmaniac.decofirmacraft.common.blocks.DFCFluids;
import com.ankmaniac.decofirmacraft.common.blocks.metal.DFCMetal;
import com.ankmaniac.decofirmacraft.common.blocks.metal.GateBlock;
import com.ankmaniac.decofirmacraft.common.blocks.rock.DFCOre;
import com.ankmaniac.decofirmacraft.common.blocks.rock.DFCRock;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

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

    public static final RegistryObject<Item> PAINTBRUSH = register("paintbrush", () -> new PaintbrushItem(TFCTiers.BRONZE, new Item.Properties()));

    public static final RegistryObject<Item> GYPSUM_POWDER = register("powder/gypsum");


    public static final Map<DFCMetal.DFCDefault, RegistryObject<BucketItem>> DFC_METAL_FLUID_BUCKETS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
            register("bucket/metal/" + metal.name(), () -> new BucketItem(DFCFluids.METALS.get(metal).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    public static final RegistryObject<Item> LEADED_HEMATITIC_GLASS_BATCH = register("leaded_hematitic_glass_batch");
    public static final RegistryObject<Item> LEADED_OLIVINE_GLASS_BATCH = register("leaded_olivine_glass_batch");
    public static final RegistryObject<Item> LEADED_SILICA_GLASS_BATCH = register("leaded_silica_glass_batch");
    public static final RegistryObject<Item> LEADED_VOLCANIC_GLASS_BATCH = register("leaded_volcanic_glass_batch");

    public static final RegistryObject<Item> GLASS_BRICK = register("glass/brick/plain");
    public static final RegistryObject<Item> FOGGY_BRICK = register("glass/brick/foggy");
    public static final Map<DyeColor, RegistryObject<Item>> STAINED_GLASS_BRICK = Helpers.mapOfKeys(DyeColor.class, color ->
            register("glass/brick/" + color.getName()));

    public static final Map<Metal.Default, RegistryObject<Item>> METAL_POWDERS = Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasParts, metal ->
            register("metal/powder/" + metal.name()));

    public static final Map<DFCMetal.DFCDefault, RegistryObject<Item>> DFC_METAL_POWDERS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal ->
                    register("metal/powder/" + metal.name()));

    public static final RegistryObject<Item> CINDER_BLOCK_MOLD = register("wood/mold/brick");
    public static final RegistryObject<Item> SLAB_MOLD = register("wood/mold/slab");
    public static final RegistryObject<Item> FILLED_CINDER_BLOCK_MOLD = register("wood/mold/brick_filled");
    public static final RegistryObject<Item> FILLED_SLAB_MOLD = register("wood/mold/slab_filled");
    public static final RegistryObject<Item> CINDER_BLOCK = register("concrete/brick");
    public static final RegistryObject<Item> CONCRETE_SLAB = register("concrete/slab");



    public static final RegistryObject<BucketItem> PLASTER_FLUID_BUCKET = register("bucket/plaster", () -> new BucketItem(DFCFluids.PLASTER.source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<BucketItem> CONCRETE_FLUID_BUCKET = register("bucket/concrete", () -> new BucketItem(DFCFluids.CONCRETE.source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));


    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
