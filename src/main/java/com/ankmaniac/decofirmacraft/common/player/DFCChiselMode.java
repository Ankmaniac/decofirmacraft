package com.ankmaniac.decofirmacraft.common.player;

import com.ankmaniac.decofirmacraft.DecoFirmaCraft;
import com.ankmaniac.decofirmacraft.common.block.ChiseledBlock;
import com.ankmaniac.decofirmacraft.common.block.rock.RockRailBlock;
import com.ankmaniac.decofirmacraft.common.block.state.DFCBlockStateProperties;
import net.dries007.tfc.client.IngameOverlays;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.player.ChiselMode;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

public abstract class DFCChiselMode {
    public static final DeferredRegister<ChiselMode> MODES = DeferredRegister.create(ChiselMode.REGISTRY, DecoFirmaCraft.MOD_ID);

    public static final DeferredHolder<ChiselMode, ChiselMode> CHISEL = register("chisel", new ChiselMode(-100) {
        @Override
        public BlockState modifyStateForPlacement(BlockState state, BlockState chiseled, Player player, BlockHitResult hit)
        {
            return chiseled;
        }


        @Override
        public <T> T createIcon(IconCallback<T> callback)
        {
            return callback.accept(IngameOverlays.TEXTURE, 0, 58, 20, 20);
        }

        @Override
        public void createHotbarIcon(HotbarIconCallback callback)
        {
            callback.accept(IngameOverlays.TEXTURE, 0, 58);
        }
    });

    public static final DeferredHolder<ChiselMode, ChiselMode> RAIL = register("rail", new ChiselMode(300) {
        @Override
        public BlockState modifyStateForPlacement(BlockState state, BlockState chiseled, Player player, BlockHitResult hit)
        {
            if (chiseled.getBlock() instanceof RockRailBlock stair)
            {
                // Use the stair placement state, but fill with fluid after the fact
                chiseled = stair.getStateForPlacement(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(stair), hit));
                if (chiseled != null)
                {
                    chiseled = FluidHelpers.fillWithFluid(chiseled, state.getFluidState().getType());
                }
            }
            return chiseled;
        }


        @Override
        public <T> T createIcon(IconCallback<T> callback)
        {
            return callback.accept(IngameOverlays.TEXTURE, 0, 58, 20, 20);
        }

        @Override
        public void createHotbarIcon(HotbarIconCallback callback)
        {
            callback.accept(IngameOverlays.TEXTURE, 0, 58);
        }
    });

    public static BooleanProperty removedChunk(BlockHitResult hit)
    {
        final Direction hitFace = hit.getDirection();
        final Direction.Axis hitAxis = hitFace.getAxis();
        boolean up = false;
        boolean east = false;
        boolean north = false;

        switch (hitAxis)
        {
            case Direction.Axis.Y ->
            {
                final double hitPos = Math.abs(hit.getLocation().y - hit.getBlockPos().getY());
                if (hitPos != 0D && hitPos != 1D)
                {
                    if (hitFace.equals(Direction.DOWN))
                    {
                        up = true;
                    }
                }
                else
                {
                    up = calculateHalf(hit, hitAxis);
                }
                east = calculateHalf(hit, Direction.Axis.X);
                north = !calculateHalf(hit, Direction.Axis.Z);
            }
            case Direction.Axis.X ->
            {
                final double hitPos = Math.abs(hit.getLocation().x - hit.getBlockPos().getX());
                if (hitPos != 0D && hitPos != 1D)
                {
                    if (hitFace.equals(Direction.WEST))
                    {
                        east = true;
                    }
                }
                else
                {
                    east = calculateHalf(hit, hitAxis);
                }
                up = calculateHalf(hit, Direction.Axis.Y);
                north = !calculateHalf(hit, Direction.Axis.Z);
            }
            case Direction.Axis.Z ->
            {
                final double hitPos = Math.abs(hit.getLocation().z - hit.getBlockPos().getZ());
                if (hitPos != 0D && hitPos != 1D)
                {
                    if (hitFace.equals(Direction.SOUTH))
                    {
                        north = true;
                    }
                }
                else
                {
                    north = !calculateHalf(hit, hitAxis);
                }
                up = calculateHalf(hit, Direction.Axis.Y);
                east = calculateHalf(hit, Direction.Axis.X);
            }
        }
        return ChiseledBlock.properties.get((up ? 0 : 4) + (north ? 0 : 2) + (east ? 0 : 1));
    }

    private static boolean calculateHalf(BlockHitResult hit, Direction.Axis axis)
    {
        switch (axis)
        {
            case Direction.Axis.Y ->
            {
                return hit.getLocation().y - hit.getBlockPos().getY() > 0.5D;
            }
            case Direction.Axis.X ->
            {
                return hit.getLocation().x - hit.getBlockPos().getX() > 0.5D;
            }
            case Direction.Axis.Z ->
            {
                return hit.getLocation().z - hit.getBlockPos().getZ() > 0.5D;
            }
        }
        return false;
    }

    private static <T extends ChiselMode> DeferredHolder<ChiselMode, T> register(String name, T mode)
    {
        return MODES.register(name, () -> mode);
    }
}
