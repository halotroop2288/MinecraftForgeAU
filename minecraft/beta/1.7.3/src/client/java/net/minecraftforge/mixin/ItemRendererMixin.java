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
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	/**
	 * Calls {@link MinecraftForgeClient#overrideTexture(Object)} after every bindTexture call.
	 *
	 * @author halotroop2288
	 * @reason implement {@link MinecraftForgeClient#overrideTexture(Object)}
	 */
	@Inject(method = "renderItem", at = @At(
			value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
	private void forge$overrideTexture(EntityLiving entity, ItemStack stack, CallbackInfo ci) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock) MinecraftForgeClient.overrideTexture(Block.blocksList[stack.itemID]);
		else MinecraftForgeClient.overrideTexture(item);
	}
}
