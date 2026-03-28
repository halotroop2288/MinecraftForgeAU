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
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.crafting.CraftingManager;
import net.minecraft.crafting.SmeltingManager;
import net.minecraft.crafting.recipe.Recipe;
import net.minecraft.entity.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.locale.Language;
import net.minecraft.stat.ItemStat;
import net.minecraft.stat.Stats;
import net.minecraft.stat.achievement.AchievementStat;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue"})
public final class ModLoader {
	private static final List<DynamicTexture> animList = new LinkedList<>();
	private static final Map<Integer, Mod> blockModels = new HashMap<>();
	private static final Map<Integer, Boolean> blockSpecialInv = new HashMap<>();
	private static long time = 0L;
	private static boolean hasInit = false;
	private static int highestEntityId = 3000;
	private static final Minecraft instance = Minecraft.INSTANCE;
	private static int itemSpriteIndex = 0;
	private static int itemSpritesLeft = 0;
	private static final Map<Mod, Map<KeyBinding, boolean[]>> keyList = new HashMap<>();
	private static int nextBlockModelID = 1000;
	private static final Map<Integer, Map<String, Integer>> overrides = new HashMap<>();
	public static @NotNull Biome[] vanillaBiomes;
	private static int terrainSpriteIndex = 0;
	private static int terrainSpritesLeft = 0;
	private static String texPack = null;
	private static boolean texturesAdded = false;
	private static final boolean[] usedItemSprites = new boolean[256];
	private static final boolean[] usedTerrainSprites = new boolean[256];

	public static void addAchievementDesc(@NotNull AchievementStat achievement, @NotNull String name, @NotNull String description) {
		if (achievement.key.contains(".")) {
			String[] split = achievement.key.split("\\.");
			if (split.length == 2) {
				String key = split[1];
				addLocalization("achievement." + key, name);
				addLocalization("achievement." + key + ".desc", description);
				achievement.key = Language.getInstance().translate("achievement." + key);
				achievement.description = Language.getInstance().translate("achievement." + key + ".desc");
			} else {
				achievement.key = name;
				achievement.description = description;
			}
		} else {
			achievement.key = name;
			achievement.description = description;
		}
	}

	/**
	 * @return the average energy value registered by every mod.
	 */
	public static int addAllFuel(@NotNull ItemStack fuel) {
		int sum = 0;
		int count = 0;
		for (Mod mod : getLoadedMods()) {
			int i = mod.addFuel(fuel);
			if (i <= 0) continue;
			sum += i;
			count++;
		}

		if (count <= 0) return 0;
		return sum / count;
	}

	public static void addAllRenderers(Map<Class<? extends Entity>, EntityRenderer> renderers) {
		getLoadedMods().forEach(mod -> mod.addRenderer(renderers));
	}

	@Environment(EnvType.CLIENT)
	public static void addAnimation(@NotNull DynamicTexture animation) {
		if (animList.stream().anyMatch(oldAnim -> oldAnim.atlas == animation.atlas && oldAnim.sprite == animation.sprite))
			animList.remove(animation);

		animList.add(animation);
	}

	@Environment(EnvType.CLIENT)
	public static int addArmor(String armorType) {
		List<String> existingArmorList = Arrays.asList(PlayerRenderer.ARMOR_VARIANTS);
		existingArmorList.add(armorType);
		PlayerRenderer.ARMOR_VARIANTS = existingArmorList.toArray(new String[0]);
		return existingArmorList.indexOf(armorType);
	}

	public static void addLocalization(@NotNull String key, @NotNull String value) {
		Language.getInstance().translations.put(key, value);
	}

	/**
	 * @deprecated use {@link #addBlockName(Block, String)},
	 * {@link #addItemName(Item, String)},
	 * or {@link #addStackName(ItemStack, String)} instead.
	 */
	@Deprecated
	public static void addName(Object object, String name) {
		if (object instanceof Block) addBlockName((Block) object, name);
		else if (object instanceof Item) addItemName((Item) object, name);
		else if (object instanceof ItemStack) addStackName((ItemStack) object, name);
	}

	public static void addBlockName(@NotNull Block block, @NotNull String name) {
		if (block.getTranslationKey() == null) return;
		ModLoader.addLocalization(block.getTranslationKey() + ".name", name);
	}

	public static void addItemName(@NotNull Item item, @NotNull String name) {
		if (item.getTranslationKey() == null) return;
		ModLoader.addLocalization(item.getTranslationKey() + ".name", name);
	}

	public static void addStackName(@NotNull ItemStack stack, @NotNull String name) {
		if (stack.getTranslationKey() == null) return;
		ModLoader.addLocalization(stack.getTranslationKey() + ".name", name);
	}

	public static void addOverride(@NotNull String path, @NotNull String overlayPath, int index) {
		int type;
		switch (path) {
			case "/terrain.png":
				type = 0;
				break;
			case "/gui/items.png":
				type = 1;
				break;
			default:
				return;
		}

		Map<String, Integer> overlays = overrides.computeIfAbsent(type, k -> new HashMap<>());

		overlays.put(overlayPath, index);
	}

	public static void addShapedRecipe(@NotNull ItemStack output, @NotNull Object @NotNull ... params) {
		CraftingManager.getInstance().registerShaped(output, params);
	}

	public static void addShapelessRecipe(@NotNull ItemStack output, @NotNull Object @NotNull ... params) {
		CraftingManager.getInstance().registerShapeless(output, params);
	}

	public static void addSmeltingRecipe(int input, @NotNull ItemStack output) {
		SmeltingManager.getInstance().register(input, output);
	}

	@SuppressWarnings("unchecked")
	public static void addSpawn(@NotNull Class<? extends MobEntity> entityClass, int weightedProb, @NotNull MobCategory spawnList, @NotNull Biome... biomes) {
		if (biomes == null) biomes = vanillaBiomes;

		for (Biome biome : biomes) {
			List<Biome.SpawnEntry> list = biome.getSpawnEntries(spawnList);
			if (list != null) {
				boolean exists = false;

				for (Biome.SpawnEntry entry : list) {
					if (entry.type == entityClass) {
						entry.weight = weightedProb;
						exists = true;
						break;
					}
				}

				if (!exists) {
					list.add(new Biome.SpawnEntry(entityClass, weightedProb));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void removeSpawn(
		@NotNull Class<? extends MobEntity> entityType,
		@NotNull MobCategory category,
		@NotNull Biome @Nullable ... biomes
	) {
		if (biomes == null) biomes = vanillaBiomes;

		Arrays.stream(biomes)
			.map(biome -> biome.getSpawnEntries(category))
			.filter(Objects::nonNull)
			.forEach(list -> list.removeIf(entry -> ((Biome.SpawnEntry) entry).type == entityType));
	}

	/**
	 * <p>Runs the first possible dispense action provided by loaded mods.</p>
	 * <b>Subsequent actions will not be run.</b>
	 *
	 * @return whether an entity has been spawned
	 */
	public static boolean dispenseEntity(@NotNull World world,
										 double entityX, double entityY, double entityZ,
										 int xVelocity, int zVelocity,
										 @NotNull ItemStack stack) {
		return getLoadedMods().stream().anyMatch(mod -> mod.dispenseEntity(world, entityX, entityY, entityZ, xVelocity, zVelocity, stack));
	}

	public static @NotNull List<Mod> getLoadedMods() {
		return FabricLoader.getInstance().getEntrypoints("modloader:mod", Mod.class);
	}

	public static @NotNull List<Mod.InGameHook> getInGameHooks() {
		return FabricLoader.getInstance().getEntrypoints("modloader:in_game", Mod.InGameHook.class);
	}

	public static @NotNull List<Mod.InGuiHook> getInGUIHooks() {
		return FabricLoader.getInstance().getEntrypoints("modloader:in_gui", Mod.InGuiHook.class);
	}

	@Environment(EnvType.CLIENT)
	public static @NotNull Minecraft getMinecraftInstance() {
		return Objects.requireNonNull(Minecraft.INSTANCE, "Called Minecraft client too early.");
	}

	public static int getUniqueBlockModelID(@NotNull Mod mod, boolean full3DItem) {
		int id = nextBlockModelID++;
		blockModels.put(id, mod);
		blockSpecialInv.put(id, full3DItem);
		return id;
	}

	public static int getUniqueEntityId() {
		return highestEntityId++;
	}

	private static int getUniqueItemSpriteIndex() {
		while (itemSpriteIndex < usedItemSprites.length) {
			if (!usedItemSprites[itemSpriteIndex]) {
				usedItemSprites[itemSpriteIndex] = true;
				itemSpritesLeft--;
				return itemSpriteIndex++;
			}

			itemSpriteIndex++;
		}

		throw new RuntimeException("No more empty item sprite indices left!");
	}

	public static int getUniqueSpriteIndex(@NotNull String path) {
		if (path.equals("/gui/items.png")) return getUniqueItemSpriteIndex();
		if (path.equals("/terrain.png")) return getUniqueTerrainSpriteIndex();
		throw new RuntimeException("No registry for this texture: " + path);
	}

	private static int getUniqueTerrainSpriteIndex() {
		while (terrainSpriteIndex < usedTerrainSprites.length) {
			if (!usedTerrainSprites[terrainSpriteIndex]) {
				usedTerrainSprites[terrainSpriteIndex] = true;
				terrainSpritesLeft--;
				return terrainSpriteIndex++;
			}

			terrainSpriteIndex++;
		}

		throw new RuntimeException("No more empty terrain sprite indices left!");
	}

	private static void init() {
		hasInit = true;
		String usedItemSpritesString = "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111111111011111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
		String usedTerrainSpritesString = "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111111110111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";

		for (int i = 0; i < 256; i++) {
			usedItemSprites[i] = usedItemSpritesString.charAt(i) == '1';
			if (!usedItemSprites[i]) itemSpritesLeft++;

			usedTerrainSprites[i] = usedTerrainSpritesString.charAt(i) == '1';
			if (!usedTerrainSprites[i]) terrainSpritesLeft++;
		}

		vanillaBiomes = Arrays.copyOfRange(Biome.BIOME_MAP, 0, 12);

		try {
			getLoadedMods().forEach(Mod::modsLoaded);

			instance.options.keyBindings = registerAllKeys(instance.options.keyBindings);
			instance.options.load();
			initStats();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	@SuppressWarnings("unchecked")
	private static void initStats() {
		for (int id = 0; id < Block.BY_ID.length; id++) {
			if (!Stats.BY_ID.containsKey(0x1000000 + id) && Block.BY_ID[id] != null && Block.BY_ID[id].hasStats()) {
				String str = Language.getInstance().translate("stat.mineBlock", Block.BY_ID[id].getName());
				Stats.BLOCKS_MINED[id] = new ItemStat(0x1000000 + id, str, id).register();
				Stats.MINED.add(Stats.BLOCKS_MINED[id]);
			}
		}

		for (int idx = 0; idx < Item.BY_ID.length; idx++) {
			if (!Stats.BY_ID.containsKey(0x1020000 + idx) && Item.BY_ID[idx] != null) {
				String str = Language.getInstance().translate("stat.useItem", Item.BY_ID[idx].getDisplayName());
				Stats.ITEMS_USED[idx] = new ItemStat(16908288 + idx, str, idx).register();
				if (idx >= Block.BY_ID.length) {
					Stats.USED.add(Stats.ITEMS_USED[idx]);
				}
			}

			if (!Stats.BY_ID.containsKey(0x1030000 + idx) && Item.BY_ID[idx] != null && Item.BY_ID[idx].isDamageable()) {
				String str = Language.getInstance().translate("stat.breakItem", Item.BY_ID[idx].getDisplayName());
				Stats.ITEMS_BROKEN[idx] = new ItemStat(16973824 + idx, str, idx).register();
			}
		}

		HashSet<Integer> idHashSet = new HashSet<>();

		for (Object result : CraftingManager.getInstance().getRecipes()) {
			idHashSet.add(((Recipe) result).getResult().id);
		}

		for (Object result : SmeltingManager.getInstance().getRecipes().values()) {
			idHashSet.add(((ItemStack) result).id);
		}

		for (int idx : idHashSet) {
			if (!Stats.BY_ID.containsKey(0x1010000 + idx) && Item.BY_ID[idx] != null) {
				String str = Language.getInstance().translate("stat.craftItem", Item.BY_ID[idx].getDisplayName());
				Stats.ITEMS_CRAFTED[idx] = new ItemStat(16842752 + idx, str, idx).register();
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static boolean isScreenOpen(@Nullable Class<? extends Screen> screen) {
		return screen == null ? Minecraft.INSTANCE.screen == null : screen.isInstance(Minecraft.INSTANCE.screen);
	}

	/**
	 * @see FabricLoader#isModLoaded(String)
	 */
	public static boolean isModLoaded(String modID) {
		return FabricLoader.getInstance().isModLoaded(modID);
	}

	public static BufferedImage loadImage(TextureManager texCache, String path) throws IOException {
		return Objects.requireNonNull(
			ImageIO.read(Objects.requireNonNull(
				texCache.texturePacks.selected.getResource(path),
				"Image not found: " + path)
			),
			"Image corrupted: " + path
		);
	}

	/**
	 * Invokes all listeners of {@link Mod#onItemPickup(PlayerEntity, ItemStack)}
	 *
	 * @param player who is picking up
	 * @param item   what is being picked up
	 */
	public static void onItemPickup(PlayerEntity player, ItemStack item) {
		getLoadedMods().forEach(mod -> mod.onItemPickup(player, item));
	}

	@Environment(EnvType.CLIENT)
	public static void onClientTick(Minecraft client) {
		if (!hasInit) init();

		if (!client.options.skin.equals(texPack)) {
			texturesAdded = false;
			texPack = client.options.skin;
		}

		if (!texturesAdded && client.textureManager != null) {
			registerAllTextureOverrides(client.textureManager);
			texturesAdded = true;
		}

		long newTime = 0L;
		if (client.world != null) {
			newTime = client.world.getTime();
			for (Mod.InGameHook mod : getInGameHooks()) {
				if (!mod.onTickInGame(client)) break;
			}
		}

		if (client.screen != null) {
			for (Mod.InGuiHook mod : getInGUIHooks()) {
				if (client.world == null) break;
				if (!mod.onTickInGUI(client, Minecraft.INSTANCE.screen)) break;
			}
		}

		if (time != newTime) {
			keyList.forEach((key, value) -> value.forEach((key1, keyInfo) -> {
				boolean state = Keyboard.isKeyDown(key1.keyCode);
				boolean oldState = keyInfo[1];
				keyInfo[1] = state;
				if (state && (!oldState || keyInfo[0])) {
					key.keyboardEvent(key1);
				}
			}));
		}

		time = newTime;
	}

	@Deprecated
	@Environment(EnvType.CLIENT)
	public static void openGUI(PlayerEntity player, Screen gui) {
		if (!hasInit) init();

		if (Minecraft.INSTANCE.player != player || gui == null) return;
		Minecraft.INSTANCE.openScreen(gui);
	}

	public static void populateChunk(ChunkSource generator, int chunkX, int chunkZ, World world) {
		if (!hasInit) init();

		Random rnd = new Random(world.getSeed());
		long xSeed = rnd.nextLong() / 2L * 2L + 1L;
		long zSeed = rnd.nextLong() / 2L * 2L + 1L;
		rnd.setSeed(chunkX * xSeed + chunkZ * zSeed ^ world.getSeed());

		for (Mod mod : getLoadedMods()) {
			if (generator.getDebugInfo().equals("RandomLevelSource")) {
				mod.generateSurface(world, rnd, chunkX << 4, chunkZ << 4);
			} else if (generator.getDebugInfo().equals("HellRandomLevelSource")) {
				mod.generateNether(world, rnd, chunkX << 4, chunkZ << 4);
			}
		}
	}

	public static KeyBinding[] registerAllKeys(KeyBinding[] bindings) {
		List<KeyBinding> combinedList = new LinkedList<>(Arrays.asList(bindings));

		keyList.values().stream().map(Map::keySet).forEach(combinedList::addAll);

		return combinedList.toArray(new KeyBinding[0]);
	}

	public static void registerAllTextureOverrides(TextureManager texCache) {
		animList.clear();

		getLoadedMods().forEach(it -> it.registerAnimation(Minecraft.INSTANCE));

		for (DynamicTexture anim : animList) {
			texCache.addDynamicTexture(anim);
		}

		for (Entry<Integer, Map<String, Integer>> overlay : overrides.entrySet()) {
			for (Entry<String, Integer> overlayEntry : overlay.getValue().entrySet()) {
				String overlayPath = overlayEntry.getKey();
				int index = overlayEntry.getValue();
				int dst = overlay.getKey();

				try {
					BufferedImage im = loadImage(texCache, overlayPath);
					DynamicTexture anim = new ModTextureStatic(index, dst, im);
					texCache.addDynamicTexture(anim);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static @Nullable BlockItem registerBlock(@NotNull Block block, @Nullable Function<Integer, ? extends BlockItem> itemConstructor) {
		Session.CREATIVE_INVENTORY.add(block);
		int id = block.id;
		BlockItem item = itemConstructor != null ? itemConstructor.apply(id - 256) : new BlockItem(id - 256);

		if (Block.BY_ID[id] != null && Item.BY_ID[id] == null) {
			Item.BY_ID[id] = item;
		}

		return item;
	}

	public static void registerEntityID(@NotNull Class<? extends Entity> entityClass, @NotNull String entityName, int id) {
		Entities.register(entityClass, entityName, id);
	}

	public static void registerKey(Mod mod, KeyBinding keyHandler, boolean allowRepeat) {
		Map<KeyBinding, boolean[]> keyMap = keyList.get(mod);
		if (keyMap == null) keyMap = new HashMap<>();

		keyMap.put(keyHandler, new boolean[]{allowRepeat, false});
		keyList.put(mod, keyMap);
	}

	@SuppressWarnings("unchecked")
	public static void registerTileEntity(
		@NotNull Class<? extends BlockEntity> blockEntityClass,
		@NotNull String id,
		@Nullable BlockEntityRenderer renderer
	) {
		BlockEntity.register(blockEntityClass, id);
		if (renderer != null) {
			BlockEntityRenderDispatcher.INSTANCE.renderers.put(blockEntityClass, renderer);
			renderer.init(BlockEntityRenderDispatcher.INSTANCE);
		}
	}

	public static boolean renderBlockIsItemFull3D(int renderType) {
		return !blockSpecialInv.containsKey(renderType) ? renderType == 16 : blockSpecialInv.get(renderType);
	}

	public static void renderInvBlock(BlockRenderer renderer, Block block, int metadata, int modelID) {
		Mod mod = blockModels.get(modelID);
		if (mod != null) mod.renderInvBlock(renderer, block, metadata, modelID);
	}

	public static boolean renderWorldBlock(
		BlockRenderer renderer,
		WorldView world,
		int x, int y, int z,
		Block block, int modelID
	) {
		Mod mod = blockModels.get(modelID);
		return mod != null && mod.renderWorldBlock(renderer, world, x, y, z, block, modelID);
	}

	public static void takenFromCrafting(@NotNull PlayerEntity player, @NotNull ItemStack item) {
		getLoadedMods().forEach(mod -> mod.takenFromCrafting(player, item));
	}

	public static void takenFromFurnace(@NotNull PlayerEntity player, @NotNull ItemStack item) {
		getLoadedMods().forEach(mod -> mod.takenFromFurnace(player, item));
	}

	private ModLoader() {
	}
}
