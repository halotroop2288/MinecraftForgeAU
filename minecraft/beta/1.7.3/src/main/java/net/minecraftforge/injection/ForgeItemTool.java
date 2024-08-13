/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.injection;

import net.minecraft.src.Block;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ForgeItemTool {
	/**
	 * Makes this tool effective against the given block.
	 *
	 * @param block the block to be made harvestable by this tool
	 * @author Space Toad
	 * @author halotroop2288
	 */
	default void addBlockEffectiveAgainst(Block block) {
		throw new RuntimeException("Not implemented.");
	}
}
