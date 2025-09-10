/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import forge.MinecraftForge;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(BucketItem.class)
public abstract class BucketItemMixin {
	/**
	 * @author halotroop2288
	 * @reason Implements {@link IBucketHandler}
	 */
	@Inject(method = "startUsing", cancellable = true, at = @At(value = "INVOKE", ordinal = 0,
		target = "Lnet/minecraft/world/World;getMaterial(III)Lnet/minecraft/block/material/Material;")
	)
	private void forge$customBucket(ItemStack stack, World world, PlayerEntity player,
									CallbackInfoReturnable<ItemStack> cir,
									@Local(ordinal = 0) int x,
									@Local(ordinal = 1) int y,
									@Local(ordinal = 2) int z) {
		ItemStack customBucket = MinecraftForge.fillCustomBucket(world, x, y, z);
		if (customBucket != null) cir.setReturnValue(customBucket);
	}
}
