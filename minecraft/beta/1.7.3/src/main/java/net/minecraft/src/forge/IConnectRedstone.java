/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is to be implemented by {@link Block} classes.
 * It will override standard algorithms controlling connection between two blocks by redstone.
 *
 * @author Eloraam
 * @since 1.0.0
 */
@FunctionalInterface
public interface IConnectRedstone {
	/**
	 * Checks if a redstone connection can be made in the given direction from this block.
	 *
	 * @param world     the world to act on
	 * @param x         the x position of this block
	 * @param y         the y position of this block
	 * @param z         the z position of this block
	 * @param direction the direction from which the redstone wire is connecting
	 * @return whether this block can provide redstone power to the given direction
	 * @author Eloraam
	 * @since 1.0.0
	 */
	boolean canConnectRedstone(@NotNull IBlockAccess world, int x, int y, int z, int direction);
}
