package com.ankmaniac.decofirmacraft.common.blocks.metal;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;

public class GateBlock extends DoorBlock {

    public GateBlock(BlockBehaviour.Properties properties, BlockSetType type) {
        super(properties, type);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean open = state.getValue(OPEN);
        if (open) {
            switch (direction) {
                case EAST:
                default:
                    return Shapes.or(
                            Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 2.0D),
                            Block.box(0.0D, 0.0D, 14.0D, 8.0D, 16.0D, 16.0D)
                    );
                case SOUTH:
                    return Shapes.or(
                            Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 8.0D),
                            Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D)
                    );
                case WEST:
                    return Shapes.or(
                            Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
                            Block.box(8.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D)
                    );
                case NORTH:
                    return Shapes.or(
                            Block.box(0.0D, 0.0D, 8.0D, 2.0D, 16.0D, 16.0D),
                            Block.box(14.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D)
                    );
            }
        } else {
            switch (direction) {
                case EAST:
                default:
                    return Shapes.or(Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
                case SOUTH:
                    return Shapes.or(Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D));
                case WEST:
                    return Shapes.or(Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
                case NORTH:
                    return Shapes.or(Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D));
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        state = state.cycle(OPEN);
        level.setBlockAndUpdate(pos, state);

        SoundEvent sound = state.getValue(OPEN) ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE;
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.CONSUME;
    }
}