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

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
	@Inject(method = "dispense", cancellable = true, at = @At(
		value = "FIELD", opcode = Opcodes.GETSTATIC,
		target = "Lnet/minecraft/item/Item;ARROW:Lnet/minecraft/item/Item;"
	))
	private void modloader$dispense_ARROW(@NotNull World world, int blockX, int blockY, int blockZ,
										  @NotNull Random random, @NotNull CallbackInfo ci,
										  @Local(ordinal = 0) double entityX,
										  @Local(ordinal = 1) double entityY,
										  @Local(ordinal = 2) double entityZ,
										  @Local(ordinal = 4) int xVelocity,
										  @Local(ordinal = 5) int yVelocity,
										  @Local ItemStack stack) {
		if (ModLoader.dispenseEntity(world, entityX, entityY, entityZ, xVelocity, yVelocity, stack)) {
			world.doEvent(2000, blockX, blockY, blockZ, xVelocity + 1 + (yVelocity + 1) * 3);
			ci.cancel();
		}
	}
}
