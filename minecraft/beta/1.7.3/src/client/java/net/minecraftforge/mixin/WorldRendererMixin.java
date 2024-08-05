package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
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
