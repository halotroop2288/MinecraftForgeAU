/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;

/**
 * Event handler for harvesting blocks
 *
 * @author Space Toad
 * @since 1.0.0
 */
@FunctionalInterface
public interface IHarvestHandler {
	/**
	 * @param tool  the tool used to harvest the block
	 * @param block the block to be harvested
	 * @author Space Toad
	 * @since 1.0.0
	 */
	boolean canHarvestBlock(ItemTool tool, Block block);
}
