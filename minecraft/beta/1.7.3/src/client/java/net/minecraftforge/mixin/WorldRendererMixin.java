/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	/**
	 * @return the current instance of the tessellator
	 * @author halotroop2288
	 * @reason force an updated reference of the tessellator instance every single time it's used.
	 */
	@Redirect(
		method = "updateRenderer",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/src/WorldRenderer;tessellator:Lnet/minecraft/src/Tessellator;"
		)
	)
	private Tessellator redirectTessellatorField() {
		return Tessellator.instance;
	}

	@Inject(method = "updateRenderer", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
		target = "Lnet/minecraft/src/Block;getRenderBlockPass()I"))
	private void forge$updateRenderer_getRenderBlockPass_after(CallbackInfo ci,
															   @Local(ordinal = 0) LocalBooleanRef flag1,
															   @Local(ordinal = 6) LocalIntRef renderBlockPass,
															   @Local Block block,
															   @Local(ordinal = 14) int pass) {
		if (block instanceof IMultipassRender) {
			if (renderBlockPass.get() != pass) flag1.set(true);
			IMultipassRender mpr = (IMultipassRender) block;
			if (mpr.canRenderInPass(pass)) renderBlockPass.set(pass);
		}
	}

	/**
	 * Calls {@link MinecraftForgeClient#beforeBlockRender(Block, RenderBlocks)}
	 * before calling {@link RenderBlocks#renderBlockByRenderType(Block, int, int, int)}.
	 * and calls {@link MinecraftForgeClient#afterBlockRender(Block, RenderBlocks)} after.
	 *
	 * @author halotroop2288
	 * @reason prevent bugs caused by implementing {@link ITextureProvider}
	 */
	@WrapOperation(method = "updateRenderer", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/src/Block;III)Z"))
	private boolean forge$updateRenderer_renderBlockByRenderType_wrap(RenderBlocks instance,
																	  Block block, int x, int y, int z,
																	  Operation<Boolean> original) {
		MinecraftForgeClient.beforeBlockRender(block, instance);
		boolean ret = original.call(instance, block, x, y, z);
		MinecraftForgeClient.afterBlockRender(block, instance);
		return ret;
	}
}
