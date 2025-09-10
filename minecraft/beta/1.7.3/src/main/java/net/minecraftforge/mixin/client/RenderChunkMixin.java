/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.*;
import net.fabricmc.api.*;
import forge.*;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.vertex.Tesselator;
import net.minecraft.client.render.world.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RenderChunk.class)
public abstract class RenderChunkMixin {
	/**
	 * @return the current instance of the tessellator
	 * @author halotroop2288
	 * @reason force an updated reference of the tessellator instance every single time it's used.
	 */
	@Redirect(
		method = "compile",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/render/world/RenderChunk;TESSELATOR:Lnet/minecraft/client/render/vertex/Tesselator;"
		)
	)
	private Tesselator redirectTessellatorField() {
		return Tesselator.INSTANCE;
	}

	@Inject(method = "compile", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
		target = "Lnet/minecraft/block/Block;getRenderLayer()I")
	)
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
	 * Calls {@link MinecraftForgeClient#beforeBlockRender(Block, BlockRenderer)}
	 * before calling {@link BlockRenderer#tesselateInWorld(Block, int, int, int)},
	 * and calls {@link MinecraftForgeClient#afterBlockRender(Block, BlockRenderer)} after.
	 *
	 * @author halotroop2288
	 * @reason prevent bugs caused by implementing {@link ITextureProvider}
	 */
	@WrapOperation(method = "compile", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/render/block/BlockRenderer;tesselateInWorld(Lnet/minecraft/block/Block;III)Z"))
	private boolean forge$updateRenderer_renderBlockByRenderType_wrap(BlockRenderer instance,
																	  Block block, int x, int y, int z,
																	  Operation<Boolean> original) {
		MinecraftForgeClient.beforeBlockRender(block, instance);
		boolean ret = original.call(instance, block, x, y, z);
		MinecraftForgeClient.afterBlockRender(block, instance);
		return ret;
	}
}
