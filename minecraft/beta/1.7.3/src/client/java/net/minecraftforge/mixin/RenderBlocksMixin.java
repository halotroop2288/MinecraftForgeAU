/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Environment(EnvType.CLIENT)
@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
	/**
	 * Calls {@link MinecraftForgeClient#beforeBlockRender(Block, RenderBlocks)}
	 * before running.
	 *
	 * @author halotroop2288
	 * @reason implement {@link ITextureProvider}
	 */
	@Inject(method = "renderBlockOnInventory", at = @At("HEAD"))
	private void forge$beforeBlockRender(Block block, int i, float f, CallbackInfo ci) {
		MinecraftForgeClient.beforeBlockRender(block, ((RenderBlocks) (Object) this));
	}

	/**
	 * Calls {@link MinecraftForgeClient#afterBlockRender(Block, RenderBlocks)}
	 * after running.
	 *
	 * @author halotroop2288
	 * @reason prevent bugs caused by implementing {@link ITextureProvider}
	 */
	@Inject(method = "renderBlockOnInventory", at = @At("TAIL"))
	private void forge$afterBlockRender(Block block, int i, float f, CallbackInfo ci) {
		MinecraftForgeClient.afterBlockRender(block, ((RenderBlocks) (Object) this));
	}
}
