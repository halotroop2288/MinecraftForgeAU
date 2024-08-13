/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import net.minecraftforge.injection.ForgeItemTool;

import java.util.*;

/**
 * @author Space Toad
 * @since 1.0.0
 */
public class MinecraftForge {
	private static final LinkedList<IBucketHandler> bucketHandlers = new LinkedList<>();
	private static final LinkedList<IBiomePopulator> biomePopulators = new LinkedList<>();
	private static final LinkedList<IHarvestHandler> harvestHandlers = new LinkedList<>();

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
	 * @param tool  the tool used to harvest the block
	 * @param block the block to be harvested
	 * @author Space Toad
	 * @since 1.0.0
	 */
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

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static void registerHarvestHandler(IHarvestHandler handler) {
		harvestHandlers.add(handler);
	}
}
