/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraft.src.forge;

import net.minecraft.src.Block;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.*;

// TODO(halotroop2288): Convert this to GSON

/**
 * This class offers advanced configurations capabilities,
 * allowing to provide various categories for configuration variables.
 *
 * @author Space Toad
 * @since 1.0.0
 */
public class Configuration {
	/**
	 * No specific category.
	 *
	 * @since 1.0.1
	 * @deprecated use {@link PropertyKind#GENERAL} instead
	 */
	@Deprecated
	public static final int GENERAL_PROPERTY = PropertyKind.GENERAL.ordinal();
	/**
	 * The Block category.
	 *
	 * @since 1.0.1
	 * @deprecated use {@link PropertyKind#BLOCK} instead
	 */
	@Deprecated
	public static final int BLOCK_PROPERTY = PropertyKind.BLOCK.ordinal();
	/**
	 * The Item category.
	 *
	 * @since 1.0.1
	 * @deprecated use {@link PropertyKind#ITEM} instead
	 */
	@Deprecated
	public static final int ITEM_PROPERTY = PropertyKind.ITEM.ordinal();

	private boolean[] configBlocks = null;

	private final @NotNull File file;

	/**
	 * Properties with no specific category.
	 */
	public TreeMap<String, Property> generalProperties = new TreeMap<>();
	/**
	 * Properties in the Block category.
	 */
	public TreeMap<String, Property> blockProperties = new TreeMap<>();
	/**
	 * Properties in the Item category.
	 */
	public TreeMap<String, Property> itemProperties = new TreeMap<>();

	/**
	 * Creates a configuration for the file given.
	 *
	 * @param file the location to store the configuration
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public Configuration(@NotNull File file) {
		this.file = file;
	}

	/**
	 * Gets or creates a block id property.<br>
	 * If the block id property key is already in the configuration, then it will be used.
	 * Otherwise, {@code defaultID} will be used, except if already taken,
	 * in which case this will try to determine a free default id.
	 *
	 * @param key       the key for which to get or create a property
	 * @param defaultID the id to try to use if the property doesn't already exist
	 * @return the property associated with the given key, or null if the property couldn't be created.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public @Nullable Property getOrCreateBlockIdProperty(@NotNull String key, int defaultID) {
		if (configBlocks == null) {
			configBlocks = new boolean[Block.blocksList.length];
			Arrays.fill(configBlocks, false);
		}

		if (blockProperties.containsKey(key)) {
			Property property = getOrCreateIntProperty(key, PropertyKind.BLOCK, defaultID);
			if (property == null) return null;
			configBlocks[Integer.parseInt(property.value)] = true;
			return property;
		}

		Property property = new Property();

		blockProperties.put(key, property);
		property.name = key;

		if (Block.blocksList[defaultID] == null && !configBlocks[defaultID]) {
			property.value = Integer.toString(defaultID);
			configBlocks[defaultID] = true;
			return property;
		}

		for (int i = Block.blocksList.length - 1; i >= 0; i--) {
			if (Block.blocksList[i] == null && !configBlocks[i]) {
				property.value = Integer.toString(i);
				configBlocks[i] = true;
				return property;
			}
		}

		throw new RuntimeException("No more block ids available for " + key);
	}

	/**
	 * The same as {@link #getOrCreateProperty(String, PropertyKind, String)}
	 * but for {@link Integer#TYPE int} properties rather than {@link String} properties.
	 *
	 * @param key          the key for which to get or create a property
	 * @param kind         the category to look for the property in
	 * @param defaultValue the value to use if the property doesn't already exist
	 * @return the property associated with the given key, or null if the property couldn't be created.
	 * @author Space Toad
	 * @see #getOrCreateIntProperty(String, PropertyKind, int)
	 * @since 1.0.1
	 * @deprecated use {@link #getOrCreateIntProperty(String, PropertyKind, int)} instead
	 */
	public @Nullable Property getOrCreateIntProperty(@NotNull String key, int kind, int defaultValue) {
		return getOrCreateIntProperty(key, PropertyKind.values()[kind], defaultValue);
	}

	/**
	 * The same as {@link #getOrCreateProperty(String, PropertyKind, String)}
	 * but for {@link Integer#TYPE int} properties rather than {@link String} properties.
	 *
	 * @param key          the key for which to get or create a property
	 * @param kind         the category to look for the property in
	 * @param defaultValue the value to use if the property doesn't already exist
	 * @return the property associated with the given key, or null if the property couldn't be created.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public @Nullable Property getOrCreateIntProperty(@NotNull String key, @NotNull PropertyKind kind, int defaultValue) {
		Property property = getOrCreateProperty(key, kind, Integer.toString(defaultValue));
		if (property == null) return null;

		try {
			Integer.parseInt(property.value);

			return property;
		} catch (NumberFormatException e) {
			property.value = Integer.toString(defaultValue);
			return property;
		}
	}

	/**
	 * The same as {@link #getOrCreateProperty(String, PropertyKind, String)}
	 * but for {@link Boolean#TYPE boolean} properties rather than {@link String} properties.
	 *
	 * @param key          the key for which to get or create a property
	 * @param kind         the category to look for the property in
	 * @param defaultValue the value to use if the property doesn't already exist
	 * @return the property associated with the given key, or null if the property couldn't be created.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public @Nullable Property getOrCreateBooleanProperty(@NotNull String key, @NotNull PropertyKind kind, boolean defaultValue) {
		Property property = getOrCreateProperty(key, kind, Boolean.toString(defaultValue));
		if (property == null) return null;

		if (!"true".equalsIgnoreCase(property.value) && !"false".equalsIgnoreCase(property.value)) {
			property.value = Boolean.toString(defaultValue);
		}

		return property;
	}

	/**
	 * Gets or creates a property.<br>
	 * If the property key is already in the configuration, then it will be used.
	 * Otherwise, {@code defaultValue} will be used.
	 *
	 * @param key          the key for which to get or create a property
	 * @param kind         the category to look for the property in
	 * @param defaultValue the value to use if the property doesn't already exist
	 * @return the property associated with the given key, or null if the property couldn't be created.
	 * @author Space Toad
	 * @since 1.0.0
	 * @deprecated use {@link #getOrCreateProperty(String, PropertyKind, String)} instead
	 */
	public @Nullable Property getOrCreateProperty(@NotNull String key, int kind, @Nullable String defaultValue) {
		return getOrCreateProperty(key, PropertyKind.values()[kind], defaultValue);
	}

	/**
	 * Gets or creates a property.<br>
	 * If the property key is already in the configuration, then it will be used.
	 * Otherwise, {@code defaultValue} will be used.
	 *
	 * @param key          the key for which to get or create a property
	 * @param kind         the category to look for the property in
	 * @param defaultValue the value to use if the property doesn't already exist
	 * @return the property associated with the given key, or null if the property couldn't be created.
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public @Nullable Property getOrCreateProperty(@NotNull String key, @Nullable PropertyKind kind, @Nullable String defaultValue) {
		TreeMap<String, Property> source = null;

		if (kind == null) {
			source = generalProperties;
		} else {
			switch (kind) {
				case GENERAL:
					source = generalProperties;
					break;
				case BLOCK:
					source = blockProperties;
					break;
				case ITEM:
					source = itemProperties;
					break;
			}
		}

		if (source.containsKey(key)) {
			return source.get(key);
		} else if (defaultValue != null) {
			Property property = new Property();

			source.put(key, property);
			property.name = key;

			property.value = defaultValue;
			return property;
		} else {
			return null;
		}
	}

	/**
	 * Loads the configuration file from disk.
	 *
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public void load() {
		try {
			if (file.getParentFile() != null && !file.getParentFile().mkdirs()) return;
			if (!file.exists() && !file.createNewFile()) return;
			if (!file.canRead()) return;

			FileInputStream fileInStream = new FileInputStream(file);

			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInStream, StandardCharsets.UTF_8));

			String line;
			TreeMap<String, Property> currentMap = null;

			while (true) {
				line = buffer.readLine();

				if (line == null) break;

				int nameStart = -1, nameEnd = -1;
				boolean skip = false;

				for (int i = 0; i < line.length() && !skip; ++i) {
					if (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '.') {
						if (nameStart == -1) {
							nameStart = i;
						}

						nameEnd = i;
						continue;
					}
					// ignore space characters
					if (Character.isWhitespace(line.charAt(i))) continue;
					switch (line.charAt(i)) {
						case '#':
							skip = true;
							continue;
						case '{':
							String scopeName = line.substring(nameStart, nameEnd + 1);

							switch (scopeName) {
								case "general":
									currentMap = generalProperties;
									break;
								case "block":
									currentMap = blockProperties;
									break;
								case "item":
									currentMap = itemProperties;
									break;
								default:
									throw new RuntimeException("unknown section " + scopeName);
							}

							break;
						case '}':
							currentMap = null;
							break;
						case '=':
							String propertyName = line.substring(nameStart, nameEnd + 1);

							if (currentMap == null) {
								throw new RuntimeException("property " + propertyName + " has no scope");
							}

							Property prop = new Property();
							prop.name = propertyName;
							prop.value = line.substring(i + 1);
							i = line.length();

							currentMap.put(propertyName, prop);

							break;
						default:
							throw new RuntimeException("unknown character " + line.charAt(i));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the configuration file to disk.
	 *
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public void save() {
		try {
			assert file.getParentFile() == null || file.getParentFile().mkdirs() : "Could not create configuration directory.";

			if (!file.exists() && !file.createNewFile()) return;

			if (file.canWrite()) {
				FileOutputStream fileoutputstream = new FileOutputStream(file);

				BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8));

				buffer.write("# Configuration file\n");
				buffer.write("# Generated on " + DateFormat.getInstance().format(new Date()) + "\n");
				buffer.write("\n");
				buffer.write("###########\n");
				buffer.write("# General #\n");
				buffer.write("###########\n\n");

				buffer.write("general {\n");
				writeProperties(buffer, generalProperties.values());
				buffer.write("}\n\n");

				buffer.write("#########\n");
				buffer.write("# Block #\n");
				buffer.write("#########\n\n");

				buffer.write("block {\n");
				writeProperties(buffer, blockProperties.values());
				buffer.write("}\n\n");

				buffer.write("########\n");
				buffer.write("# Item #\n");
				buffer.write("########\n\n");

				buffer.write("item {\n");
				writeProperties(buffer, itemProperties.values());
				buffer.write("}\n\n");

				buffer.close();
				fileoutputstream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeProperties(BufferedWriter buffer, Collection<Property> props) throws IOException {
		for (Property property : props) {
			if (property.comment != null) {
				buffer.write("   # " + property.comment + "\n");
			}

			buffer.write("   " + property.name + "=" + property.value);
			buffer.write("\n");
		}
	}

	/**
	 * Holds data for each configuration entry.
	 *
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static class Property {
		/**
		 * The name of the configuration property.
		 * @since 1.0.0
		 */
		public String name;
		/**
		 * The value associated with the configuration property.
		 * @since 1.0.0
		 */
		public String value;
		/**
		 * The comment that describes the configuration property to the user.
		 * @since 1.0.0
		 */
		public String comment;

		/**
		 * Default constructor.
		 * @since 1.0.0
		 */
		public Property() {
		}
	}

	/**
	 * Valid types of configuration properties.
	 *
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public enum PropertyKind {
		/**
		 * No specific category.
		 */
		GENERAL,
		/**
		 * Property in the Block category.
		 */
		BLOCK,
		/**
		 * Property in the Item category.
		 */
		ITEM,
	}
}
