/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.src.*;
import net.minecraft.src.forge.IBlockSecondaryProperties;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
	@Shadow
	public abstract int getBlockId(int x, int y, int z);

	/**
	 * @param x        the x position of the block
	 * @param y        the y position of the block
	 * @param z        the z position of the block
	 * @param original the original return value
	 * @return whether the block is effectively air
	 * @author halotroop2288
	 * @reason implement {@link IBlockSecondaryProperties#isAirBlock(World, int, int, int)}
	 */
	@WrapMethod(method = "isAirBlock")
	private boolean forge$isAirBlock(int x, int y, int z, Operation<Boolean> original) {
		int blockID = getBlockId(x, y, z);
		if (blockID == 0) return true;
		Block block = Block.blocksList[blockID];

		if (block instanceof IBlockSecondaryProperties) {
			return ((IBlockSecondaryProperties) block).isAirBlock((World) (Object) this, x, y, z);
		}

		return false;
	}

	/**
	 * @param ignored the bounding box to check
	 * @param x       the x position of the block
	 * @param y       the y position of the block
	 * @param z       the z position of the block
	 * @author halotroop2288
	 * @reason implement {@link IBlockSecondaryProperties#isBlockBurning(World, int, int, int)}
	 */
	@Inject(method = "isBoundingBoxBurning", cancellable = true, at = @At(value = "RETURN", ordinal = 0))
	private void forge$isBoundingBoxBurning_return_0(AxisAlignedBB ignored, CallbackInfoReturnable<Boolean> cir,
													 @Local(ordinal = 6) int x,
													 @Local(ordinal = 7) int y,
													 @Local(ordinal = 8) int z,
													 @Local(ordinal = 9) int blockID) {
		if (cir.getReturnValue()) return;
		Block block = Block.blocksList[blockID];
		if (block instanceof IBlockSecondaryProperties) {
			IBlockSecondaryProperties bsp = (IBlockSecondaryProperties) block;
			cir.setReturnValue(bsp.isBlockBurning((World) (Object) this, x, y, z));
		}
	}

	@Inject(method = "isBlockNormalCube", cancellable = true, at = @At(value = "RETURN", ordinal = 1))
	private void forge$isBlockNormalCube(int x, int y, int z, CallbackInfoReturnable<Boolean> cir, @Local Block block) {
		if (block instanceof IBlockSecondaryProperties) {
			IBlockSecondaryProperties bsp = (IBlockSecondaryProperties) block;
			cir.setReturnValue(bsp.isBlockNormalCube((World) (Object) this, x, y, z));
		}
	}

	/**
	 * @param blockID  the ID of the block to place
	 * @param x        the x position of the block to replace
	 * @param y        the y position of the block to replace
	 * @param z        the z position of the block to replace
	 * @param ignoreBB whether to ignore entity bounding box collisions
	 * @param side     the side to place the block on
	 * @param block    the block at the given position
	 * @author halotroop2288
	 * @reason implement {@link IBlockSecondaryProperties#isBlockReplaceable(World, int, int, int)}
	 */
	@Inject(method = "canBlockBePlacedAt", at = @At(value = "RETURN", ordinal = 1))
	private void forge$canBePlacedAt_return_1(int blockID, int x, int y, int z, boolean ignoreBB, int side,
											  CallbackInfoReturnable<Boolean> cir,
											  @Local(ordinal = 0) LocalRef<Block> block) {
		if (block instanceof IBlockSecondaryProperties) {
			IBlockSecondaryProperties bsp = (IBlockSecondaryProperties) block;
			if (bsp.isBlockReplaceable((World) (Object) this, x, y, z)) block.set(null);
		}
	}
}
