/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import net.minecraftforge.injection.ForgeItemTool;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * Common hooks for Minecraft mod development.
 *
 * @author Space Toad
 * @since 1.0.0
 */
public class MinecraftForge {
	private static final LinkedList<IBucketHandler> bucketHandlers = new LinkedList<>();
	private static final LinkedList<IBiomePopulator> biomePopulators = new LinkedList<>();
	private static final LinkedList<IHarvestHandler> harvestHandlers = new LinkedList<>();

	private MinecraftForge() {
	}

	/**
	 * This is not supposed to be called outside of Minecraft internals.
	 *
	 * @param world the world to act on
	 * @param x     the x position of the block the bucket user is aiming at
	 * @param y     the y position of the block the bucket user is aiming at
	 * @param z     the z position of the block the bucket user is aiming at
	 * @return the stack to replace the bucket
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@ApiStatus.Internal
	public static ItemStack fillCustomBucket(World world, int x, int y, int z) {
		return bucketHandlers.stream().map(handler -> handler.fillCustomBucket(world, x, y, z))
			.filter(Objects::nonNull).findFirst().orElse(null);
	}

	/**
	 * This is not supposed to be called outside of Minecraft internals.
	 *
	 * @param world  the world to act on
	 * @param biome  the biome at the given chunk coordinates
	 * @param chunkX the x position of the chunk being generated
	 * @param chunkZ the z position of the chunk being generated
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@ApiStatus.Internal
	public static void populateBiome(World world, BiomeGenBase biome, int chunkX, int chunkZ) {
		biomePopulators.forEach(populator -> populator.populate(world, biome, chunkX, chunkZ));
	}

	/**
	 * This is not supposed to be called outside of Minecraft internals.
	 *
	 * @param tool  the tool used to harvest the block
	 * @param block the block to be harvested
	 * @return true if the block can be harvested in the given context
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@ApiStatus.Internal
	public static boolean canHarvestBlock(ItemTool tool, Block block) {
		return harvestHandlers.stream().anyMatch(handler -> handler.canHarvestBlock(tool, block));
	}

	/**
	 * Makes all vanilla pickaxes effective against the given block.
	 *
	 * @param block the block that should be harvestable by pickaxes.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void addPickaxeBlockEffectiveAgainst(Block block) {
		((ForgeItemTool) Item.pickaxeWood).addBlockEffectiveAgainst(block);
		((ForgeItemTool) Item.pickaxeStone).addBlockEffectiveAgainst(block);
		((ForgeItemTool) Item.pickaxeSteel).addBlockEffectiveAgainst(block);
		((ForgeItemTool) Item.pickaxeGold).addBlockEffectiveAgainst(block);
		((ForgeItemTool) Item.pickaxeDiamond).addBlockEffectiveAgainst(block);
	}

	/**
	 * Registers a new custom bucket handler.
	 *
	 * @param handler the handler to register
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void registerCustomBucketHandler(IBucketHandler handler) {
		bucketHandlers.add(handler);
	}

	/**
	 * Registers a new biome contributor.
	 *
	 * @param populator the populator to register
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void registerBiomePopulate(IBiomePopulator populator) {
		biomePopulators.add(populator);
	}

	/**
	 * Registers a new harvest handler.
	 *
	 * @param handler the handler to register
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void registerHarvestHandler(IHarvestHandler handler) {
		harvestHandlers.add(handler);
	}
}
