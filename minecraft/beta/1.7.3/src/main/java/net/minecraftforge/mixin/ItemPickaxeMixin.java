/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(ItemPickaxe.class)
public abstract class ItemPickaxeMixin {
	/**
	 * @author halotroop2288
	 * @reason Implements {@link IHarvestHandler}
	 */
	@Inject(method = "canHarvestBlock", cancellable = true, at = @At(value = "FIELD", ordinal = 0,
		target = "Lnet/minecraft/src/Block;blockMaterial:Lnet/minecraft/src/Material;"))
	private void forge$canHarvestBlock_return(Block block, CallbackInfoReturnable<Boolean> cir) {
		if (MinecraftForge.canHarvestBlock(((ItemPickaxe) (Object) this), block)) {
			cir.setReturnValue(true);
		}
	}
}
