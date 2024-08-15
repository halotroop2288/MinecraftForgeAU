/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import org.jetbrains.annotations.NotNull;

/**
 * This interface has to be implemented by a {@link Block},
 * for deciding whether it can replace specific blocks when placed.
 *
 * @author Eloraam
 * @since 1.0.0
 */
public interface IOverrideReplace {
	/**
	 * @param world   the world to act on
	 * @param x       the x position of the block to replace
	 * @param y       the y position of the block to replace
	 * @param z       the z position of the block to replace
	 * @param blockID the ID of the block to replace
	 * @return whether this block is able to replace the block represented by the given ID
	 * @author Eloraam
	 * @since 1.0.0
	 */
	boolean canReplaceBlock(@NotNull World world, int x, int y, int z, int blockID);

	/**
	 * @return whether the block was successfully replaced
	 * @author Eloraam
	 * @since 1.0.0
	 */
	boolean getReplacedSuccess();
}
