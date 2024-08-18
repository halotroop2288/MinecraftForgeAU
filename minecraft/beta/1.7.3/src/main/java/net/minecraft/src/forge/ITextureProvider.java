/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.fabricmc.api.*;
import net.minecraft.src.*;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is to be implemented by a {@link Block} or {@link Item},
 * and provide custom texture capabilities.
 *
 * @author Space Toad
 * @since 1.0.0
 */
public interface ITextureProvider {
	/**
	 * <h4>If this is implemented on an item:</h4>
	 * This method has to return a path to a file that is the same size as {@code /items.png},
	 * but not <i>exactly</i>{@code /items.png}.
	 *
	 * <h4>If this is implemented on a block:</h4>
	 * This method has to return the path to a file that is the same size as {@code /terrain.png},
	 * but not <i>exactly</i> {@code /terrain.png}.<br>
	 * If the block implements {@code getRenderType}},
	 * it will use that terrain file to render texture instead of the default {@code /terrain.png} one.
	 *
	 * @return the path to the texture atlas file as described above
	 * @author Space Toad
	 * @since 1.0.0
	 */
	@Environment(EnvType.CLIENT)
	@NotNull String getTextureFile();
}
