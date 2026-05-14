//package com.ankmaniac.decofirmacraft.common.entities.minecart;
//
//import com.ankmaniac.decofirmacraft.common.block.IDFCRail;
//import com.ankmaniac.decofirmacraft.common.block.rock.RockRailBlock;
//import com.ankmaniac.decofirmacraft.util.DFCTags;
//import com.ankmaniac.decofirmacraft.util.tracker.DFCWorldTracker;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import lombok.Getter;
//import lombok.Setter;
//import net.dries007.tfc.common.TFCTags;
//import net.dries007.tfc.util.Helpers;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Vec3i;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntitySelector;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.MoverType;
//import net.minecraft.world.entity.animal.IronGolem;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.vehicle.AbstractMinecart;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.BaseRailBlock;
//import net.minecraft.world.level.block.PoweredRailBlock;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.RailShape;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//
//import javax.annotation.Nullable;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public abstract class DFCMinecart extends AbstractMinecart implements IDFCMinecartExtension
//{
//    private final List<DFCMinecart> connections = Lists.newArrayList();
//    private MinecartTrain minecartTrain;
//
//    //Why the fuck would they make these private
//    private boolean onRails;
//    private int lerpSteps;
//    private double lerpX;
//    private double lerpY;
//    private double lerpZ;
//    private double lerpYRot;
//    private double lerpXRot;
//    private boolean flipped;
//    private Vec3 targetDeltaMovement = Vec3.ZERO;
//
//    @Nullable
//    @Setter
//    @Getter
//    private RailInfo currentRail;
//
//    @Setter
//    @Getter
//    private float mass;
//
//    @Setter
//    @Getter
//    private boolean negative = false;
//
//    @Getter
//    @Setter
//    private double progress = 0;
//
//    public List<DFCMinecart> getConnections() {
//        return this.connections;
//    }
//
//    protected DFCMinecart(EntityType<?> entityType, Level level, float initialMass) {
//        super(entityType, level);
//        this.blocksBuilding = true;
//        this.mass = initialMass;
//    }
//
//    protected DFCMinecart(EntityType<?> entityType, Level level, double x, double y, double z, float initialMass) {
//        this(entityType, level, initialMass);
//        this.setPos(x, y, z);
//        this.xo = x;
//        this.yo = y;
//        this.zo = z;
//    }
//
//    @Override
//    public InteractionResult interact(Player player, InteractionHand hand)
//    {
//        if (this.isAlive() && Helpers.isItem(player.getItemInHand(hand).getItem(), TFCTags.Items.TOOLS_HAMMER))
//        {
//            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel)
//            {
//                MinecartTrainManager trainManager = DFCWorldTracker.get(this.level()).getTrainManager();
//
//                AABB box;
//                if (getCollisionHandler() != null) box = getCollisionHandler().getMinecartCollisionBox(this);
//                else box = getBoundingBox().inflate(0.2F, 0.0D, 0.2F);
//                List<Entity> list = serverLevel.getEntities(this, box, EntitySelector.pushableBy(this));
//
//                Set<DFCMinecart> minecarts = new HashSet<>();
//
//                for (Entity entity : list)
//                {
//                    if (entity instanceof DFCMinecart adjacentMinecart && adjacentMinecart.getMinecartTrain() != this.minecartTrain)
//                    {
//                        minecarts.add(adjacentMinecart);
//                    }
//                }
//                switch(minecarts.size())
//                {
//                    case 0:
//                    {
//                        if (this.minecartTrain.getSize() > 1)
//                        {
//                            trainManager.disconnect(this, serverLevel);
//                            return InteractionResult.SUCCESS;
//                        }
//                        return InteractionResult.SUCCESS_NO_ITEM_USED;
//                    }
//                    case 1:
//                    {
//                        DFCMinecart adjacentMinecart = minecarts.iterator().next();
//                        if(trainManager.addTo(this, adjacentMinecart))
//                        {
//                            return InteractionResult.SUCCESS;
//                        }
//                        break;
//                    }
//                    case 2:
//                    {
//                        if (this.isLeader() || this.isRear())
//                        {
//                            DFCMinecart adjacentMinecart1 = minecarts.iterator().next();
//                            DFCMinecart adjacentMinecart2 = minecarts.iterator().next();
//                            if (trainManager.addTo(this, adjacentMinecart1, adjacentMinecart2))
//                            {
//                                return InteractionResult.SUCCESS;
//                            }
//                        }
//                        break;
//                    }
//                    //3 or more minecarts close by
//                    default:
//                    {
//                        if (this.minecartTrain.getSize() > 1)
//                        {
//                            trainManager.disconnect(this, serverLevel);
//                            return InteractionResult.SUCCESS;
//                        }
//                        return InteractionResult.SUCCESS_NO_ITEM_USED;
//                    }
//                }
//            }
//
//        }
//
//        return InteractionResult.PASS;
//    }
//
//    @Override
//    public void tick() {
//        if (this.isLeader())
//        {
//
//        }
//        if (this.getHurtTime() > 0) {
//            this.setHurtTime(this.getHurtTime() - 1);
//        }
//
//        if (this.getDamage() > 0.0F) {
//            this.setDamage(this.getDamage() - 1.0F);
//        }
//
//        this.checkBelowWorld();
//        this.handlePortal();
//        if (this.level().isClientSide) {
//            if (this.lerpSteps > 0) {
//                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
//                this.lerpSteps--;
//            } else {
//                this.reapplyPosition();
//                this.setRot(this.getYRot(), this.getXRot());
//            }
//        } else {
//            this.applyGravity();
//            int i = Mth.floor(this.getX());
//            int j = Mth.floor(this.getY());
//            int k = Mth.floor(this.getZ());
//            if (this.level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
//                j--;
//            }
//
//            BlockPos blockpos = new BlockPos(i, j, k);
//            BlockState blockstate = this.level().getBlockState(blockpos);
//            this.onRails = BaseRailBlock.isRail(blockstate);
//            if (canUseRail() && this.onRails) {
//                this.moveAlongTrack(blockpos, blockstate);
//                if (blockstate.getBlock() instanceof PoweredRailBlock && ((PoweredRailBlock) blockstate.getBlock()).isActivatorRail()) {
//                    this.activateMinecart(i, j, k, blockstate.getValue(PoweredRailBlock.POWERED));
//                }
//            } else {
//                this.comeOffTrack();
//            }
//
//            this.checkInsideBlocks();
//            this.setXRot(0.0F);
//            double d0 = this.xo - this.getX();
//            double d1 = this.zo - this.getZ();
//            if (d0 * d0 + d1 * d1 > 0.001) {
//                this.setYRot((float)(Mth.atan2(d1, d0) * 180.0 / Math.PI));
//                if (this.flipped) {
//                    this.setYRot(this.getYRot() + 180.0F);
//                }
//            }
//
//            double d2 = (double)Mth.wrapDegrees(this.getYRot() - this.yRotO);
//            if (d2 < -170.0 || d2 >= 170.0) {
//                this.setYRot(this.getYRot() + 180.0F);
//                this.flipped = !this.flipped;
//            }
//
//            this.setRot(this.getYRot(), this.getXRot());
//            AABB box;
//            if (getCollisionHandler() != null) box = getCollisionHandler().getMinecartCollisionBox(this);
//            else box = this.getBoundingBox().inflate(0.2F, 0.0D, 0.2F);
//            if (canBeRidden() && this.getDeltaMovement().horizontalDistanceSqr() > 0.01D) {
//                List<Entity> list = this.level().getEntities(this, box, EntitySelector.pushableBy(this));
//                if (!list.isEmpty()) {
//                    for (Entity entity1 : list) {
//                        if (!(entity1 instanceof Player)
//                                && !(entity1 instanceof IronGolem)
//                                && !(entity1 instanceof AbstractMinecart)
//                                && !this.isVehicle()
//                                && !entity1.isPassenger()) {
//                            entity1.startRiding(this);
//                        } else {
//                            entity1.push(this);
//                        }
//                    }
//                }
//            } else {
//                for(Entity entity : this.level().getEntities(this, box)) {
//                    if (!this.hasPassenger(entity) && entity.isPushable() && entity instanceof AbstractMinecart) {
//                        entity.push(this);
//                    }
//                }
//            }
//
//            this.updateInWaterStateAndDoFluidPushing();
//            if (this.isInLava()) {
//                this.lavaHurt();
//                this.fallDistance *= 0.5F;
//            }
//
//            this.firstTick = false;
//        }
//    }
//
//    protected void moveAlongTrack(BlockPos pos, BlockState state) {
//        this.resetFallDistance();
//        double d0 = this.getX();
//        double d1 = this.getY();
//        double d2 = this.getZ();
//        Vec3 vec3 = this.getPos(d0, d1, d2);
//        d1 = (double)pos.getY();
//        boolean flag = false;
//        boolean flag1 = false;
//        BaseRailBlock baserailblock = (BaseRailBlock) state.getBlock();
//        if (baserailblock instanceof PoweredRailBlock && !((PoweredRailBlock) baserailblock).isActivatorRail()) {
//            flag = state.getValue(PoweredRailBlock.POWERED);
//            flag1 = !flag;
//        }
//
//        double d3 = getSlopeAdjustment();
//        if (this.isInWater()) {
//            d3 *= 0.2;
//        }
//
//        Vec3 vec31 = this.getDeltaMovement();
//        //If the rail is ascending it adds this flat number to the speed in that direction
//        //(speeds and slows by flat amount)
//        RailShape railshape = ((BaseRailBlock)state.getBlock()).getRailDirection(state, this.level(), pos, this);
//        switch (railshape) {
//            case ASCENDING_EAST:
//                this.setDeltaMovement(vec31.add(-d3, 0.0, 0.0));
//                d1++;
//                break;
//            case ASCENDING_WEST:
//                this.setDeltaMovement(vec31.add(d3, 0.0, 0.0));
//                d1++;
//                break;
//            case ASCENDING_NORTH:
//                this.setDeltaMovement(vec31.add(0.0, 0.0, d3));
//                d1++;
//                break;
//            case ASCENDING_SOUTH:
//                this.setDeltaMovement(vec31.add(0.0, 0.0, -d3));
//                d1++;
//        }
//
//        vec31 = this.getDeltaMovement();
//        Pair<Vec3i, Vec3i> pair = exits(railshape);
//        Vec3i vec3i = pair.getFirst();
//        Vec3i vec3i1 = pair.getSecond();
//        double d4 = (double)(vec3i1.getX() - vec3i.getX());
//        double d5 = (double)(vec3i1.getZ() - vec3i.getZ());
//        double d6 = Math.sqrt(d4 * d4 + d5 * d5);
//        double d7 = vec31.x * d4 + vec31.z * d5;
//        if (d7 < 0.0) {
//            d4 = -d4;
//            d5 = -d5;
//        }
//
//        //caps speed
//        double d8 = Math.min(2.0, vec31.horizontalDistance());
//        //multiplies horizontal axis speeds by the speed and divided by sqrt2 if it was diagonal
//        vec31 = new Vec3(d8 * d4 / d6, vec31.y, d8 * d5 / d6);
//        this.setDeltaMovement(vec31);
//        Entity entity = this.getFirstPassenger();
//        if (entity instanceof Player) {
//            Vec3 vec32 = entity.getDeltaMovement();
//            double d9 = vec32.horizontalDistanceSqr();
//            double d11 = this.getDeltaMovement().horizontalDistanceSqr();
//            if (d9 > 1.0E-4 && d11 < 0.01) {
//                //Player moving the cart if its speed is low enough already
//                this.setDeltaMovement(this.getDeltaMovement().add(vec32.x * 0.1, 0.0, vec32.z * 0.1));
//                flag1 = false;
//            }
//        }
//
//        //checks if the rail is an off powered rail
//        //or if the player is  moving the cart (only possible on low speeds)
//        if (flag1 && shouldDoRailFunctions()) {
//            double d22 = this.getDeltaMovement().horizontalDistance();
//            if (d22 < 0.03) {
//                //if its slow enough stop the cart
//                this.setDeltaMovement(Vec3.ZERO);
//            } else {
//                //halve the carts speed every tick
//                this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.0, 0.5));
//            }
//        }
//
//        //gets change in x and z (from the rail direction)
//        double d23 = (double)pos.getX() + 0.5 + (double)vec3i.getX() * 0.5;
//        double d10 = (double)pos.getZ() + 0.5 + (double)vec3i.getZ() * 0.5;
//        double d12 = (double)pos.getX() + 0.5 + (double)vec3i1.getX() * 0.5;
//        double d13 = (double)pos.getZ() + 0.5 + (double)vec3i1.getZ() * 0.5;
//        d4 = d12 - d23;
//        d5 = d13 - d10;
//        double d14;
//        if (d4 == 0.0) {
//            d14 = d2 - (double)pos.getZ();
//        } else if (d5 == 0.0) {
//            d14 = d0 - (double)pos.getX();
//        } else {
//            double d15 = d0 - d23;
//            double d16 = d2 - d10;
//            d14 = (d15 * d4 + d16 * d5) * 2.0;
//        }
//
//        //aligns with rail
//        d0 = d23 + d4 * d14;
//        d2 = d10 + d5 * d14;
//        this.setPos(d0, d1, d2);
//        this.moveMinecartOnRail(pos);
//        //if this is on a slope and it crosses a block border it goes down a block(?)
//        //probably because it doesnt want the minecart to cross either of the 2 blocks
//        //to the top or bottom of diagonal rails
//        //why would they make this so dynamic all rails have the same height change
//        if (vec3i.getY() != 0 && Mth.floor(this.getX()) - pos.getX() == vec3i.getX() && Mth.floor(this.getZ()) - pos.getZ() == vec3i.getZ()) {
//            this.setPos(this.getX(), this.getY() + (double)vec3i.getY(), this.getZ());
//        } else if (vec3i1.getY() != 0 && Mth.floor(this.getX()) - pos.getX() == vec3i1.getX() && Mth.floor(this.getZ()) - pos.getZ() == vec3i1.getZ()
//        )
//        {
//            this.setPos(this.getX(), this.getY() + (double)vec3i1.getY(), this.getZ());
//        }
//
//        //multiplies movement vector for slowdown
//        //if it has a passenger it slows down slower
//        //alter to take mass into account instead of a flat multiplier
//        this.applyNaturalSlowdown();
//        Vec3 vec33 = this.getPos(this.getX(), this.getY(), this.getZ());
//        //checks if null because new or old getPos mightve been on a non rail(returns null)
//        if (vec33 != null && vec3 != null) {
//            //5% of height change is divided by movement speed and added to it
//            //(slows down or speeds up by percentage of current speed)
//            //this is kinda dumb why would its a flat multiplier either way
//            //probably just in case if it got on or off a rail on the same tick (reduced effect)
//            double d17 = (vec3.y - vec33.y) * 0.05;
//            Vec3 vec34 = this.getDeltaMovement();
//            double d18 = vec34.horizontalDistance();
//            if (d18 > 0.0) {
//                this.setDeltaMovement(vec34.multiply((d18 + d17) / d18, 1.0, (d18 + d17) / d18));
//            }
//
//            //sets height as new calculated height
//            this.setPos(this.getX(), vec33.y, this.getZ());
//        }
//
//        int j = Mth.floor(this.getX());
//        int i = Mth.floor(this.getZ());
//        //if blockpos changes horizontally
//        if (j != pos.getX() || i != pos.getZ()) {
//            Vec3 vec35 = this.getDeltaMovement();
//            double d26 = vec35.horizontalDistance();
//            //sets all movement to be in the direction it crossed block borders from
//            this.setDeltaMovement(d26 * (double)(j - pos.getX()), vec35.y, d26 * (double)(i - pos.getZ()));
//        }
//
//        if (shouldDoRailFunctions())
//            //rail does its own internal stuff
//            baserailblock.onMinecartPass(state, level(), pos, this);
//
//        //if the rail is powered
//        if (flag && shouldDoRailFunctions()) {
//            Vec3 vec36 = this.getDeltaMovement();
//            double d27 = vec36.horizontalDistance();
//            //0.01 is the max speed of a passenger pushed minecart
//            if (d27 > 0.01) {
//                //adds 0.06 to the current speed/direction if it is moving
//                double d19 = 0.06;
//                this.setDeltaMovement(vec36.add(vec36.x / d27 * 0.06, 0.0, vec36.z / d27 * 0.06));
//            } else {
//                //if speed is lower than 0.01
//                //checks if any block to the ends of the rail is a full block (also conductive)
//                //gives the cart a push if this is the case
//                Vec3 vec37 = this.getDeltaMovement();
//                double d20 = vec37.x;
//                double d21 = vec37.z;
//                if (railshape == RailShape.EAST_WEST) {
//                    if (this.isRedstoneConductor(pos.west())) {
//                        d20 = 0.02;
//                    } else if (this.isRedstoneConductor(pos.east())) {
//                        d20 = -0.02;
//                    }
//                } else {
//                    if (railshape != RailShape.NORTH_SOUTH) {
//                        return;
//                    }
//
//                    if (this.isRedstoneConductor(pos.north())) {
//                        d21 = 0.02;
//                    } else if (this.isRedstoneConductor(pos.south())) {
//                        d21 = -0.02;
//                    }
//                }
//
//                this.setDeltaMovement(d20, vec37.y, d21);
//            }
//        }
//    }
//
//    @Nullable
//    public Vec3 getPos(double x, double y, double z) {
//        int i = Mth.floor(x);
//        int j = Mth.floor(y);
//        int k = Mth.floor(z);
//        if (this.level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
//            j--;
//        }
//
//        BlockState blockstate = this.level().getBlockState(new BlockPos(i, j, k));
//        if (BaseRailBlock.isRail(blockstate)) {
//            RailShape railshape = ((BaseRailBlock)blockstate.getBlock()).getRailDirection(blockstate, this.level(), new BlockPos(i, j, k), this);
//            Pair<Vec3i, Vec3i> pair = exits(railshape);
//            Vec3i vec3i = pair.getFirst();
//            Vec3i vec3i1 = pair.getSecond();
//            double d0 = (double)i + 0.5 + (double)vec3i.getX() * 0.5;
//            double d1 = (double)j + 0.0625 + (double)vec3i.getY() * 0.5;
//            double d2 = (double)k + 0.5 + (double)vec3i.getZ() * 0.5;
//            double d3 = (double)i + 0.5 + (double)vec3i1.getX() * 0.5;
//            double d4 = (double)j + 0.0625 + (double)vec3i1.getY() * 0.5;
//            double d5 = (double)k + 0.5 + (double)vec3i1.getZ() * 0.5;
//            double d6 = d3 - d0;
//            double d7 = (d4 - d1) * 2.0;
//            double d8 = d5 - d2;
//            double d9;
//            if (d6 == 0.0) {
//                d9 = z - (double)k;
//            } else if (d8 == 0.0) {
//                d9 = x - (double)i;
//            } else {
//                double d10 = x - d0;
//                double d11 = z - d2;
//                d9 = (d10 * d6 + d11 * d8) * 2.0;
//            }
//
//            x = d0 + d6 * d9;
//            y = d1 + d7 * d9;
//            z = d2 + d8 * d9;
//            if (d7 < 0.0) {
//                y++;
//            } else if (d7 > 0.0) {
//                y += 0.5;
//            }
//
//            return new Vec3(x, y, z);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public void moveMinecartOnRail(BlockPos pos) { //Non-default because getMaximumSpeed is protected
//        AbstractMinecart mc = this;
//        double d24 = mc.isVehicle() ? 0.75D : 1.0D;
//        double d25 = mc.getMaxSpeedWithRail();
//        Vec3 vec3d1 = mc.getDeltaMovement();
//        mc.move(MoverType.SELF, new Vec3(Mth.clamp(d24 * vec3d1.x, -d25, d25), 0.0D, Mth.clamp(d24 * vec3d1.z, -d25, d25)));
//    }
//
//    public void tickFollow()
//    {
//
//    }
//
//    public float getTrackForce()
//    {
//        float trackForce = 0.0F;
//
//        BlockPos pos = blockPosition();
//        if (!this.level().getBlockState(pos).is(BlockTags.RAILS))
//        {
//            pos = pos.below();
//        }
//        BlockState state = this.level().getBlockState(pos);
//        if (state.is(BlockTags.RAILS) && isOnRails())
//        {
//            if(state.getBlock() instanceof IDFCRail railBlock)
//            {
//                trackForce = railBlock.boostCart();
//            }
//        }
//
//        return !this.negative ? trackForce : -trackForce;
//    }
//
//    public float getGravityForce()
//    {
//        float gravityForce = 0.0F;
//
//        BlockPos pos = blockPosition();
//        if (!this.level().getBlockState(pos).is(BlockTags.RAILS))
//        {
//            pos = pos.below();
//        }
//        BlockState state = this.level().getBlockState(pos);
//        if (state.is(BlockTags.RAILS) && isOnRails())
//        {
//            if(state.getBlock() instanceof BaseRailBlock railBlock)
//            {
//                if (state.getValue(RockRailBlock.SHAPE).isAscending())
//                {
//                    gravityForce =(float) (this.getMass() * getGravity() * (1 / Math.sqrt(2d)));
//                }
//            }
//        }
//
//        return !this.negative ? gravityForce :  -gravityForce;
//    }
//
//    public float getFrictionForce()
//    {
//        float normalForce = (float) (this.getMass() * getGravity());
//        float frictionForce = 0.0F;
//
//        BlockPos pos = blockPosition();
//        if (!this.level().getBlockState(pos).is(BlockTags.RAILS))
//        {
//            pos = pos.below();
//        }
//        BlockState state = this.level().getBlockState(pos);
//        if (state.is(BlockTags.RAILS) && isOnRails())
//        {
//            if(state.getBlock() instanceof BaseRailBlock railBlock)
//            {
//                if (state.getValue(RockRailBlock.SHAPE).isAscending())
//                {
//                    normalForce =(float) (this.getMass() * getGravity() * (1 / Math.sqrt(2d)));
//                }
//            }
//        }
//
//        if (this.getDeltaMovement().horizontalDistanceSqr() > 0.01)
//        {
//            frictionForce = normalForce * this.getKineticFrictionCoefficient();
//        }
//        else
//        {
//            frictionForce = normalForce * this.getStaticFrictionCoefficient();
//        }
//        return !this.negative ? frictionForce : -frictionForce;
//    }
//
//    public Vec3 getPushingForce()
//    {
//        Vec3 pushingForce = null;
//        BlockPos pos = blockPosition();
//        if (!this.level().getBlockState(pos).is(BlockTags.RAILS))
//        {
//            pos = pos.below();
//        }
//        BlockState state = this.level().getBlockState(pos);
//        if (state.is(BlockTags.RAILS) && isOnRails())
//        {
//            if(state.getBlock() instanceof BaseRailBlock railBlock)
//            {
//                Entity entity = this.getFirstPassenger();
//                if (entity instanceof Player)
//                {
//                    Vec3 vec32 = entity.getDeltaMovement();
//                    double d9 = vec32.horizontalDistanceSqr();
//                    double d11 = this.getDeltaMovement().horizontalDistanceSqr();
//                    if (d9 > 1.0E-4 && d11 < 0.01)
//                    {
//                        pushingForce = Vec3.ZERO.add(vec32.x * .1, 0, vec32.z * .1);
//                    }
//                }
//            }
//        }
//        return pushingForce;
//    }
//
//    //default numbers are for metal on dirt or metal on metal
//    public float getKineticFrictionCoefficient()
//    {
//        BlockPos pos = blockPosition();
//        if (!this.level().getBlockState(pos).is(BlockTags.RAILS))
//        {
//            pos = pos.below();
//        }
//        BlockState state = this.level().getBlockState(pos);
//        if (state.is(BlockTags.RAILS) && isOnRails())
//        {
//            if(state.getBlock() instanceof IDFCRail dfcRail)
//            {
//                //TODO come up with better solution for different type carts
//                //maybe feed current cart type and it can decide based on what type it is
//                return dfcRail.getKineticFrictionCoefficient();
//            }
//            return 0.4f;
//        }
//        else if (state.is(DFCTags.Blocks.LOW_FRICTION_BLOCKS))
//        {
//            //do NOT use this to make ultra efficient ice rails
//            return 0.1f;
//        }
//        return 0.7F;
//    }
//
//    //same numbers for now
//    //TODO - CHANGE TO REAL NUMBERS
//    private float getStaticFrictionCoefficient()
//    {
//        BlockPos pos = blockPosition();
//        if (!this.level().getBlockState(pos).is(BlockTags.RAILS))
//        {
//            pos = pos.below();
//        }
//        BlockState state = this.level().getBlockState(pos);
//        if (state.is(BlockTags.RAILS) && isOnRails())
//        {
//            if(state.getBlock() instanceof IDFCRail dfcRail)
//            {
//                //come up with better solution for different type carts
//                return dfcRail.getKineticFrictionCoefficient();
//            }
//            return 0.4f;
//        }
//        else if (state.is(DFCTags.Blocks.LOW_FRICTION_BLOCKS))
//        {
//            //do NOT use this to make ultra efficient ice rails
//            return 0.1f;
//        }
//        return 0.7F;
//    }
//
//    public void setMinecartTrain(MinecartTrain minecartTrain)
//    {
//        this.minecartTrain = minecartTrain;
//    }
//
//    public MinecartTrain getMinecartTrain()
//    {
//        return this.minecartTrain;
//    }
//
//    public boolean isLeader()
//    {
//        return this.minecartTrain.isLeader(this);
//    }
//
//    public boolean isRear()
//    {
//        return this.minecartTrain.isRear(this);
//    }
//
//    @Override
//    public boolean isOnRails() {
//        return this.onRails;
//    }
//
//    @Override
//    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
//        this.lerpX = x;
//        this.lerpY = y;
//        this.lerpZ = z;
//        this.lerpYRot = (double)yRot;
//        this.lerpXRot = (double)xRot;
//        this.lerpSteps = steps + 2;
//        this.setDeltaMovement(this.targetDeltaMovement);
//    }
//
//    @Override
//    public double lerpTargetX() {
//        return this.lerpSteps > 0 ? this.lerpX : this.getX();
//    }
//
//    @Override
//    public double lerpTargetY() {
//        return this.lerpSteps > 0 ? this.lerpY : this.getY();
//    }
//
//    @Override
//    public double lerpTargetZ() {
//        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
//    }
//
//    @Override
//    public float lerpTargetXRot() {
//        return this.lerpSteps > 0 ? (float)this.lerpXRot : this.getXRot();
//    }
//
//    @Override
//    public float lerpTargetYRot() {
//        return this.lerpSteps > 0 ? (float)this.lerpYRot : this.getYRot();
//    }
//
//    /**
//     * Updates the entity motion clientside, called by packets from the server
//     */
//    @Override
//    public void lerpMotion(double x, double y, double z) {
//        this.targetDeltaMovement = new Vec3(x, y, z);
//        this.setDeltaMovement(this.targetDeltaMovement);
//    }
//
//    private boolean isRedstoneConductor(BlockPos pos) {
//        return this.level().getBlockState(pos).isRedstoneConductor(this.level(), pos);
//    }
//
//    public static enum DFCTypes
//    {
//        WOODEN_MINECART;
//    }
//}
