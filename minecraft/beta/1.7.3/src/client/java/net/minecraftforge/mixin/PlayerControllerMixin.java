/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.IUseItemFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerController.class)
public abstract class PlayerControllerMixin {
	@Inject(method = "sendPlaceBlock", cancellable = true, at = @At("HEAD"))
	private void forge$sendPlaceBlock_head(EntityPlayer player, World world, ItemStack stack,
										   int x, int y, int z, int side,
										   CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && stack.getItem() instanceof IUseItemFirst) {
			if (((IUseItemFirst) stack.getItem()).onItemUseFirst(stack, player, world, x, y, z, side))
				cir.setReturnValue(true);
		}
	}
}
