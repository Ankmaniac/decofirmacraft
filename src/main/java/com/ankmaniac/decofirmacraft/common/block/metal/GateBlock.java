package com.ankmaniac.decofirmacraft.common.block.metal;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GateBlock extends DoorBlock {

    public GateBlock(BlockSetType type, BlockBehaviour.Properties properties) {
        super(type, properties);
        this.type = type;
    }

    private final BlockSetType type;

    public BlockSetType type() {
        return this.type;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean open = state.getValue(OPEN);
        if (open) {
            switch (direction) {
                case NORTH:
                default:
                    return Shapes.or(
                            Block.box(0.0D, 0.0D, 8.0D, 2.0D, 16.0D, 16.0D),
                            Block.box(14.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D)
                    );
                case EAST:
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
            }
        } else {
            switch (direction) {
                case NORTH, SOUTH:
                default:
                    return Shapes.or(Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D));
                case EAST, WEST:
                    return Shapes.or(Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!this.type.canOpenByHand()) {
            state = (BlockState)state.cycle(OPEN);
            level.setBlock(pos, state, 10);
            this.playSound(player, level, pos, (Boolean)state.getValue(OPEN));
            level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    private void playSound(@Nullable Entity source, Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, isOpening ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }
}
