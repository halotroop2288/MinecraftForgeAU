/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.minecraft.block.Block;
import forge.IOverrideReplace;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
	@Shadow public World world;

	/**
	 * @author halotroop2288
	 * @reason implement {@link IOverrideReplace}
	 */
	@Inject(method = "setBlockAt", cancellable = true,
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/chunk/WorldChunk;blocks:[B", ordinal = 1))
	private void forge$setBlockID_blocks(int x, int y, int z, int id, CallbackInfoReturnable<Boolean> cir) {
		Block block = Block.BY_ID[id];
		if (block instanceof IOverrideReplace) {
			IOverrideReplace or = (IOverrideReplace) block;
			if (or.canReplaceBlock(this.world, x, y, z, id)) {
				cir.setReturnValue(or.getReplacedSuccess());
			}
		}
	}

	/**
	 * @author halotroop2288
	 * @reason implement {@link IOverrideReplace}
	 */
	@Inject(method = "setBlockWithMetadataAt", cancellable = true,
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/chunk/WorldChunk;blocks:[B", ordinal = 1))
	private void forge$setBlockIDWithMeta_blocks(int x, int y, int z, int id, int meta, CallbackInfoReturnable<Boolean> cir) {
		Block block = Block.BY_ID[id];
		if (block instanceof IOverrideReplace) {
			IOverrideReplace or = (IOverrideReplace) block;
			if (or.canReplaceBlock(this.world, x, y, z, id)) {
				cir.setReturnValue(or.getReplacedSuccess());
			}
		}
	}
}
