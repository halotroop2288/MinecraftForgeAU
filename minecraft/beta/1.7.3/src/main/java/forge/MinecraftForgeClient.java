/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package forge;

import net.fabricmc.api.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.vertex.Tesselator;
import net.minecraft.item.Item;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Client hooks for Minecraft mod development.
 *
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
	public static void beforeBlockRender(Block block, BlockRenderer renderer) {
		if (block instanceof ITextureProvider && renderer.forcedSprite == -1) {
			Tesselator tessellator = Tesselator.INSTANCE;
			tessellator.end();
			tessellator.begin();
			tessellator.normal(0F, -1F, 0F);
			GL11.glBindTexture(GL_TEXTURE_2D, Minecraft.INSTANCE.textureManager
				.load(((ITextureProvider) (block)).getTextureFile()));
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
	public static void afterBlockRender(Block block, BlockRenderer renderer) {
		if (block instanceof ITextureProvider && renderer.forcedSprite == -1) {
			Tesselator tessellator = Tesselator.INSTANCE;
			tessellator.end();
			tessellator.begin();
			GL11.glBindTexture(GL_TEXTURE_2D, Minecraft.INSTANCE.textureManager.load("/terrain.png"));
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
			glBindTexture(GL_TEXTURE_2D, Minecraft.INSTANCE.textureManager.load(texturePath));
		}
	}

	/**
	 * Called during {@link Minecraft#init()} after singletons are created.
	 *
	 * @author FlowerChild
	 * @since 1.0.0
	 */
	public static void onGameStart() {
	}
}
