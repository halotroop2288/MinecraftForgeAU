/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import forge.*;
import net.fabricmc.api.*;
import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	@Inject(method = "useBlock", cancellable = true, at = @At("HEAD"))
	private void forge$sendPlaceBlock_head(PlayerEntity player, World world, ItemStack stack,
										   int x, int y, int z, int side,
										   CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && stack.getItem() instanceof IUseItemFirst) {
			if (((IUseItemFirst) stack.getItem()).onItemUseFirst(stack, player, world, x, y, z, side))
				cir.setReturnValue(true);
		}
	}
}
