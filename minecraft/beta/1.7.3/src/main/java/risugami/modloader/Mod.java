/*
 * Copyright (c) 2026 Caroline Joy Bell.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package risugami.modloader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.AchievementStat;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * Interface version of {@link BaseMod}
 * with the intention to allow extension of other classes.
 *
 * @author Risugami
 * @author halotroop2288
 */
public interface Mod {
	// Metadata - This must be implemented for other mods to query.

	/**
	 * <p>Not to be confused with the semantic version provided to Fabric Loader.</p>
	 * <ul><li>Example: "ModLoader Beta 1.7.3"</li></ul>
	 *
	 * @return a unique current version string for the mod
	 */
	String version();

	// Events and Callbacks - These are all called by ModLoader
	// Override to subscribe to the event.

	default int addFuel(@NotNull ItemStack stack) {
		return addFuel(stack.id);
	}

	default int addFuel(int id) {
		return 0;
	}

	@Environment(EnvType.CLIENT)
	default void addRenderer(@NotNull Map<Class<? extends Entity>, @NotNull EntityRenderer> renderers) {
	}

	default boolean dispenseEntity(@NotNull World world,
								   double x, double y, double z,
								   int xVel, int zVel,
								   @NotNull ItemStack item) {
		return false;
	}

	default void generateNether(@NotNull World world, @NotNull Random random,
								int chunkX, int chunkZ) {
	}

	default void generateSurface(@NotNull World world, @NotNull Random random,
								 int chunkX, int chunkZ) {
	}

	default void keyboardEvent(@NotNull KeyBinding event) {
	}

	default void modsLoaded() {
	}

	/**
	 * @see Minecraft#textureManager
	 */
	@Environment(EnvType.CLIENT)
	default void registerAnimation(@NotNull Minecraft client) {
	}

	@Environment(EnvType.CLIENT)
	default void renderInvBlock(@NotNull BlockRenderer renderer,
								@NotNull Block block, int metadata, int modelID
	) {
	}

	@Environment(EnvType.CLIENT)
	default boolean renderWorldBlock(@NotNull BlockRenderer renderer, @NotNull WorldView world,
									 int x, int y, int z,
									 @NotNull Block block, int modelID
	) {
		return false;
	}

	default void takenFromCrafting(@NotNull PlayerEntity player, @NotNull ItemStack result) {
	}

	default void takenFromFurnace(@NotNull PlayerEntity player, @NotNull ItemStack result) {
	}

	default void onItemPickup(@NotNull PlayerEntity player, @NotNull ItemStack stack) {
	}

	// Helpers - These simply call the ModLoader functions.
	// Override if you want to add a mod namespaces, etc.

	@SuppressWarnings("unused")
	default void addAchievementDescription(@NotNull AchievementStat achievement,
										   @NotNull String name, @NotNull String description) {
		ModLoader.addAchievementDesc(achievement, name, description);
	}

	default void addName(@NotNull Block block, @NotNull String name) {
		ModLoader.addBlockName(block, name);
	}

	default void addName(@NotNull Item item, @NotNull String name) {
		ModLoader.addItemName(item, name);
	}

	default void addName(@NotNull ItemStack stack, @NotNull String name) {
		ModLoader.addStackName(stack, name);
	}

	default void addShapedRecipe(@NotNull ItemStack output, @NotNull Object... params) {
		ModLoader.addShapedRecipe(output, params);
	}

	default void addShapelessRecipe(@NotNull ItemStack output, @NotNull Object... ingredients) {
		ModLoader.addShapelessRecipe(output, ingredients);
	}

	default void addSmeltingRecipe(int input, @NotNull ItemStack output) {
		ModLoader.addSmeltingRecipe(input, output);
	}

	default void registerBlock(@NotNull Block block) {
		this.registerBlock(block, null);
	}

	default void registerBlock(@NotNull Block block, @Nullable Function<Integer, ? extends BlockItem> itemSupplier) {
		ModLoader.registerBlock(block, itemSupplier);
	}

	default void registerBlockEntity(@NotNull Class<? extends BlockEntity> blockEntityClass, @NotNull String id) {
		this.registerTileEntity(blockEntityClass, id, null);
	}

	default void registerTileEntity(
		@NotNull Class<? extends BlockEntity> blockEntityClass,
		@NotNull String id,
		@Nullable BlockEntityRenderer renderer
	) {
		ModLoader.registerTileEntity(blockEntityClass, id, renderer);
	}

	@Environment(EnvType.CLIENT)
	default void addAnimation(DynamicTexture animation) {
		ModLoader.addAnimation(animation);
	}

	@Environment(EnvType.CLIENT)
	default int addArmorType(String armorType) {
		return ModLoader.addArmor(armorType);
	}

	@Environment(EnvType.CLIENT)
	default void registerKey(KeyBinding keyHandler, boolean allowRepeat) {
		ModLoader.registerKey(this, keyHandler, allowRepeat);
	}

	@Environment(EnvType.CLIENT)
	default int addOverride(String fileToOverride, String fileToAdd) {
		int i = ModLoader.getUniqueSpriteIndex(fileToOverride);
		this.addOverride(fileToOverride, fileToAdd, i);
		return i;
	}

	@Environment(EnvType.CLIENT)
	default void addOverride(String fileToOverride, String fileToAdd, int spriteID) {
		ModLoader.addOverride(fileToOverride, fileToAdd, spriteID);
	}

	default void addSpawn(@NotNull Class<? extends MobEntity> entityClass, int weightedProb, @NotNull MobCategory spawnList) {
		this.addSpawn(entityClass, weightedProb, spawnList, ModLoader.vanillaBiomes);
	}

	default void addSpawn(@NotNull String entityName, int weightedProb, @NotNull MobCategory spawnList) {
		this.addSpawn(entityName, weightedProb, spawnList, ModLoader.vanillaBiomes);
	}

	@SuppressWarnings("unchecked")
	default void addSpawn(@NotNull String entityName, int weightedProb, @NotNull MobCategory spawnList, @NotNull Biome... biomes) {
		Class<? extends Entity> entityClass = (Class<? extends Entity>) Entities.KEY_TO_TYPE.get(entityName);
		if (entityClass != null && MobEntity.class.isAssignableFrom(entityClass)) {
			this.addSpawn((Class<? extends MobEntity>) entityClass, weightedProb, spawnList, biomes);
		}
	}

	default void addSpawn(@NotNull Class<? extends MobEntity> entityClass, int weightedProb, @NotNull MobCategory spawnList, @NotNull Biome... biomes) {
		ModLoader.addSpawn(entityClass, weightedProb, spawnList, biomes);
	}

	default void removeSpawn(@NotNull Class<? extends MobEntity> entityClass, @NotNull MobCategory spawnList) {
		this.removeSpawn(entityClass, spawnList, ModLoader.vanillaBiomes);
	}

	default void removeSpawn(@NotNull String entityName, @NotNull MobCategory spawnList) {
		this.removeSpawn(entityName, spawnList, ModLoader.vanillaBiomes);
	}

	@SuppressWarnings("unchecked")
	default void removeSpawn(@NotNull String entityName, @NotNull MobCategory category, @NotNull Biome... biomes) {
		this.removeSpawn((Class<? extends MobEntity>) Entities.KEY_TO_TYPE.get(entityName), category, biomes);
	}

	default void removeSpawn(@NotNull Class<? extends MobEntity> entityClass, @NotNull MobCategory spawnList, @NotNull Biome @Nullable ... biomes) {
		ModLoader.removeSpawn(entityClass, spawnList, biomes);
	}

	default void registerEntityID(@NotNull Class<? extends Entity> entityClass, @NotNull String entityName) {
		this.registerEntityID(entityClass, entityName, ModLoader.getUniqueEntityId());
	}

	default void registerEntityID(@NotNull Class<? extends Entity> entityClass, @NotNull String entityName, int id) {
		ModLoader.registerEntityID(entityClass, entityName, id);
	}

	@Environment(EnvType.CLIENT)
	interface InGameHook {
		@Environment(EnvType.CLIENT)
		default boolean onTickInGame(@NotNull Minecraft client) {
			return false;
		}
	}

	@Environment(EnvType.CLIENT)
	interface InGuiHook {
		@Environment(EnvType.CLIENT)
		default boolean onTickInGUI(@NotNull Minecraft client, @NotNull Screen screen) {
			return false;
		}
	}
}
