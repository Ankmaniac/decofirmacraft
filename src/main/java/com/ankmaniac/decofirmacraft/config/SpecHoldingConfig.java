package com.ankmaniac.decofirmacraft.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public sealed class SpecHoldingConfig permits ClientConfig, CommonConfig, ServerConfig, StartupConfig {

	private @Nullable ModConfigSpec spec;

	final void updateSpec(final ModConfigSpec spec) {
		this.spec = spec;
	}

	public final ModConfigSpec spec() {
		return Objects.requireNonNull(this.spec);
	}
}