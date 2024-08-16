/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;

/**
 * This interface is to be implemented by an {@link Item},
 * to be invoked at the beginning of {@link Item#onItemUse(ItemStack, EntityPlayer, World, int, int, int, int)}.
 *
 * @author Eloraam
 * @since 1.0.0
 */
public interface IUseItemFirst {
	/**
	 * This is called when the item is used, before the block is activated.<br>
	 * When this returns {@code true}, {@link Item#onItemUse(ItemStack, EntityPlayer, World, int, int, int, int)}
	 * will immediately return true as well.
	 *
	 * @param stack  the stack containing this item
	 * @param player the player using the item
	 * @param world  the world being acted on
	 * @param x      the x position of the block the player is aiming at
	 * @param y      the y position of the block the player is aiming at
	 * @param z      the z position of the block the player is aiming at
	 * @param side   the side of the block the player is aiming at
	 * @return whether the item has been used
	 * @author Eloraam
	 * @since 1.0.0
	 */
	boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side);
}
