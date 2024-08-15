/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.*;
import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Inject(method = "updateRenderer", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lnet/minecraft/src/Block;getRenderBlockPass()I"))
	private void forge$updateRenderer_getRenderBlockPass_after(CallbackInfo ci,
															   @Local(ordinal = 0) LocalBooleanRef flag,
															   @Local(ordinal = 6) LocalIntRef j3,
															   @Local Block block,
															   @Local(ordinal = 14) int pass) {
		if (block instanceof IMultipassRender) {
			if (j3.get() != pass) flag.set(true);
			IMultipassRender mpr = (IMultipassRender) block;
			if (mpr.canRenderInPass(pass)) j3.set(pass);
		}
	}

	/**
	 * Calls {@link MinecraftForgeClient#beforeBlockRender(Block, RenderBlocks)}
	 * before calling {@link RenderBlocks#renderBlockByRenderType(Block, int, int, int)}.
	 *
	 * @author halotroop2288
	 * @reason implement {@link ITextureProvider}
	 */
	@Inject(method = "updateRenderer", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/src/Block;III)Z"))
	private void forge$updateRenderer_renderBlockByRenderType(CallbackInfo ci,
															  @Local Block block,
															  @Local RenderBlocks renderer) {
		MinecraftForgeClient.beforeBlockRender(block, renderer);
	}

	/**
	 * Calls {@link MinecraftForgeClient#afterBlockRender(Block, RenderBlocks)}
	 * after calling {@link RenderBlocks#renderBlockByRenderType(Block, int, int, int)}.
	 *
	 * @author halotroop2288
	 * @reason prevent bugs caused by implementing {@link ITextureProvider}
	 */
	@Inject(method = "updateRenderer", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/src/Block;III)Z"))
	private void forge$updateRenderer_renderBlockByRenderType_after(CallbackInfo ci,
																	@Local Block block,
																	@Local RenderBlocks renderer) {
		MinecraftForgeClient.afterBlockRender(block, renderer);
	}
}
