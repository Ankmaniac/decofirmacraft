//package com.ankmaniac.decofirmacraft.providers;
//
//import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
//import com.ankmaniac.decofirmacraft.common.block.DFCBlocks;
//import com.ankmaniac.decofirmacraft.common.block.rock.DFCExtendedRock;
//import com.google.common.base.Preconditions;
//import net.dries007.tfc.TerraFirmaCraft;
//import net.dries007.tfc.common.blocks.rock.RockCategory;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.Block;
//import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
//import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
//import net.neoforged.neoforge.client.model.generators.ModelFile;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;
//
//public class BuiltInBlockStates extends BlockStateProvider {
//
//    public BuiltInBlockStates(final PackOutput packOutput, final ExistingFileHelper existingFileHelper) {
//        super(packOutput, DecoFirmaCraft.MOD_ID, existingFileHelper);
//    }
//
//    @Override
//    protected void registerStatesAndModels() {
//        ModelFile anvilModel = models().withExistingParent("anvil", tfcLoc("block/rock")).texture();
//
//
//        for (DFCExtendedRock rock : DFCExtendedRock.values()){
//            if ((rock.category().equals(RockCategory.IGNEOUS_EXTRUSIVE)||rock.category().equals(RockCategory.IGNEOUS_INTRUSIVE))&&rock.isDFCRock()) {
//                simpleBlockWithItem(DFCBlocks.DFC_MAGMA_BLOCKS.get(rock).get(), cubeAll(DFCBlocks.DFC_MAGMA_BLOCKS.get(rock).get()));
//                simpleBlockWithItem(DFCBlocks.DFC_ROCK_ANVILS.get(rock).get(), anvilModel);
//            }
//
//        }
//    }
//
//    public ResourceLocation dfcLoc(String name) {
//        return ResourceLocation.fromNamespaceAndPath(DecoFirmaCraft.MOD_ID, name);
//    }
//
//    public ResourceLocation tfcLoc(String name) {
//        return ResourceLocation.fromNamespaceAndPath(TerraFirmaCraft.MOD_ID, name);
//    }
//}
