/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(ItemBucket.class)
public abstract class ItemBucketMixin {
	/**
	 * @param stack  the stack containing an empty bucket
	 * @param world  the world
	 * @param player the interacting player
	 * @param x      block x position
	 * @param y      block y position
	 * @param z      block z position
	 * @author halotroop2288
	 * @reason Implements {@link IBucketHandler}
	 */
	@Inject(method = "onItemRightClick", cancellable = true, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/src/World;getBlockMaterial(III)Lnet/minecraft/src/Material;"))
	private void forge$customBucket(ItemStack stack, World world, EntityPlayer player,
									CallbackInfoReturnable<ItemStack> cir,
									@Local(ordinal = 0) int x,
									@Local(ordinal = 1) int y,
									@Local(ordinal = 2) int z) {
		ItemStack customBucket = MinecraftForge.fillCustomBucket(world, x, y, z);
		if (customBucket != null) cir.setReturnValue(customBucket);
	}
}
