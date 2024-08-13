/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.minecraft.src.*;
import net.minecraftforge.injection.ForgeItemTool;
import org.spongepowered.asm.mixin.*;

import java.util.*;

/**
 * @author halotroop2288
 */
@Mixin(ItemTool.class)
public abstract class ItemToolMixin implements ForgeItemTool {
	@Shadow private Block[] blocksEffectiveAgainst;

	/**
	 * @param block the block to harvest
	 * @author Space Toad
	 * @author halotroop2288
	 */
	@Override
	public void addBlockEffectiveAgainst(Block block) {
		List<Block> blocks = new ArrayList<>(Arrays.asList(blocksEffectiveAgainst));
		blocks.add(block);
		blocksEffectiveAgainst = blocks.toArray(new Block[0]);
	}
}
