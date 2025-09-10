/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import forge.*;
import net.fabricmc.api.*;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author halotroop2288
 */
@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {
	/**
	 * Calls {@link MinecraftForgeClient#beforeBlockRender(Block, BlockRenderer)}
	 * before and {@link MinecraftForgeClient#afterBlockRender(Block, BlockRenderer)}
	 * after running.
	 *
	 * @author halotroop2288
	 * @reason implement {@link ITextureProvider}
	 */
	@WrapMethod(method = "renderAsItem")
	private void forge$beforeBlockRender(Block block, int metadata, float brightness, Operation<Void> original) {
		MinecraftForgeClient.beforeBlockRender(block, ((BlockRenderer) (Object) this));
		original.call(block, metadata, brightness);
		MinecraftForgeClient.afterBlockRender(block, ((BlockRenderer) (Object) this));
	}
}
