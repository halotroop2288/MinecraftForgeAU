/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package forge;

import net.minecraft.block.Block;
import net.minecraft.client.ParticleManager;
import net.minecraft.client.entity.particle.BlockParticle;
import org.jetbrains.annotations.ApiStatus;

/**
 * Adds public methods to {@link ParticleManager} via Fabric Interface Injectors.
 *
 * @author halotroop2288
 */
@ApiStatus.Internal
public interface ForgeParticleManager {
	/**
	 * Adds the given effect to the {@code effectList} if it is not already present.
	 *
	 * @param blockParticle the particle to add to the list
	 * @param block     the block that produced the particle
	 * @author halotroop2288
	 * @author FlowerChild
	 * @author Space Toad
	 */
	default void addDigParticleEffect(BlockParticle blockParticle, Block block) {
		throw new RuntimeException("Not implemented.");
	}
}
