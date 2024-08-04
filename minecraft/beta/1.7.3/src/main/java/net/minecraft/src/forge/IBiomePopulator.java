package net.minecraft.src.forge;

import net.minecraft.src.*;

/**
 * Event handler for populating biomes
 *
 * @author Space Toad
 * @since 1.0.0
 */
@FunctionalInterface
public interface IBiomePopulator {
	/**
	 * Implements custom generation for the given chunk.
	 *
	 * @param world  the world to act on
	 * @param biome  the biome at the given coordinates
	 * @param chunkX the x position of the chunk
	 * @param chunkZ the z position of the chunk
	 * @since 1.0.0
	 */
	void populate(World world, BiomeGenBase biome, int chunkX, int chunkZ);
}
