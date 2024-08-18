/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.Block;

/**
 * This interface has to be implemented by a {@link Block},
 * for deciding whether it should be rendered during a given pass.
 *
 * @author Eloraam
 * @since 1.0.0
 */
public interface IMultipassRender {
	/**
	 * Checks whether the block can be rendered during the given rendering pass.
	 *
	 * @param pass the current render pass
	 * @return whether the block should be rendered during this pass
	 * @author Eloraam
	 * @since 1.0.0
	 */
	boolean canRenderInPass(int pass);
}
