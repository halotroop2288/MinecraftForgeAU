/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is to be implemented by {@link Block} classes,
 * to provide access to a number of secondary block properties
 * by providing overrides to methods within {@link World}.
 *
 * @author Eloraam
 * @since 1.0.2
 */
public interface IBlockSecondaryProperties {
	/**
	 * This allows for blocks like torches, rails, redstone dust, etc. being attached
	 * to the surface of the block regardless of whether it is opaque,
	 * or possesses other rendering properties that would not normally make this possible.
	 *
	 * @param world the world to act on
	 * @param x     the x position of the block
	 * @param y     the y position of the block
	 * @param z     the z position of the block
	 * @return whether this block should be treated as a normal cube by game logic
	 * (this is independent of rendering).
	 * @author Eloraam
	 * @since 1.0.2
	 */
	default boolean isBlockNormalCube(@NotNull World world, int x, int y, int z) {
		if (this instanceof Block) {
			Block block = ((Block) this);
			return block.blockMaterial.getIsOpaque() && block.isACube();
		}

		return false;
	}

	/**
	 * This provides a means of allowing blocks to not be considered when the player attempts to place another within
	 * their bounds, in the same way that water, lava, fire, and some other blocks behave within the game.<br>
	 * Provides an override to the corresponding section in {@link World#canBlockBePlacedAt(int, int, int, int, boolean, int)}.
	 *
	 * @param world the world to act on
	 * @param x     the x position of the block
	 * @param y     the y position of the block
	 * @param z     the z position of the block
	 * @return whether this block can be replaced by another by the player
	 * @author Eloraam
	 * @since 1.0.2
	 */
	default boolean isBlockReplaceable(@NotNull World world, int x, int y, int z) {
		if (this instanceof Block) {
			Block block = ((Block) this);
			return block == Block.waterMoving || block == Block.waterStill
				|| block == Block.lavaMoving || block == Block.lavaStill
				|| block == Block.fire || block == Block.snow;
		}

		return false;
	}

	/**
	 * Provides an override to the related test within {@link World#isBoundingBoxBurning(AxisAlignedBB)}.
	 *
	 * @param world the world to act on
	 * @param x     the x position of the block
	 * @param y     the y position of the block
	 * @param z     the z position of the block
	 * @return whether this block should set fire and deal fire damage to entities coming into contact with it
	 * @author Eloraam
	 * @since 1.0.2
	 */
	default boolean isBlockBurning(@NotNull World world, int x, int y, int z) {
		if (this instanceof Block) {
			Block block = ((Block) this);
			return block == Block.fire || block == Block.lavaMoving || block == Block.lavaStill;
		}

		return false;
	}

	/**
	 * This method is primarily useful for creating pure logic-blocks that will be invisible
	 * to the player and otherwise interact as air would.
	 * Provides an override to {@link World#isAirBlock(int, int, int)}
	 *
	 * @param world the world to act on
	 * @param x     the x position of the block
	 * @param y     the y position of the block
	 * @param z     the z position of the block
	 * @return whether this block should be treated as an air block by the rest of the code
	 * @author Eloraam
	 * @since 1.0.2
	 */
	default boolean isAirBlock(@NotNull World world, int x, int y, int z) {
		return this instanceof Block && ((Block) this).blockID == 0;
	}
}
