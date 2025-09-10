/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.client;

import net.minecraft.client.entity.particle.Particle;

import java.util.*;

/**
 * Holds block particle information for {@link ParticleManager}.
 *
 * @author FlowerChild
 * @since 1.0.0
 */
public class ParticleManager$BlockTextureParticles {
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
	public List<Particle> effects = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public ParticleManager$BlockTextureParticles() {
	}
}
