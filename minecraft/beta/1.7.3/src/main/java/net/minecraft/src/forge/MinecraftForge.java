/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;

import java.util.*;

/**
 * @author Space Toad
 * @since 1.0.0
 */
public class MinecraftForge {
	private static final LinkedList<IBucketHandler> bucketHandlers = new LinkedList<>();
	private static final LinkedList<IBiomePopulator> biomePopulators = new LinkedList<>();

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static ItemStack fillCustomBucket(World world, int x, int y, int z) {
		return bucketHandlers.stream().map(handler -> handler.fillCustomBucket(world, x, y, z))
				.filter(Objects::nonNull).findFirst().orElse(null);
	}

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void populateBiome(World world, BiomeGenBase biome, int chunkX, int chunkZ) {
		biomePopulators.forEach(populator -> populator.populate(world, biome, chunkX, chunkZ));
	}

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void registerCustomBucketHandler(IBucketHandler bucketHandler) {
		bucketHandlers.add(bucketHandler);
	}

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void registerBiomePopulate(IBiomePopulator biomePopulator) {
		biomePopulators.add(biomePopulator);
	}
}
