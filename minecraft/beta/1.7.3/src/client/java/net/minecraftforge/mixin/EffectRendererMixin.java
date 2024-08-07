/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraftforge.injection.ForgeEffectRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

@Environment(EnvType.CLIENT)
@Mixin(EffectRenderer.class)
public abstract class EffectRendererMixin implements ForgeEffectRenderer {
	@Shadow private RenderEngine renderer;

	@Shadow
	public abstract void addEffect(EntityFX entityFX);

	@Unique private final List<EffectRenderer$BlockTextureParticles> effectList = new ArrayList<>();

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Inject(method = "updateEffects", at = @At("TAIL"))
	private void forge$init_tail(CallbackInfo ci) {
		for (EffectRenderer$BlockTextureParticles entry : effectList) {
			for (int i = 0; i < entry.effects.size(); i++) {
				EntityFX entityFX = entry.effects.get(i);
				if (entityFX.isDead) entry.effects.remove(i--);
			}
		}
	}

	/**
	 * @return whether the operation should continue
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@WrapWithCondition(method = "renderParticles", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/src/EntityFX;renderParticle(Lnet/minecraft/src/Tessellator;FFFFFF)V"))
	private boolean forge$renderParticles_renderParticle(EntityFX entityFX, Tessellator tessellator,
														 float a, float b, float c, float d, float e, float f) {
		return !(entityFX instanceof EntityDiggingFX);
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Inject(method = "renderParticles", at = @At("TAIL"))
	private void forge$renderParticles_tail(Entity entity, float f, CallbackInfo ci,
											@Local(ordinal = 1) float f1, @Local(ordinal = 2) float f2,
											@Local(ordinal = 2) float f3, @Local(ordinal = 3) float f4,
											@Local(ordinal = 5) float f5) {
		Tessellator tessellator = Tessellator.instance;

		for (EffectRenderer$BlockTextureParticles entry : this.effectList) {
			glBindTexture(GL_TEXTURE_2D, renderer.getTexture(entry.texture));
			tessellator.startDrawingQuads();
			for (EntityFX entityFX : entry.effects) entityFX.renderParticle(tessellator, f, f1, f5, f2, f3, f4);
			tessellator.draw();
		}
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Inject(method = "clearEffects", at = @At("TAIL"))
	private void forge$clearEffects_tail(World world, CallbackInfo ci) {
		for (EffectRenderer$BlockTextureParticles entry : this.effectList) entry.effects.clear();
		this.effectList.clear();
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Redirect(method = "addBlockDestroyEffects", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/src/EffectRenderer;addEffect(Lnet/minecraft/src/EntityFX;)V"))
	private void forge$addBlockDestroyEffects_addEffect(EffectRenderer instance, EntityFX entityFX, @Local Block block) {
		instance.addDigParticleEffect((EntityDiggingFX) entityFX, block);
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Redirect(method = "addBlockHitEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EffectRenderer;addEffect(Lnet/minecraft/src/EntityFX;)V"))
	private void forge$addBlockHitEffects_addEffect(EffectRenderer instance, EntityFX entityFX, @Local Block block) {
		instance.addDigParticleEffect((EntityDiggingFX) entityFX, block);
	}

	@Override
	public void addDigParticleEffect(EntityDiggingFX digEffect, Block block) {
		String comp = block instanceof ITextureProvider ? ((ITextureProvider) block).getTextureFile() : "/terrain.png";
		boolean added = false;

		for (EffectRenderer$BlockTextureParticles entry : this.effectList) {
			if (!entry.texture.equals(comp)) continue;
			entry.effects.add(digEffect);
			added = true;
		}

		if (!added) {
			EffectRenderer$BlockTextureParticles entry = new EffectRenderer$BlockTextureParticles();
			entry.texture = comp;
			entry.effects.add(digEffect);
			this.effectList.add(entry);
		}

		this.addEffect(digEffect);
	}
}
