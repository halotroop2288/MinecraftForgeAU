/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import net.fabricmc.api.*;
import net.minecraft.block.Block;
import net.minecraft.client.render.ItemInHandRenderer;
import forge.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author halotroop2288
 */
@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every bindTexture call.
	 *
	 * @author halotroop2288
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "render", at = @At(
		value = "INVOKE", shift = At.Shift.AFTER,
		target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
	private void forge$overrideTexture(MobEntity entity, ItemStack stack, CallbackInfo ci) {
		Item item = stack.getItem();
		if (item instanceof BlockItem) MinecraftForgeClient.overrideTexture(Block.BY_ID[stack.id]);
		else MinecraftForgeClient.overrideTexture(item);
	}
}
