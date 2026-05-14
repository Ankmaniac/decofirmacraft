//package com.ankmaniac.decofirmacraft.common.entities.minecart;
//
//import com.ankmaniac.decofirmacraft.common.block.IDFCRail;
//import com.ankmaniac.decofirmacraft.common.block.rock.RockRailBlock;
//import com.google.common.collect.Lists;
//import lombok.Getter;
//import lombok.Setter;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntitySelector;
//import net.minecraft.world.entity.vehicle.AbstractMinecart;
//import net.minecraft.world.level.block.BaseRailBlock;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//
//import javax.annotation.Nullable;
//import java.util.*;
//
//public class MinecartTrain {
//    private final MinecartTrainManager handler;
//    private final List<UUID> minecarts;
//    private UUID leader;
//    private UUID rear;
//    private final ServerLevel level;
//    private final long id;
//    private final List<RailInfo> rails;
//    @Getter
//    @Setter
//    private double force = 0;
//
//    public RailInfo getRailInfo(BlockPos pos)
//    {
//        for (RailInfo railInfo : rails)
//        {
//            if (railInfo.getRailPos() != null && railInfo.getRailPos().equals(pos))
//            {
//                return railInfo;
//            }
//        }
//        return null;
//    }
//
//    MinecartTrain(AbstractMinecart leader, ServerLevel level, MinecartTrainManager handler, long id)
//    {
//        this.minecarts = Lists.newArrayList();
//        this.leader = leader.getUUID();
//        this.level = level;
//        this.add(leader);
//        this.handler = handler;
//        this.id = id;
//        this.rails = Lists.newArrayList();
//    }
//
//    MinecartTrain(List<UUID> newMinecarts, ServerLevel level, MinecartTrainManager handler, long id)
//    {
//        this.minecarts = newMinecarts;
//        this.leader = newMinecarts.getFirst();
//        if (newMinecarts.size() > 1)
//        {
//            this.rear = newMinecarts.getLast();
//        }
//        this.level = level;
//        this.handler = handler;
//        this.id = id;
//        for (UUID uuid : this.minecarts)
//        {
//            DFCMinecart minecart = (DFCMinecart) level.getEntity(uuid);
//            assert minecart != null;
//            minecart.setMinecartTrain(this);
//        }
//        //TODO - not sure if we should keep it like this
//        this.rails = Lists.newArrayList();
//    }
//
//    public float getAcceleration(ServerLevel level)
//    {
//        for (UUID uuid : this.minecarts)
//        {
//            float frictionForce = 0;
//            float gravityForce = 0;
//            float trackForce = 0;
//            float pushingForce = 0;
//            float trainMass = 0;
//            DFCMinecart cart = (DFCMinecart) level.getEntity(uuid);
//            if (cart != null)
//            {
//                Vec3 projectedAcceleration = Vec3.ZERO;
//
//                //reduce total added force by friction force
//                //if friction force is greater than all other influences it stays at a standstill
//                float cartFrictionForce = cart.getFrictionForce() / 300f;
//                float cartGravityForce = cart.getGravityForce() / 300f;
//                Vec3 cartPushingForce = cart.getPushingForce();
//                //dont adjust track force for mass it applies the same force to all carts
//                float cartTrackForce = cart.getTrackForce();
//                //add all together to get total influence put onto this cart
//                //also add drag calculations maybe? cart might not get fast enough though
//                float cartMass = cart.getMass();
//
//                //default mass is 300. everything will be balanced around that (for forces and stuff)
//                frictionForce += cartFrictionForce;
//                gravityForce += cartGravityForce;
//                pushingForce += cartTrackForce;
//            }
//            //TODO add all track forces together to get full train forces
//            //Also get their mass. this will determine acceleration. add this to previous speed
//            //then determine per cart what axis and what sign the force is applied to
//        }
//        float totalForce = 0;
//        return totalForce;
//    }
//
//    public void tick(ServerLevel level)
//    {
//        setForce(getForce() + getAcceleration(level));
//        double force = getForce();
//
//        if(this.rails.isEmpty())
//        {
//        }
//        else
//        {
//            for (UUID uuid : this.minecarts)
//            {
//                DFCMinecart cart = (DFCMinecart) level.getEntity(uuid);
//                if (cart != null)
//                {
//                    @Nullable
//                    RailInfo railInfo = cart.getCurrentRail();
//                    //if cart doesnt have a rail assigned
//                    if (railInfo == null)
//                    {
//                        BlockPos pos = cart.blockPosition();
//
//                        if (!level.getBlockState(pos).is(BlockTags.RAILS))
//                        {
//                            pos = pos.below();
//                        }
//                        BlockState state = level.getBlockState(pos);
//                        if (state.is(BlockTags.RAILS) && cart.isOnRails())
//                        {
//                            if(getRailInfo(pos) != null)
//                            {
//                                cart.setCurrentRail(getRailInfo(pos));
//                                railInfo = cart.getCurrentRail();
//                            }
//                            else
//                            {
//                                if(state.getBlock() instanceof IDFCRail)
//                                {
//                                    //TODO - add axis parameters later
//                                    RailInfo newRailInfo = new RailInfo(state, pos, null, RailInfo.getLength(state, level, pos));
//                                    railInfo = newRailInfo;
//                                    cart.setCurrentRail(newRailInfo);
//                                    if (getRailInfo(RailInfo.nextPos(false, pos, state, level)) != null)
//                                    {
//                                        this.rails.add(this.rails.indexOf(getRailInfo(RailInfo.nextPos(false, pos, state, level))), getRailInfo(RailInfo.nextPos(false, pos, state, level)));
//                                    }
//                                }
//                                else if(state.getBlock() instanceof BaseRailBlock)
//                                {
//
//                                }
//                            }
//                        }
//                    }
//                    if (railInfo != null)
//                    {
//                        //add progress to cart, check if it should go to next block
//                        double progress = cart.getProgress() + force;
//                        double length = railInfo.getLength();
//                        boolean negative = cart.isNegative();
//                        if (progress >= length)
//                        {
//                            while (progress >= length)
//                            {
//                                progress = progress - length;
//                                final BlockPos nextPos = RailInfo.nextPos(negative, railInfo.getRailPos(), railInfo.getRailState(), level, cart);
//
//                                if (cart.isNegative() != negative)
//                                {
//                                    negative = cart.isNegative();
//                                }
//                                if (getRailInfo(nextPos) != null)
//                                {
//                                    final RailInfo nextRailInfo = getRailInfo(nextPos);
//                                    length = nextRailInfo.getLength();
//                                    railInfo = nextRailInfo;
//                                }
//                                else if(BaseRailBlock.isRail(level, nextPos))
//                                {
//
//                                }
//                                else
//                                {
//                                    railInfo = null;
//                                }
//                            }
//                            cart.setCurrentRail(railInfo);
//                        }
//                        else if (progress < 0)
//                        {
//                            while (progress < 0)
//                            {
//                                final BlockPos nextPos = RailInfo.nextPos(!negative, railInfo.getRailPos(), railInfo.getRailState(), level, cart);
//
//                                if (cart.isNegative() != negative)
//                                {
//                                    negative = cart.isNegative();
//                                }
//                                if (getRailInfo(nextPos) != null)
//                                {
//                                    final RailInfo nextRailInfo = getRailInfo(nextPos);
//                                    progress = progress + nextRailInfo.getLength();
//                                    railInfo = nextRailInfo;
//                                }
//                                else if(BaseRailBlock.isRail(level, nextPos))
//                                {
//
//                                }
//                                else
//                                {
//                                    railInfo = null;
//                                }
//                            }
//                            cart.setCurrentRail(railInfo);
//                        }
//
//                        //set new progress for cart
//                        cart.setProgress(progress);
//                    }
//                    //update carts position and rotation
//                    cart.updatePosition();
//
//                }
//            }
//        }
//    }
//
//    public void add(AbstractMinecart minecart)
//    {
//        if (minecarts.size() == 1)
//        {
//            rear = minecart.getUUID();
//        }
//        minecarts.add(minecart.getUUID());
//    }
//
//    public void add(DFCMinecart minecart, DFCMinecart cart1, DFCMinecart cart2)
//    {
//        int position1 = minecarts.indexOf(cart1.getUUID());
//        int position2 = minecarts.indexOf(cart2.getUUID());
//
//        minecarts.add(Math.max(position1, position2), minecart.getUUID());
//        minecart.setMinecartTrain(this);
//    }
//
//    public void add(DFCMinecart minecart, DFCMinecart cartAdjacent)
//    {
//        UUID cartId = minecart.getUUID();
//        if (cartAdjacent.isLeader())
//        {
//            minecarts.addFirst(cartId);
//        }
//        else if (cartAdjacent.isRear())
//        {
//            minecarts.addLast(cartId);
//        }
//        else
//        {
//            minecarts.add(getPosition(cartAdjacent), cartId);
//        }
//        minecart.setMinecartTrain(this);
//    }
//
//    public void add(DFCMinecart minecart, int position)
//    {
//        this.minecarts.add(position, minecart.getUUID());
//        minecart.setMinecartTrain(this);
//    }
//
//    public void merge(DFCMinecart adjacentMinecart , DFCMinecart newMinecart)
//    {
//        List<UUID> newMinecarts = newMinecart.getMinecartTrain().getMinecarts();
//
//        assert newMinecart.isLeader() || newMinecart.isRear();
//        if (!newMinecart.isLeader())
//        {
//            newMinecarts = newMinecarts.reversed();
//        }
//
//        assert adjacentMinecart.isLeader() || adjacentMinecart.isRear();
//        final boolean oldIsLeader = adjacentMinecart.isLeader();
//
//        while (!newMinecarts.isEmpty())
//        {
//            UUID cartUUID = newMinecarts.getFirst();
//            if (oldIsLeader)
//            {
//                this.minecarts.addFirst(cartUUID);
//                if(newMinecarts.size() == 1)
//                {
//                    this.leader = cartUUID;
//                }
//            }
//            else
//            {
//                this.minecarts.addLast(cartUUID);
//                if(newMinecarts.size() == 1)
//                {
//                    this.rear = cartUUID;
//                }
//            }
//            ((DFCMinecart) this.level.getEntity(cartUUID)).setMinecartTrain(this);
//            newMinecarts.removeFirst();
//        }
//    }
//
//    public void remove(DFCMinecart minecart, ServerLevel level)
//    {
//        UUID removedCart = minecart.getUUID();
//        if(minecart.isLeader())
//        {
//            this.minecarts.remove(removedCart);
//            if(minecarts.size() == 1)
//            {
//                this.leader = null;
//                this.handler.removeTrain(this);
//            }
//            else
//            {
//                this.leader = minecarts.getFirst();
//            }
//        }
//        else if(minecart.isRear())
//        {
//            this.minecarts.remove(removedCart);
//            if (minecarts.size() > 1)
//            {
//                this.rear = minecarts.getLast();
//            }
//            else
//            {
//                this.rear = null;
//            }
//        }
//        else
//        {
//            int removedPos = minecarts.indexOf(minecart.getUUID());
//
//            List<UUID> visited = Lists.newArrayList();
//
//            rear = null;
//
//            for (int i = removedPos + 1; i <= minecarts.size(); i++)
//            {
//                visited.add(minecarts.get(i));
//            }
//
//            minecarts.removeAll(visited);
//
//            this.minecarts.remove(removedPos);
//
//            if (minecarts.size() > 1)
//            {
//                rear = minecarts.getLast();
//            }
//
//            this.handler.addTrain(visited, level);
//        }
//    }
//
//    public AbstractMinecart getLeader()
//    {
//        return (AbstractMinecart) this.level.getEntity(leader);
//    }
//
//    public AbstractMinecart getRear()
//    {
//        return (AbstractMinecart) this.level.getEntity(rear);
//    }
//
//    public boolean isLeader(AbstractMinecart minecart)
//    {
//        return this.leader.equals(minecart.getUUID());
//    }
//
//    public boolean isRear(AbstractMinecart minecart)
//    {
//        return this.rear.equals(minecart.getUUID());
//    }
//
//    public boolean hasMinecart(AbstractMinecart minecart)
//    {
//        return minecarts.contains(minecart.getUUID());
//    }
//
//    public int getSize()
//    {
//        return this.minecarts.size();
//    }
//
//    public long getId()
//    {
//        return this.id;
//    }
//
//    public List<UUID> getMinecarts()
//    {
//        return this.minecarts;
//    }
//
//    public int getPosition(DFCMinecart minecart)
//    {
//        return this.minecarts.indexOf(minecart.getUUID());
//    }
//
//    public void clear()
//    {
//        this.minecarts.clear();
//        this.leader = null;
//        this.rear = null;
//    }
//}
