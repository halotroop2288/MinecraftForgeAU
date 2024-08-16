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
	 * <p>
	 * This is called before Minecraft tries to fill a bucket with water or lava.
	 * If it returns a non-null result, then the filling process will be stopped
	 * and the empty bucket will be changed to the result of this function.
	 * </p>
	 *
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
