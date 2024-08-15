/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.IBlockAccess;

/**
 * Event handler for connecting redstone wire
 *
 * @author Eloraam
 * @since 1.0.0
 */
@FunctionalInterface
public interface IConnectRedstone {
	/**
	 * @param world     the world to act on
	 * @param x         the x position of this block
	 * @param y         the y position of this block
	 * @param z         the z position of this block
	 * @param direction the direction from which the redstone wire is connecting
	 * @return whether the wire should connect directly to this block from the given direction
	 * @author Eloraam
	 * @since 1.0.0
	 */
	boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int direction);
}
