/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package forge;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Event handler for populating biomes
 *
 * @author Space Toad
 * @since 1.0.0
 */
@FunctionalInterface
public interface IBiomePopulator {
	/**
	 * <p>Implements custom generation for the given chunk.</p>
	 * <p>
	 * This is called for each chunk, after the rest of the generation,
	 * and allows contributors to add additional blocks in the world.
	 * </p>
	 *
	 * @param world  the world to act on
	 * @param biome  the biome at the given coordinates
	 * @param chunkX the x position of the chunk
	 * @param chunkZ the z position of the chunk
	 * @author Space Toad
	 * @see MinecraftForge#registerBiomePopulate(IBiomePopulator)
	 * @since 1.0.0
	 */
	void populate(World world, Biome biome, int chunkX, int chunkZ);
}
