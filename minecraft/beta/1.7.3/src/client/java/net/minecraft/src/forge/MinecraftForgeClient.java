/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.fabricmc.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Space Toad
 * @author Flowerchild
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class MinecraftForgeClient {
	private MinecraftForgeClient() {
	}

	/**
	 * Re-initializes the tessellator and binds a custom texture before rendering a block
	 * that implements {@link ITextureProvider}.
	 *
	 * @param block    the block being rendered
	 * @param renderer the block renderer instance
	 * @author Space Toad
	 * @author halotroop2288
	 * @since 1.0.0
	 */
	@ApiStatus.Internal
	public static void beforeBlockRender(Block block, RenderBlocks renderer) {
		if (block instanceof ITextureProvider && renderer.overrideBlockTexture == -1) {
			Tessellator tessellator = Tessellator.instance;
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1F, 0.0F);
			GL11.glBindTexture(GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine
				.getTexture(((ITextureProvider) (block)).getTextureFile()));
		}
	}

	/**
	 * Re-initializes the tessellator and binds {@code terrain.png} after rendering a block
	 * to avoid bugs caused by binding a custom texture from {@link ITextureProvider}.
	 *
	 * @param block    the block being rendered
	 * @param renderer the block renderer instance
	 * @author Space Toad
	 * @author halotroop2288
	 * @since 1.0.0
	 */
	@ApiStatus.Internal
	public static void afterBlockRender(Block block, RenderBlocks renderer) {
		if (block instanceof ITextureProvider && renderer.overrideBlockTexture == -1) {
			Tessellator tessellator = Tessellator.instance;
			tessellator.draw();
			tessellator.startDrawingQuads();
			GL11.glBindTexture(GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png"));
		}
	}

	/**
	 * If the provided object is an instance of {@link ITextureProvider},
	 * binds the texture provided by the object.
	 *
	 * @param object an object to get the texture for
	 * @author Space Toad
	 * @author halotroop2288
	 * @since 1.0.0
	 */
	public static void overrideTexture(Object object) {
		assert object instanceof Item || object instanceof Block : "Overriding texture for a non-block/item object";
		if (object instanceof ITextureProvider) {
			ITextureProvider provider = (ITextureProvider) object;
			String texturePath = provider.getTextureFile();
			glBindTexture(GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine.getTexture(texturePath));
		}
	}

	/**
	 * Called during {@link Minecraft#startGame()} after singletons are created.
	 *
	 * @author FlowerChild
	 * @since 1.0.0
	 */
	public static void onGameStart() {
	}
}
