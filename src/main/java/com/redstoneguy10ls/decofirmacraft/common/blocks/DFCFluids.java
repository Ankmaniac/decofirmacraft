package com.redstoneguy10ls.decofirmacraft.common.blocks;

import com.redstoneguy10ls.decofirmacraft.common.blocks.metal.DFCMetal;
import com.redstoneguy10ls.decofirmacraft.common.items.DFCItems;
import net.dries007.tfc.common.fluids.ExtendedFluidType;
import net.dries007.tfc.common.fluids.FluidRegistryObject;
import net.dries007.tfc.common.fluids.FluidTypeClientProperties;
import net.dries007.tfc.common.fluids.MoltenFluid;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.redstoneguy10ls.decofirmacraft.DecoFirmaCraft.MOD_ID;
import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class DFCFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);

    public static final Map<DFCMetal.DFCDefault, FluidRegistryObject<ForgeFlowingFluid>> METALS = Helpers.mapOfKeys(DFCMetal.DFCDefault.class, metal
            -> register(
            "metal/dfc_" + metal.getSerializedName(),
            properties -> properties
                    .block(DFCBlocks.DFC_METAL_FLUIDS.get(metal))
                    .bucket(DFCItems.DFC_METAL_FLUID_BUCKETS.get(metal))
                    .explosionResistance(100),
            lavaLike()
                    .descriptionId("fluid.dfc.metal." + metal.getSerializedName())
                    .canConvertToSource(false),
            new FluidTypeClientProperties(ALPHA_MASK | metal.getColor(), MOLTEN_STILL, MOLTEN_FLOW, null, null),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    ));





    private static FluidType.Properties lavaLike()
    {
        return FluidType.Properties.create()
                .adjacentPathType(BlockPathTypes.LAVA)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .lightLevel(15)
                .density(3000)
                .viscosity(6000)
                .temperature(1300)
                .canConvertToSource(false)
                .canDrown(false)
                .canExtinguish(false)
                .canHydrate(false)
                .canPushEntity(false)
                .canSwim(false)
                .supportsBoating(false);
    }

    private static FluidType.Properties waterLike()
    {
        return FluidType.Properties.create()
                .adjacentPathType(BlockPathTypes.WATER)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .canConvertToSource(true)
                .canDrown(true)
                .canExtinguish(true)
                .canHydrate(false)
                .canPushEntity(true)
                .canSwim(true)
                .supportsBoating(true);
    }
    private static <F extends FlowingFluid> FluidRegistryObject<F> register(String name, Consumer<ForgeFlowingFluid.Properties> builder, FluidType.Properties typeProperties, FluidTypeClientProperties clientProperties, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory)
    {
        // Names `metal/foo` to `metal/flowing_foo`
        final int index = name.lastIndexOf('/');
        final String flowingName = index == -1 ? "flowing_" + name : name.substring(0, index) + "/flowing_" + name.substring(index + 1);

        return RegistrationHelpers.registerFluid(FLUID_TYPES, FLUIDS, name, name, flowingName, builder, () -> new ExtendedFluidType(typeProperties, clientProperties), sourceFactory, flowingFactory);
    }
}
