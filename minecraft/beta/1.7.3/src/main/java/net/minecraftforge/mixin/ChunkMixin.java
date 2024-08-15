/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.minecraft.src.*;
import net.minecraft.src.forge.IOverrideReplace;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(Chunk.class)
public abstract class ChunkMixin {
	@Shadow public World worldObj;

	/**
	 * @param x  the x position to modify
	 * @param y  the y position to modify
	 * @param z  the z position to modify
	 * @param id the block id to set
	 * @author halotroop2288
	 * @reason implement {@link IOverrideReplace}
	 */
	@Inject(method = "setBlockID", cancellable = true,
		at = @At(value = "FIELD", target = "Lnet/minecraft/src/Chunk;blocks:[B", ordinal = 1))
	private void forge$setBlockID_blocks(int x, int y, int z, int id, CallbackInfoReturnable<Boolean> cir) {
		Block block = Block.blocksList[id];
		if (block instanceof IOverrideReplace) {
			IOverrideReplace or = (IOverrideReplace) block;
			if (or.canReplaceBlock(this.worldObj, x, y, z, id)) {
				cir.setReturnValue(or.getReplacedSuccess());
			}
		}
	}

	/**
	 * @param x  the x position to modify
	 * @param y  the y position to modify
	 * @param z  the z position to modify
	 * @param id the block id to set
	 * @param meta the metadata value to set
	 * @author halotroop2288
	 * @reason implement {@link IOverrideReplace}
	 */
	@Inject(method = "setBlockIDWithMetadata", cancellable = true,
		at = @At(value = "FIELD", target = "Lnet/minecraft/src/Chunk;blocks:[B", ordinal = 1))
	private void forge$setBlockIDWithMeta_blocks(int x, int y, int z, int id, int meta, CallbackInfoReturnable<Boolean> cir) {
		Block block = Block.blocksList[id];
		if (block instanceof IOverrideReplace) {
			IOverrideReplace or = (IOverrideReplace) block;
			if (or.canReplaceBlock(this.worldObj, x, y, z, id)) {
				cir.setReturnValue(or.getReplacedSuccess());
			}
		}
	}
}
