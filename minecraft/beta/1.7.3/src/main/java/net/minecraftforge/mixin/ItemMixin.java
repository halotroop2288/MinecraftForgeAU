/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import forge.IUseItemFirst;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(Item.class)
public abstract class ItemMixin {
	/**
	 * @author halotroop2288
	 * @reason implement {@link IUseItemFirst}
	 */
	@Inject(method = "useOn", cancellable = true, at = @At("HEAD"))
	private void forge$onItemUse_head(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side,
									  CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof IUseItemFirst && ((IUseItemFirst) this)
			.onItemUseFirst(stack, player, world, x, y, z, side)) {
			cir.setReturnValue(true);
		}
	}
}
