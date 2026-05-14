//package com.ankmaniac.decofirmacraft.common.entities;
//
//import net.dries007.tfc.util.registry.RegistryHolder;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.MobCategory;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//import java.util.Locale;
//
//import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;
//
////What does a decoration mod have to do with entities
//public class DFCEntities {
//    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MOD_ID);
//
////    public static final Id<WoodenMinecart> WOODEN_MINECART = register("wooden_minecart", EntityType.Builder.<WoodenMinecart>of(WoodenMinecart::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
//
//    public static <E extends Entity> Id<E> register(String name, EntityType.Builder<E> builder)
//    {
//        return register(name, builder, true);
//    }
//
//    public static <E extends Entity> Id<E> register(String name, EntityType.Builder<E> builder, boolean serialize)
//    {
//        final String id = name.toLowerCase(Locale.ROOT);
//        return new Id<>(ENTITIES.register(id, () -> {
//            if (!serialize) builder.noSave();
//            return builder.build(MOD_ID + ":" + id);
//        }));
//    }
//
//    public record Id<T extends Entity>(DeferredHolder<EntityType<?>, EntityType<T>> holder)
//            implements RegistryHolder<EntityType<?>, EntityType<T>> {}
//}
