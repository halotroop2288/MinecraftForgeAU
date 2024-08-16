/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.*;
import org.jetbrains.annotations.*;

/**
 * This interface is to be implemented by a {@link Block}
 * to give it a custom resistance to explosions.
 *
 * @author Eloraam
 */
public interface ISpecialResistance {
	/**
	 * @param world the world to act on
	 * @param blockX the x position of this block
	 * @param blockY the y position of this block
	 * @param blockZ the z position of this block
	 * @param sourceX the x position of the explosion source
	 * @param sourceY the y position of the explosion source
	 * @param sourceZ the z position of the explosion source
	 * @param exploder the cause of the explosion (Example: Creeper, {@code null} if bed, fireball, or TNT)
	 * @return the resistance value to the explosion
	 * @author Eloraam
	 * @since 1.0.0
	 */
	float getSpecialExplosionResistance(@NotNull World world,
										int blockX, int blockY, int blockZ,
										double sourceX, double sourceY, double sourceZ,
										@Nullable Entity exploder);
}
