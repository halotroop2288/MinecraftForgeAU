/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.MinecraftForgeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Environment(EnvType.CLIENT)
@Mixin(RenderItem.class)
public abstract class RenderItemMixin {
	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every call
	 * to {@link RenderItem#loadTexture(String) loadTexture("/terrain.png")}
	 *
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "doRenderItem", require = 2,
			at = @At(value = "CONSTANT", shift = At.Shift.AFTER, args = "stringValue=/terrain.png"))
	private void forge$overrideTerrainTexture(EntityItem itemEntity, double x, double y, double z,
											  float a, float b, CallbackInfo ci) {
		MinecraftForgeClient.overrideTexture(Block.blocksList[itemEntity.item.itemID]);
	}

	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every call
	 * to {@link RenderItem#loadTexture(String) loadTexture("/gui/items.png")}
	 *
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "doRenderItem", require = 1,
			at = @At(value = "CONSTANT", shift = At.Shift.AFTER, args = "stringValue=/gui/items.png"))
	private void forge$overrideItemsTexture(EntityItem itemEntity, double x, double y, double z,
											float a, float b, CallbackInfo ci) {
		MinecraftForgeClient.overrideTexture(itemEntity.item.getItem());
	}

	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every call
	 * to {@link RenderEngine#bindTexture(int)}
	 *
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "drawItemIntoGui", require = 3, at = @At(
			value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lnet/minecraft/src/RenderEngine;bindTexture(I)V"))
	private void forge$overrideTerrainTexture(FontRenderer font, RenderEngine engine, int itemID, int a, int b,
											  int c, int d, CallbackInfo ci) {
		if (itemID < 256) MinecraftForgeClient.overrideTexture(Block.blocksList[itemID]);
		else MinecraftForgeClient.overrideTexture(Item.itemsList[itemID]);
	}
}
