/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.injection;

import net.minecraft.src.*;
import org.jetbrains.annotations.ApiStatus;

/**
 * Adds public methods to {@link EffectRenderer} via Fabric Interface Injectors.
 *
 * @author halotroop2288
 */
@ApiStatus.Internal
public interface ForgeEffectRenderer {
	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 */
	default void addDigParticleEffect(EntityDiggingFX digEffect, Block block) {
		throw new RuntimeException("Not implemented.");
	}
}
