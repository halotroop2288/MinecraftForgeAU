/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.ItemArmor;

/**
 * This interface is to be implemented by {@link ItemArmor} classes.
 * It will allow to modify computation of damage and health loss.
 * Computation will be called before the actual armor computation,
 * which can then be cancelled.
 *
 * @author Space Toad
 * @since 1.0.0
 */
public interface ISpecialArmor {
	/**
	 * Adjusts the amount of damage received by the entity.
	 *
	 * @author Space Toad
	 * @since 1.0.0
	 */
	int adjustArmorDamage(int damage);

	/**
	 * @return true if the vanilla armor computation should be cancelled.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	boolean allowRegularComputation();
}
