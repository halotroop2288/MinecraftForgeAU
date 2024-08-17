/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.src.*;
import net.minecraft.src.forge.ISpecialArmor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLiving {
	@Shadow public InventoryPlayer inventory;

	private EntityPlayerMixin(World w) {
		super(w);
		throw new RuntimeException();
	}

	/**
	 * @param i      immutable version of {@code damage}, ignored.
	 * @param damage the damage amount
	 * @author halotroop2288
	 * @reason implement {@link ISpecialArmor}
	 */
	@Inject(method = "damageEntity", cancellable = true, at = @At("HEAD"))
	private void forge$damageEntity_head(int i, CallbackInfo ci, @Local(argsOnly = true) LocalIntRef damage) {
		boolean doRegularComputation = true;
		for (ItemStack stack : inventory.armorInventory) {
			if (stack == null) continue;
			Item item = stack.getItem();
			if (item instanceof ISpecialArmor) {
				ISpecialArmor armor = (ISpecialArmor) item;
				damage.set(armor.adjustArmorDamage(damage.get()));
				doRegularComputation = doRegularComputation && armor.allowRegularComputation();
			}
		}

		if (!doRegularComputation) {
			super.damageEntity(damage.get());
			ci.cancel();
		}
	}
}
