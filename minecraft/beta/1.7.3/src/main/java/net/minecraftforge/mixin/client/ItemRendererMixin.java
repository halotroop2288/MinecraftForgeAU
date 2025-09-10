/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import net.fabricmc.api.*;
import forge.*;
import net.minecraft.block.Block;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every call
	 * to {@link ItemRenderer#bindTexture(String) bindTexture("/terrain.png")}
	 *
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", require = 2,
		at = @At(value = "CONSTANT", shift = At.Shift.AFTER, args = "stringValue=/terrain.png"))
	private void forge$overrideTerrainTexture(ItemEntity itemEntity, double x, double y, double z,
											  float a, float b, CallbackInfo ci) {
		MinecraftForgeClient.overrideTexture(Block.BY_ID[itemEntity.item.id]);
	}

	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every call
	 * to {@link ItemRenderer#bindTexture(String) bindTexture("/gui/items.png")}
	 *
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", require = 1,
		at = @At(value = "CONSTANT", shift = At.Shift.AFTER, args = "stringValue=/gui/items.png"))
	private void forge$overrideItemsTexture(ItemEntity itemEntity, double x, double y, double z,
											float a, float b, CallbackInfo ci) {
		MinecraftForgeClient.overrideTexture(itemEntity.item.getItem());
	}

	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every call
	 * to {@link TextureManager#bind(int)}
	 *
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "renderGuiItem", require = 3, at = @At(
		value = "INVOKE", shift = At.Shift.AFTER,
		target = "Lnet/minecraft/client/render/texture/TextureManager;bind(I)V"))
	private void forge$overrideTerrainTexture(TextRenderer font, TextureManager textureManager,
											  int itemID, int a, int b, int c, int d, CallbackInfo ci) {
		if (itemID < 256) MinecraftForgeClient.overrideTexture(Block.BY_ID[itemID]);
		else MinecraftForgeClient.overrideTexture(Item.BY_ID[itemID]);
	}
}
