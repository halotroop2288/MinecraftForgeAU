/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.player.PlayerEntity;
import forge.ISpecialArmor;
import net.minecraft.entity.mob.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends MobEntity {
	@Shadow public PlayerInventory inventory;

	private PlayerEntityMixin(World w) {
		super(w);
		throw new RuntimeException();
	}

	/**
	 * @param i      immutable version of {@code damage}, ignored.
	 * @param damage the damage amount
	 * @author halotroop2288
	 * @reason implement {@link ISpecialArmor}
	 */
	@Inject(method = "applyDamage", cancellable = true, at = @At("HEAD"))
	private void forge$damageEntity_head(int i, CallbackInfo ci, @Local(argsOnly = true) LocalIntRef damage) {
		boolean doRegularComputation = true;
		for (ItemStack stack : inventory.armor) {
			if (stack == null) continue;
			Item item = stack.getItem();
			if (item instanceof ISpecialArmor) {
				ISpecialArmor armor = (ISpecialArmor) item;
				damage.set(armor.adjustArmorDamage(damage.get()));
				doRegularComputation = doRegularComputation && armor.allowRegularComputation();
			}
		}

		if (!doRegularComputation) {
			super.applyDamage(damage.get());
			ci.cancel();
		}
	}
}
