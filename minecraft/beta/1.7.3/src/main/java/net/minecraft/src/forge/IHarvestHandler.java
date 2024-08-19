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
	 * <p>
	 * This is typically used to add (for example) blocks only extracted by a diamond pickaxe.
	 * Other rules may activate the harvesting outside of this handler.
	 * </p>
	 *
	 * @param tool  the tool used to harvest the block
	 * @param block the block to be harvested
	 * @return {@code true} if the tool in parameter is known to be able to harvest the given block.
	 * @author Space Toad
	 * @see MinecraftForge#registerHarvestHandler(IHarvestHandler)
	 * @since 1.0.0
	 */
	boolean canHarvestBlock(ItemTool tool, Block block);
}
