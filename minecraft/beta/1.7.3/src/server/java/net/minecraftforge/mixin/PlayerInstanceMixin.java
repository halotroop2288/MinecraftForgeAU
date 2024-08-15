/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.*;
import net.minecraft.src.PlayerInstance;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

/**
 * @author halotroop2288
 */
@Environment(EnvType.SERVER)
@Mixin(PlayerInstance.class)
public abstract class PlayerInstanceMixin {
	@Shadow private short[] blocksToUpdate;

	/**
	 * @param instance  the vanilla value
	 * @param loopIndex the index in the for loop
	 * @return the correct block ID
	 * @author Eloraam
	 * @author halotroop2288
	 * @reason bug fix
	 */
	@Redirect(method = "onUpdate", at = @At(value = "FIELD", ordinal = 5,
		target = "Lnet/minecraft/src/PlayerInstance;numBlocksToUpdate:I"))
	private int forge$onUpdate_numBlocksToUpdate5(PlayerInstance instance, @Local(ordinal = 6) int loopIndex) {
		return blocksToUpdate[loopIndex];
	}

	/**
	 * @param instance  the vanilla value
	 * @param loopIndex the index in the for loop
	 * @return the correct block ID
	 * @author Eloraam
	 * @author halotroop2288
	 * @reason bug fix
	 */
	@Redirect(method = "onUpdate", at = @At(value = "FIELD", ordinal = 6,
		target = "Lnet/minecraft/src/PlayerInstance;numBlocksToUpdate:I"))
	private int forge$onUpdate_numBlocksToUpdate6(PlayerInstance instance, @Local(ordinal = 6) int loopIndex) {
		return blocksToUpdate[loopIndex];
	}

	/**
	 * @param instance  the vanilla value
	 * @param loopIndex the index in the for loop
	 * @return the correct block ID
	 * @author Eloraam
	 * @author halotroop2288
	 * @reason bug fix
	 */
	@Redirect(method = "onUpdate", at = @At(value = "FIELD", ordinal = 7,
		target = "Lnet/minecraft/src/PlayerInstance;numBlocksToUpdate:I"))
	private int forge$onUpdate_numBlocksToUpdate7(PlayerInstance instance, @Local(ordinal = 6) int loopIndex) {
		return blocksToUpdate[loopIndex];
	}
}
