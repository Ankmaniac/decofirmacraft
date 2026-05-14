//package com.ankmaniac.decofirmacraft.common.entities.minecart;
//
//import com.ankmaniac.decofirmacraft.common.block.IDFCRail;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.block.BaseRailBlock;
//import net.minecraft.world.level.block.RailState;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.RailShape;
//
//import javax.annotation.Nullable;
//
////FUCK records why are they immutable
//@Setter
//@Getter
//@AllArgsConstructor
//@RequiredArgsConstructor
//public class RailInfo
//{
//    private BlockState railState;
//    private BlockPos railPos;
//    @Nullable
//    private Direction.Axis axis;
//    private double length;
//
//    public static BlockPos nextPos(boolean negative, BlockPos currentRailPos, BlockState currentRailState, ServerLevel level, DFCMinecart minecart)
//    {
//        if (currentRailState instanceof IDFCRail)
//        {
//            return ((IDFCRail) currentRailState).nextRailPos(negative, currentRailPos);
//        }
//        else if (BaseRailBlock.isRail(currentRailState))
//        {
//            BaseRailBlock baseRailBlock = (BaseRailBlock) currentRailState.getBlock();
//            RailShape railState = baseRailBlock.getRailDirection(currentRailState, level, currentRailPos, null);
//            BlockPos nextPos = null;
//
//            switch (railState)
//            {
//                case EAST_WEST:
//                {
//                    nextPos = !negative ? currentRailPos.east() : currentRailPos.west();
//                    break;
//                }
//                case NORTH_SOUTH:
//                {
//                    nextPos = !negative ? currentRailPos.north() : currentRailPos.south();
//                    break;
//                }
//                case NORTH_EAST:
//                {
//                    if (negative)
//                    {
//                        minecart.setNegative(!minecart.isNegative());
//                    }
//                    nextPos = !negative ? currentRailPos.north() : currentRailPos.east();
//                    break;
//                }
//                case NORTH_WEST:
//                {
//                    nextPos = !negative ? currentRailPos.north() : currentRailPos.west();
//                    break;
//                }
//                case SOUTH_EAST:
//                {
//                    nextPos = !negative ? currentRailPos.east() : currentRailPos.south();
//                    break;
//                }
//                case SOUTH_WEST:
//                {
//                    if (negative)
//                    {
//                        minecart.setNegative(!minecart.isNegative());
//                    }
//                    nextPos = !negative ? currentRailPos.west() : currentRailPos.south();
//                    break;
//                }
//                case ASCENDING_NORTH:
//                {
//                    nextPos = !negative ? currentRailPos.north().above() : BaseRailBlock.isRail(level.getBlockState(currentRailPos.south().below())) ? currentRailPos.south().below() : currentRailPos.south();
//                    break;
//                }
//                case ASCENDING_EAST:
//                {
//                    nextPos = !negative ? currentRailPos.east().above() : BaseRailBlock.isRail(level.getBlockState(currentRailPos.west().below())) ? currentRailPos.west().below() : currentRailPos.west();
//                    break;
//                }
//                case ASCENDING_SOUTH:
//                {
//                    nextPos = !negative ? BaseRailBlock.isRail(level.getBlockState(currentRailPos.north().below())) ? currentRailPos.north().below() : currentRailPos.north() : currentRailPos.south().above();
//                    break;
//                }
//                case ASCENDING_WEST:
//                {
//                    nextPos = !negative ? BaseRailBlock.isRail(level.getBlockState(currentRailPos.east().below())) ? currentRailPos.east().below() : currentRailPos.east() :  currentRailPos.west().above();
//                    break;
//                }
//            }
//            if (BaseRailBlock.isRail(level.getBlockState(nextPos)))
//            {
//                BaseRailBlock nextBaseRailBlock = (BaseRailBlock) currentRailState.getBlock();
//                RailShape nextRailState = baseRailBlock.getRailDirection(currentRailState, level, currentRailPos, null);
//
//                if(nextRailState == RailShape.NORTH_EAST)
//                {
//                    if(currentRailPos.west().equals(nextPos))
//                    {
//                        minecart.setNegative(!minecart.isNegative());
//                    }
//                }
//                else if(nextRailState == RailShape.SOUTH_WEST)
//                {
//                    if(currentRailPos.east().equals(nextPos))
//                    {
//                        minecart.setNegative(!minecart.isNegative());
//                    }
//                }
//            }
//
//            return nextPos;
//        }
//        return null;
//    }
//
//    public static BlockPos nextPos(boolean negative, BlockPos currentRailPos, BlockState currentRailState, ServerLevel level)
//    {
//        if (currentRailState instanceof IDFCRail)
//        {
//            return ((IDFCRail) currentRailState).nextRailPos(negative, currentRailPos);
//        }
//        else if (BaseRailBlock.isRail(currentRailState))
//        {
//            BaseRailBlock baseRailBlock = (BaseRailBlock) currentRailState.getBlock();
//            RailShape railState = baseRailBlock.getRailDirection(currentRailState, level, currentRailPos, null);
//            BlockPos nextPos = null;
//
//            switch (railState)
//            {
//                case EAST_WEST:
//                {
//                    nextPos = !negative ? currentRailPos.east() : currentRailPos.west();
//                    break;
//                }
//                case NORTH_SOUTH:
//                {
//                    nextPos = !negative ? currentRailPos.north() : currentRailPos.south();
//                    break;
//                }
//                case NORTH_EAST:
//                {
//                    nextPos = !negative ? currentRailPos.north() : currentRailPos.east();
//                    break;
//                }
//                case NORTH_WEST:
//                {
//                    nextPos = !negative ? currentRailPos.north() : currentRailPos.west();
//                    break;
//                }
//                case SOUTH_EAST:
//                {
//                    nextPos = !negative ? currentRailPos.east() : currentRailPos.south();
//                    break;
//                }
//                case SOUTH_WEST:
//                {
//                    nextPos = !negative ? currentRailPos.west() : currentRailPos.south();
//                    break;
//                }
//                case ASCENDING_NORTH:
//                {
//                    nextPos = !negative ? currentRailPos.north().above() : BaseRailBlock.isRail(level.getBlockState(currentRailPos.south().below())) ? currentRailPos.south().below() : currentRailPos.south();
//                    break;
//                }
//                case ASCENDING_EAST:
//                {
//                    nextPos = !negative ? currentRailPos.east().above() : BaseRailBlock.isRail(level.getBlockState(currentRailPos.west().below())) ? currentRailPos.west().below() : currentRailPos.west();
//                    break;
//                }
//                case ASCENDING_SOUTH:
//                {
//                    nextPos = !negative ? BaseRailBlock.isRail(level.getBlockState(currentRailPos.north().below())) ? currentRailPos.north().below() : currentRailPos.north() : currentRailPos.south().above();
//                    break;
//                }
//                case ASCENDING_WEST:
//                {
//                    nextPos = !negative ? BaseRailBlock.isRail(level.getBlockState(currentRailPos.east().below())) ? currentRailPos.east().below() : currentRailPos.east() :  currentRailPos.west().above();
//                    break;
//                }
//            }
//
//            return nextPos;
//        }
//        return null;
//    }
//
//    public static double getLength(BlockState railState, ServerLevel level, BlockPos railPos)
//    {
//        if (railState instanceof IDFCRail)
//        {
//            return ((IDFCRail) railState).getLength();
//        }
//        else if (BaseRailBlock.isRail(railState))
//        {
//            BaseRailBlock baseRailBlock = (BaseRailBlock) railState.getBlock();
//            RailShape railShape = baseRailBlock.getRailDirection(railState, level, railPos, null);
//            BlockPos nextPos = null;
//
//            switch (railShape)
//            {
//                case EAST_WEST, NORTH_SOUTH:
//                {
//                    return 1D;
//                }
//                case NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST:
//                {
//                    return 0.707106D;
//                }
//                case ASCENDING_NORTH, ASCENDING_EAST, ASCENDING_SOUTH, ASCENDING_WEST:
//                {
//                    return 1.41421D;
//                }
//            }
//        }
//        return 0;
//    }
//}
