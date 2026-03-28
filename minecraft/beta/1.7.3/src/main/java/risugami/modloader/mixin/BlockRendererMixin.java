/*
 * Copyright (c) 2026 Caroline Joy Bell.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package risugami.modloader.mixin;

import risugami.modloader.ModLoader;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public class BlockRendererMixin {
	@Shadow
	private WorldView world;

	@Inject(method = "renderAsItem", at = @At("RETURN"))
	private void modloader$renderAsItem_RETURN(Block block, int metadata, float brightness, CallbackInfo ci) {
		int renderType = block.getRenderType();

		if (renderType != 0 && renderType != 16) {
			switch (renderType) {
				case 1:
				case 2:
				case 6:
				case 10:
				case 11:
				case 13:
					break;
				default:
					ModLoader.renderInvBlock((BlockRenderer) (Object) this, block, metadata, renderType);
			}
		}
	}

	@ModifyReturnValue(method = "tesselateInWorld", at = @At(value = "RETURN", ordinal = 18))
	private boolean modloader$tesselateInWorld_last_return(
		boolean original,
		@Local(argsOnly = true) Block block,
		@Local(ordinal = 0, argsOnly = true) int x,
		@Local(ordinal = 1, argsOnly = true) int y,
		@Local(ordinal = 2, argsOnly = true) int z,
		@Local(ordinal = 3) int modelID
	) {
		return ModLoader.renderWorldBlock((BlockRenderer) (Object) this, this.world, x, y, z, block, modelID);
	}

	@ModifyReturnValue(method = "isItem3d", at = @At(value = "RETURN", ordinal = 5))
	private static boolean modloader$isBlock3D(boolean original, @Local(argsOnly = true) int type) {
		return original || ModLoader.renderBlockIsItemFull3D(type);
	}
}
