/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.*;
import net.minecraft.src.forge.IConnectRedstone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author halotroop2288
 */
@Mixin(BlockRedstoneWire.class)
public abstract class BlockRedstoneWireMixin {
	/**
	 * @param world     the world to act on
	 * @param x         the x position of this block
	 * @param y         the y position of this block
	 * @param z         the z position of this block
	 * @param direction the direction from which the redstone wire is connecting
	 * @author halotroop2288
	 * @reason implement {@link IConnectRedstone}
	 */
	@Inject(method = "isPowerProviderOrWire", cancellable = true,
		at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;canProvidePower()Z"))
	private static void forge$isPowerProviderOrWire_canProvidePower(IBlockAccess world, int x, int y, int z, int direction,
																	CallbackInfoReturnable<Boolean> cir,
																	@Local(ordinal = 4) int blockID) {
		Block block = Block.blocksList[blockID];
		if (block instanceof IConnectRedstone) {
			cir.setReturnValue(((IConnectRedstone) block).canConnectRedstone(world, x, y, z, direction));
		}
	}
}
