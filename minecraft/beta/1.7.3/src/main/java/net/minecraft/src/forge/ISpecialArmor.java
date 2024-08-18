/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.ItemArmor;
import org.jetbrains.annotations.ApiStatus;

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
	 * Called before vanilla damage computation.<br>
	 * Adjusts the amount of damage received by the wearer.
	 *
	 * @param damage the damage value before the armor is applied
	 * @return the new damage value after this armor is applied
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@ApiStatus.OverrideOnly
	int adjustArmorDamage(int damage);

	/**
	 * Allows you to cancel the vanilla armor calculation.
	 *
	 * @return false if the vanilla armor calculation should be cancelled.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@ApiStatus.OverrideOnly
	boolean allowRegularComputation();
}
