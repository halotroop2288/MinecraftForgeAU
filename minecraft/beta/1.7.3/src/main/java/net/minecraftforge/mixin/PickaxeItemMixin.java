/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import forge.*;
import net.minecraft.block.Block;
import net.minecraft.item.PickaxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin {
	/**
	 * @author halotroop2288
	 * @reason Implements {@link IHarvestHandler}
	 */
	@Inject(method = "canMineBlock", cancellable = true, at = @At(value = "FIELD", ordinal = 0,
		target = "Lnet/minecraft/block/Block;material:Lnet/minecraft/block/material/Material;"))
	private void forge$canHarvestBlock_return(Block block, CallbackInfoReturnable<Boolean> cir) {
		if (MinecraftForge.canHarvestBlock(((PickaxeItem) (Object) this), block)) {
			cir.setReturnValue(true);
		}
	}
}
