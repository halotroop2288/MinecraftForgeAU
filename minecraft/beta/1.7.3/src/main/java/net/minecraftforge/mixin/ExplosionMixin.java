/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.src.*;
import net.minecraft.src.forge.ISpecialResistance;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author halotroop2288
 */
@Mixin(Explosion.class)
public abstract class ExplosionMixin {
	@Shadow private World worldObj;

	@Shadow public double explosionX;

	@Shadow public double explosionY;

	@Shadow public double explosionZ;

	@Shadow public Entity exploder;

	/**
	 * @author halotroop2288
	 * @reason implement {@link ISpecialResistance}
	 */
	@WrapOperation(method = "doExplosion", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/src/Block;getExplosionResistance(Lnet/minecraft/src/Entity;)F"))
	private float forge$getBlastResistance(Block block, Entity entity, Operation<Float> operation,
										   @Local(ordinal = 4) LocalIntRef x,
										   @Local(ordinal = 5) LocalIntRef y,
										   @Local(ordinal = 6) LocalIntRef z) {
		if (block instanceof ISpecialResistance) {
			return ((ISpecialResistance) block).getSpecialExplosionResistance(this.worldObj,
				x.get(), y.get(), z.get(),
				this.explosionX, this.explosionY, this.explosionZ,
				this.exploder
			);
		}

		return operation.call(block, entity);
	}
}
