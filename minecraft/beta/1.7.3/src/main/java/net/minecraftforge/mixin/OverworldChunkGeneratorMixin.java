/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import forge.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Mixin(OverworldChunkGenerator.class)
public abstract class OverworldChunkGeneratorMixin {
	@Shadow private World world;

	/**
	 * @author halotroop2288
	 * @reason implement {@link IBiomePopulator}
	 */
	@Inject(method = "populateChunk", at = @At(value = "INVOKE", ordinal = 1,
		target = "Lnet/minecraft/world/World;getBiomeSource()Lnet/minecraft/world/biome/source/BiomeSource;")
	)
	private void forge$populateBiome_getWorldChunkManager(ChunkSource provider, int chunkX, int chunkZ,
														  CallbackInfo ci, @Local(ordinal = 0) Biome biome) {
		MinecraftForge.populateBiome(this.world, biome, chunkX, chunkZ);
	}
}
