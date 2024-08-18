/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Mixin(ChunkProviderGenerate.class)
public abstract class ChunkProviderGenerateMixin {
	@Shadow private World worldObj;

	/**
	 * @author halotroop2288
	 * @reason implement {@link IBiomePopulator}
	 */
	@Inject(method = "populate", at = @At(value = "INVOKE", ordinal = 1,
		target = "Lnet/minecraft/src/World;getWorldChunkManager()Lnet/minecraft/src/WorldChunkManager;"))
	private void forge$populateBiome_getWorldChunkManager(IChunkProvider provider, int chunkX, int chunkZ,
														  CallbackInfo ci, @Local(ordinal = 0) BiomeGenBase biome) {
		MinecraftForge.populateBiome(this.worldObj, biome, chunkX, chunkZ);
	}
}
