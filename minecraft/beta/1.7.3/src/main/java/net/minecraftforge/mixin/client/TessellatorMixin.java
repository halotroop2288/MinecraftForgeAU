/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin.client;

import net.minecraft.client.render.vertex.Tesselator;
import net.minecraftforge.ForgeTesselator;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Tesselator.class)
public abstract class TessellatorMixin {
	/**
	 * Replaces the vanilla tessellator
	 * for Eloraam's "Multiple-tessellator texture renderer"
	 * rather than patching it.
	 *
	 * @author halotroop2288
	 */
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/client/render/vertex/Tesselator;"))
	private static Tesselator forge$redirect(int bufferSize) {
		return new ForgeTesselator(bufferSize);
	}
}
