package net.minecraftforge.mixin;

import net.minecraft.src.Tessellator;
import net.minecraftforge.ForgeTessellator;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Tessellator.class)
public abstract class TessellatorMixin {
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/src/Tessellator;"))
	private static Tessellator redirect(int bufferSize) {
		return new ForgeTessellator(bufferSize);
	}
}
