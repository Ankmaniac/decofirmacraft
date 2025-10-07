package com.ankmaniac.decofirmacraft.common.block.metal;

import net.dries007.tfc.common.blocks.IWeatheringBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringRotatedPillarBlock extends RotatedPillarBlock implements IWeatheringBlock {
    private final Age age;
    private final float weatheringResistance;

    public WeatheringRotatedPillarBlock(Properties properties, Age age, float weatheringResistance)
    {
        super(age == Age.OXIDIZED ? properties : properties.randomTicks());
        this.age = age;
        this.weatheringResistance = weatheringResistance;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        onRandomTick(state, level, pos, random);
    }

    @Override
    public float weatheringResistance()
    {
        return weatheringResistance;
    }

    @Override
    public Age getAge()
    {
        return age;
    }
}
