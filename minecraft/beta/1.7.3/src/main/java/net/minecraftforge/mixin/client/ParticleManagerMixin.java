/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import forge.*;
import net.fabricmc.api.*;
import net.minecraft.block.Block;
import net.minecraft.client.ParticleManager;
import net.minecraft.client.ParticleManager$BlockTextureParticles;
import net.minecraft.client.entity.particle.BlockParticle;
import net.minecraft.client.entity.particle.Particle;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.render.vertex.Tesselator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements ForgeParticleManager {
	@Shadow private TextureManager textureManager;

	@Shadow
	public abstract void add(Particle entityFX);

	@Unique private final List<ParticleManager$BlockTextureParticles> effectList = new ArrayList<>();

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Inject(method = "tick", at = @At("TAIL"))
	private void forge$init_tail(CallbackInfo ci) {
		for (ParticleManager$BlockTextureParticles entry : effectList) {
			for (int i = 0; i < entry.effects.size(); i++) {
				Particle particle = entry.effects.get(i);
				if (particle.removed) entry.effects.remove(i--);
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
	@WrapWithCondition(method = "render", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/entity/particle/Particle;render(Lnet/minecraft/client/render/vertex/Tesselator;FFFFFF)V"))
	private boolean forge$renderParticles_renderParticle(Particle particle, Tesselator tessellator,
														 float a, float b, float c, float d, float e, float f) {
		return !(particle instanceof BlockParticle);
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Inject(method = "render", at = @At("TAIL"))
	private void forge$renderParticles_tail(Entity entity, float f, CallbackInfo ci,
											@Local(ordinal = 1) float f1, @Local(ordinal = 2) float f2,
											@Local(ordinal = 2) float f3, @Local(ordinal = 3) float f4,
											@Local(ordinal = 5) float f5) {
		Tesselator tesselator = Tesselator.INSTANCE;

		for (ParticleManager$BlockTextureParticles entry : this.effectList) {
			glBindTexture(GL_TEXTURE_2D, textureManager.load(entry.texture));
			tesselator.begin();
			for (Particle entityFX : entry.effects) entityFX.render(tesselator, f, f1, f5, f2, f3, f4);
			tesselator.end();
		}
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Inject(method = "setWorld", at = @At("TAIL"))
	private void forge$clearEffects_tail(World world, CallbackInfo ci) {
		for (ParticleManager$BlockTextureParticles entry : this.effectList) entry.effects.clear();
		this.effectList.clear();
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Redirect(method = "handleBlockBreaking", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/ParticleManager;add(Lnet/minecraft/client/entity/particle/Particle;)V"))
	private void forge$addBlockDestroyEffects_addEffect(ParticleManager instance, Particle entityFX,
														@Local Block block) {
		this.addDigParticleEffect((BlockParticle) entityFX, block);
	}

	/**
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 * @reason infinite sprite support
	 */
	@Redirect(method = "handleBlockMining", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ParticleManager;add(Lnet/minecraft/client/entity/particle/Particle;)V"))
	private void forge$addBlockHitEffects_addEffect(ParticleManager instance, Particle entityFX, @Local Block block) {
		this.addDigParticleEffect((BlockParticle) entityFX, block);
	}

	@Override
	public void addDigParticleEffect(BlockParticle digEffect, Block block) {
		String comp = block instanceof ITextureProvider ? ((ITextureProvider) block).getTextureFile() : "/terrain.png";
		boolean added = false;

		for (ParticleManager$BlockTextureParticles entry : this.effectList) {
			if (!entry.texture.equals(comp)) continue;
			entry.effects.add(digEffect);
			added = true;
		}

		if (!added) {
			ParticleManager$BlockTextureParticles entry = new ParticleManager$BlockTextureParticles();
			entry.texture = comp;
			entry.effects.add(digEffect);
			this.effectList.add(entry);
		}

		this.add(digEffect);
	}
}
