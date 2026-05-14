//package com.ankmaniac.decofirmacraft.common.entities.minecart;
//
//import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
//import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
//import net.minecraft.server.level.ServerLevel;
//
//import java.util.List;
//import java.util.UUID;
//
//public class MinecartTrainManager {
//    private final Long2ObjectMap<MinecartTrain> minecartHandlers;
//    private long nextTrainId;
//
//    public MinecartTrainManager()
//    {
//        this.minecartHandlers = new Long2ObjectOpenHashMap<>();
//        this.nextTrainId = 0;
//    }
//
//    public void add(DFCMinecart minecart, ServerLevel level)
//    {
//        if(minecart.getMinecartTrain() != null)
//        {
//            MinecartTrain minecartTrain = new MinecartTrain(minecart, level, this, nextTrainId);
//            this.minecartHandlers.put(nextTrainId, minecartTrain);
//
//            nextTrainId++;
//        }
//    }
//
//    public boolean addTo(DFCMinecart minecart, DFCMinecart adjacentMinecart)
//    {
//        final MinecartTrain minecartTrain = minecart.getMinecartTrain();
//        final MinecartTrain adjacentTrain = adjacentMinecart.getMinecartTrain();
//
//        if(minecartTrain.getSize() == 1)
//        {
//            if(adjacentTrain.getSize() == 1)
//            {
//                adjacentTrain.add(minecart);
//            }
//            else
//            {
//                adjacentTrain.add(minecart, adjacentMinecart);
//            }
//            removeTrain(minecartTrain);
//            return true;
//        }
//        else
//        {
//            if(adjacentTrain.getSize() == 1)
//            {
//                minecartTrain.add(adjacentMinecart, minecart);
//                removeTrain(adjacentTrain);
//                return true;
//            }
//            else if(adjacentMinecart.isLeader()|| adjacentMinecart.isRear())
//            {
//                int trainSize1 = minecartTrain.getSize();
//                int trainSize2 = adjacentTrain.getSize();
//
//                if(trainSize1 > trainSize2)
//                {
//                    merge(minecart, adjacentMinecart);
//                }
//                else if (trainSize1 < trainSize2)
//                {
//                    merge(adjacentMinecart, minecart);
//                }
//                else if (trainSize1 == trainSize2)
//                {
//                    //If this fails theres something seriously wrong
//                    assert minecartTrain.getId() != adjacentTrain.getId();
//                    if (minecartTrain.getId() < adjacentTrain.getId())
//                    {
//                        merge(minecart, adjacentMinecart);
//                    }
//                    else
//                    {
//                        merge(adjacentMinecart, minecart);
//                    }
//                }
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean addTo(DFCMinecart minecart, DFCMinecart minecart1, DFCMinecart minecart2)
//    {
//        final MinecartTrain minecartTrain = minecart.getMinecartTrain();
//        final MinecartTrain minecartTrain1 = minecart1.getMinecartTrain();
//        final MinecartTrain minecartTrain2 = minecart2.getMinecartTrain();
//
//        if (minecartTrain.getSize() > 1 && (minecart1.isLeader() || minecart1.isRear()) && (minecart2.isLeader() || minecart2.isRear()))
//        {
//            if (minecartTrain1.getId() != minecartTrain2.getId())
//            {
//                int trainSize1 = minecartTrain1.getSize();
//                int trainSize2 = minecartTrain2.getSize();
//                if (trainSize1 > 1 ||  trainSize2 > 1)
//                {
//                    if (trainSize1 != trainSize2)
//                    {
//                        if (trainSize1 > trainSize2)
//                        {
//                            minecartTrain1.add(minecart, minecart1);
//                            removeTrain(minecartTrain);
//                            merge(minecart, minecart2);
//                        }
//                        else
//                        {
//                            minecartTrain2.add(minecart, minecart2);
//                            removeTrain(minecartTrain);
//                            merge(minecart, minecart1);
//                        }
//                    }
//                    else
//                    {
//                        if (minecartTrain1.getId() < minecartTrain2.getId())
//                        {
//                            minecartTrain1.add(minecart, minecart1);
//                            removeTrain(minecartTrain);
//                            merge(minecart, minecart2);
//                        }
//                        else
//                        {
//                            minecartTrain2.add(minecart, minecart2);
//                            removeTrain(minecartTrain);
//                            merge(minecart, minecart1);
//                        }
//                    }
//                    return true;
//                }
//                else
//                {
//                    assert minecartTrain1.getId() != minecartTrain2.getId();
//                    if (minecartTrain1.getId() < minecartTrain2.getId())
//                    {
//                        minecartTrain1.add(minecart, minecart1);
//                        removeTrain(minecartTrain);
//                        minecartTrain1.add(minecart2, minecart);
//                        removeTrain(minecartTrain2);
//                    }
//                    else
//                    {
//                        minecartTrain2.add(minecart, minecart2);
//                        removeTrain(minecartTrain);
//                        minecartTrain2.add(minecart1, minecart);
//                        removeTrain(minecartTrain1);
//                    }
//                    return true;
//                }
//            }
//            else
//            {
//                final int position1 = minecartTrain1.getPosition(minecart1);
//                final int position2 = minecartTrain1.getPosition(minecart2);
//                if (position1 == position2 + 1 || position1 == position2 -1)
//                {
//                    minecartTrain1.add(minecart, Math.max(position1, position2));
//                    removeTrain(minecartTrain);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public void merge(DFCMinecart minecart1, DFCMinecart minecart2)
//    {
//        MinecartTrain minecartTrain1 = minecart1.getMinecartTrain();
//        MinecartTrain minecartTrain2 = minecart2.getMinecartTrain();
//        //Minecart1/train1 will get merged into by cart2/train2
//        //Take order of both minecarts into account for whether to add new carts to front or back,
//        //going forwards or backwards from new carts
//        //Cart 1 leader, add to front, if rear add to end
//        //Cart 2 leader, add normally, if rear add in reverse
//        //Delete train 2 afterwards
//        minecartTrain1.merge(minecart1, minecart2);
//        removeTrain(minecartTrain2);
//    }
//
//    public void addTrain(List<UUID> newMinecarts, ServerLevel serverLevel)
//    {
//        MinecartTrain minecartTrain = new MinecartTrain(newMinecarts, serverLevel, this, nextTrainId);
//        this.minecartHandlers.put(nextTrainId, minecartTrain);
//
//        nextTrainId++;
//    }
//
//    public void disconnect(DFCMinecart minecart, ServerLevel level)
//    {
//        minecart.getMinecartTrain().remove(minecart, level);
//        add(minecart, level);
//    }
//
//    public void removeTrain(MinecartTrain minecartTrain)
//    {
//        minecartTrain.clear();
//        this.minecartHandlers.remove(minecartTrain.getId());
//    }
//
//    public MinecartTrain getTrain(long id)
//    {
//        return this.minecartHandlers.get(id);
//    }
//}
