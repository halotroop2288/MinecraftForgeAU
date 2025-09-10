/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import forge.ISpecialResistance;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author halotroop2288
 */
@Mixin(Explosion.class)
public abstract class ExplosionMixin {
	@Shadow private World world;

	@Shadow public double x;

	@Shadow public double y;

	@Shadow public double z;

	@Shadow public Entity source;

	/**
	 * @author halotroop2288
	 * @reason implement {@link ISpecialResistance}
	 */
	@WrapOperation(method = "damageEntities", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/block/Block;getBlastResistance(Lnet/minecraft/entity/Entity;)F"))
	private float forge$getBlastResistance(Block block, Entity entity, Operation<Float> operation,
										   @Local(ordinal = 4) LocalIntRef x,
										   @Local(ordinal = 5) LocalIntRef y,
										   @Local(ordinal = 6) LocalIntRef z) {
		if (block instanceof ISpecialResistance) {
			return ((ISpecialResistance) block).getSpecialExplosionResistance(this.world,
				x.get(), y.get(), z.get(),
				this.x, this.y, this.z,
				this.source
			);
		}

		return operation.call(block, entity);
	}
}
