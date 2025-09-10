/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.ToolItem;
import net.minecraftforge.injection.ForgeItemTool;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.*;

/**
 * @author halotroop2288
 */
@Mixin(ToolItem.class)
public abstract class ToolItemMixin implements ForgeItemTool {
	@Shadow private Block[] effectiveBlocks;

	/**
	 * @author Space Toad
	 * @author halotroop2288
	 */
	@Override
	public void addBlockEffectiveAgainst(@NotNull Block block) {
		List<Block> blocks = new ArrayList<>(Arrays.asList(effectiveBlocks));
		blocks.add(block);
		effectiveBlocks = blocks.toArray(new Block[0]);
	}
}
