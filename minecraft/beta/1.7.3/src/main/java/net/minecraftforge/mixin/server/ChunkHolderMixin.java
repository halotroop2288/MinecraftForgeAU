/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.fabricmc.api.*;
import net.minecraft.server.ChunkMap;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

/**
 * @author halotroop2288
 */
@Environment(EnvType.SERVER)
@Mixin(ChunkMap.ChunkHolder.class)
public abstract class ChunkHolderMixin {
	@Shadow private short[] dirtyBlocks;

	/**
	 * @param instance  the vanilla value
	 * @param loopIndex the index in the for loop
	 * @return the correct block ID
	 * @author Eloraam
	 * @author halotroop2288
	 * @reason bug fix
	 */
	@Redirect(method = "sendChanges", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, ordinal = 5,
		target = "Lnet/minecraft/server/ChunkMap$ChunkHolder;blocksChanged:I"
	))
	private int forge$onUpdate_numBlocksToUpdate5(ChunkMap.ChunkHolder instance, @Local(ordinal = 0) int loopIndex) {
		return dirtyBlocks[loopIndex];
	}

	/**
	 * @param instance  the vanilla value
	 * @param loopIndex the index in the for loop
	 * @return the correct block ID
	 * @author Eloraam
	 * @author halotroop2288
	 * @reason bug fix
	 */
	@Redirect(method = "sendChanges", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, ordinal = 6,
		target = "Lnet/minecraft/server/ChunkMap$ChunkHolder;blocksChanged:I"
	))
	private int forge$onUpdate_numBlocksToUpdate6(ChunkMap.ChunkHolder instance, @Local(ordinal = 0) int loopIndex) {
		return dirtyBlocks[loopIndex];
	}

	/**
	 * @param instance  the vanilla value
	 * @param loopIndex the index in the for loop
	 * @return the correct block ID
	 * @author Eloraam
	 * @author halotroop2288
	 * @reason bug fix
	 */
	@Redirect(method = "sendChanges", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, ordinal = 7,
		target = "Lnet/minecraft/server/ChunkMap$ChunkHolder;blocksChanged:I"
	))
	private int forge$onUpdate_numBlocksToUpdate7(ChunkMap.ChunkHolder instance, @Local(ordinal = 0) LocalIntRef loopIndex) {
		return dirtyBlocks[loopIndex.get()];
	}
}
