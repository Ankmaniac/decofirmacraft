//package com.ankmaniac.decofirmacraft.util.tracker;
//
//import com.ankmaniac.decofirmacraft.DFCAttachments;
//import com.ankmaniac.decofirmacraft.common.entities.minecart.MinecartTrainManager;
//import net.dries007.tfc.util.rotation.RotationNetworkManager;
//import net.dries007.tfc.util.tracker.Collapse;
//import net.dries007.tfc.util.tracker.TickEntry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.LongArrayTag;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.levelgen.RandomSupport;
//import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
//
//public final class DFCWorldTracker
//{
//    public static DFCWorldTracker get(Level level)
//    {
//        return level.getData(DFCAttachments.DFC_WORLD_TRACKER);
//    }
//
//    private final Level level;
//    private final RandomSource random;
//
//    private final MinecartTrainManager  trainManager =  new MinecartTrainManager();
//
//    public DFCWorldTracker(Level level)
//    {
//        this.level = level;
//        this.random = new XoroshiroRandomSource(RandomSupport.generateUniqueSeed());
//    }
//
//    public MinecartTrainManager getTrainManager()
//    {
//        return trainManager;
//    }
//}
