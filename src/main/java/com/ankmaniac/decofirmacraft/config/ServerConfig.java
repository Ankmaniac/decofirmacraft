package com.ankmaniac.decofirmacraft.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.ankmaniac.decofirmacraft.DecoFirmaCraft.MOD_ID;

public final class ServerConfig extends SpecHoldingConfig {

	public final Supplier<Integer> gobletCapacity;

	ServerConfig(ModConfigSpec.Builder innerBuilder) {

		Function<String, ModConfigSpec.Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.server." + name);

		innerBuilder.push("general");

		gobletCapacity = builder.apply("gobletCapacity")
				.comment("Amount of mb a goblet can hold. Default is 100.")
				.defineInRange("gobletCapacity",100, 1, Integer.MAX_VALUE);
		innerBuilder.pop();
	}
}