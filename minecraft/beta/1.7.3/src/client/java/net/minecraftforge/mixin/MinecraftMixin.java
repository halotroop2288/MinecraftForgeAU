/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.fabricmc.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.MinecraftForgeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	/**
	 * @author halotroop2288
	 * @reason implement {@link MinecraftForgeClient#onGameStart()}
	 */
	@Inject(method = "startGame", at = @At(value = "NEW",
			target =  "(Ljava/io/File;Lnet/minecraft/client/Minecraft;)Lnet/minecraft/src/ThreadDownloadResources;"))
	private void forge$startGame_ThreadDownloadResources(CallbackInfo ci) {
		MinecraftForgeClient.onGameStart();
	}
}
