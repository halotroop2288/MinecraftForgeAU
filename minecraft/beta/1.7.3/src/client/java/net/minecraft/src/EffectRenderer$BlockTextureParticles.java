/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src;

import java.util.*;

/**
 * Holds block particle information for {@link EffectRenderer}.
 *
 * @author FlowerChild
 * @since 1.0.0
 */
public class EffectRenderer$BlockTextureParticles {
	/**
	 * The block texture to use for the particles.
	 *
	 * @since 1.0.0
	 */
	public String texture;
	/**
	 * A list of particles to render for the block.
	 *
	 * @since 1.0.0
	 */
	public List<EntityFX> effects = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public EffectRenderer$BlockTextureParticles() {
	}
}
