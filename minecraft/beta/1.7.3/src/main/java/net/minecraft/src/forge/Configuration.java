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
 * @author Space Toad
 * @since 1.0.0
 */
public class Configuration {
	private boolean[] configBlocks = null;

	private final @NotNull File file;

	public TreeMap<String, Property> blockProperties = new TreeMap<>();
	public TreeMap<String, Property> itemProperties = new TreeMap<>();
	public TreeMap<String, Property> generalProperties = new TreeMap<>();

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public Configuration(@NotNull File file) {
		this.file = file;
	}

	/**
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
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public @Nullable Property getOrCreateProperty(@NotNull String key, @NotNull PropertyKind kind, @Nullable String defaultValue) {
		TreeMap<String, Property> source = null;

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

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
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
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public static class Property {
		public String name;
		public String value;
		public String comment;
	}

	/**
	 * @author Space Toad
	 * @since 1.0.0
	 */
	public enum PropertyKind {
		GENERAL, BLOCK, ITEM
	}
}
