/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import org.jetbrains.annotations.*;

/**
 * Event handler for filling buckets
 *
 * @author Space Toad
 * @since 1.0.0
 */
@FunctionalInterface
public interface IBucketHandler {
	/**
	 * @param world the world to act on
	 * @param x     the x position of the fluid
	 * @param y     the y position of the fluid
	 * @param z     the z position of the fluid
	 * @return the new stack of the bucket containing the fluid
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@Nullable ItemStack fillCustomBucket(@NotNull World world, int x, int y, int z);
}
