// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core;

import com.twentyfeel.laf.core.util.PlatformInfo;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

/**
 * Abstract base class for the Zizi Look and Feel.
 * This class provides common functionality for customizing the UI defaults and properties.
 */
public abstract class ZiziLaf extends BasicLookAndFeel {
	private BasicLookAndFeel baseLookAndFeel;

	private static final String VARIABLE_PREFIX = "@";
	private static final String REF_PREFIX = VARIABLE_PREFIX + "@";
	private static final String GLOBAL_PREFIX = "*.";

	@Override
	public String getID() {
		return getName();
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return true;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}

	@Override
	public void initialize() {
		getBaseLookAndFeel().initialize();
		super.initialize();
	}

	@Override
	public void uninitialize() {
		if (baseLookAndFeel != null) {
			baseLookAndFeel.uninitialize();
		}
		super.uninitialize();
	}

	/**
	 * Returns the base Look and Feel.
	 * Initializes the base Look and Feel if it is not already initialized.
	 *
	 * @return the base Look and Feel
	 */
	private BasicLookAndFeel getBaseLookAndFeel() {
		if (baseLookAndFeel == null) {
			baseLookAndFeel = new MetalLookAndFeel();
		}
		return baseLookAndFeel;
	}

	@Override
	public UIDefaults getDefaults() {
		UIDefaults defaults = getBaseLookAndFeel().getDefaults();
		Color controlColor = defaults.getColor("control");

		defaults.put("EditorPane.disabledBackground", controlColor);
		defaults.put("EditorPane.inactiveBackground", controlColor);
		defaults.put("FormattedTextField.disabledBackground", controlColor);
		defaults.put("PasswordField.disabledBackground", controlColor);
		defaults.put("TextArea.disabledBackground", controlColor);
		defaults.put("TextArea.inactiveBackground", controlColor);
		defaults.put("TextField.disabledBackground", controlColor);
		defaults.put("TextPane.disabledBackground", controlColor);
		defaults.put("TextPane.inactiveBackground", controlColor);

		defaults.put("Spinner.disabledBackground", controlColor);
		defaults.put("Spinner.disabledForeground", controlColor);

		setFontDefaults(defaults);
		loadPropertiesToDefaults(defaults);

		return defaults;
	}

	/**
	 * Sets default fonts for various components.
	 *
	 * @param defaults the UIDefaults to update
	 */
	private void setFontDefaults(UIDefaults defaults) {
		FontUIResource uiFont = null;

		if (PlatformInfo.IS_WINDOWS) {
			Font windowsFont = (Font) Toolkit.getDefaultToolkit().getDesktopProperty("win.messagebox.font");
			if (windowsFont != null) {
				uiFont = new FontUIResource(windowsFont);
			}
		}

		if (uiFont != null) {
			for (Object key : defaults.keySet()) {
				if (key instanceof String && ((String) key).endsWith(".font")) {
					defaults.put(key, uiFont);
				}
			}
		}
	}

	/**
	 * Loads properties from class-specific .properties files and adds them to the UIDefaults.
	 *
	 * @param defaults the UIDefaults to update
	 */
	private void loadPropertiesToDefaults(UIDefaults defaults) {
		List<Class<?>> lafClasses = getClassHierarchy();

		try {
			Properties properties = loadPropertiesFromClassHierarchy(lafClasses);
			Map<String, Object> globalDefaults = extractGlobalDefaults(properties);

			applyGlobalDefaults(defaults, globalDefaults);
			applyNonGlobalProperties(defaults, properties);

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Gets the class hierarchy for the current Look and Feel class.
	 *
	 * @return a list of classes in the hierarchy
	 */
	private List<Class<?>> getClassHierarchy() {
		List<Class<?>> lafClasses = new LinkedList<>();
		for (Class<?> lafClass = getClass(); ZiziLaf.class.isAssignableFrom(lafClass); lafClass = lafClass.getSuperclass()) {
			lafClasses.addFirst(lafClass);
		}
		return lafClasses;
	}

	/**
	 * Loads properties from the class-specific .properties files.
	 *
	 * @param lafClasses the list of classes in the hierarchy
	 * @return the loaded properties
	 * @throws IOException if an I/O error occurs
	 */
	private Properties loadPropertiesFromClassHierarchy(List<Class<?>> lafClasses) throws IOException {
		Properties properties = new Properties();
		for (Class<?> lafClass : lafClasses) {
			String propertiesFileName = "/" + lafClass.getName().replace('.', '/') + ".properties";
			try (InputStream inputStream = lafClass.getResourceAsStream(propertiesFileName)) {
				if (inputStream != null) {
					properties.load(inputStream);
				}
			}
		}
		return properties;
	}

	/**
	 * Extracts global defaults from the properties.
	 *
	 * @param properties the properties to extract from
	 * @return a map of global defaults
	 */
	private Map<String, Object> extractGlobalDefaults(Properties properties) {
		Map<String, Object> globals = new HashMap<>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			if (key.startsWith(GLOBAL_PREFIX)) {
				String value = resolveVariable(properties, (String) entry.getValue());
				globals.put(key.substring(GLOBAL_PREFIX.length()), parseValue(key, value));
			}
		}
		return globals;
	}

	/**
	 * Applies global defaults to the UIDefaults.
	 *
	 * @param defaults       the UIDefaults to update
	 * @param globalDefaults the global defaults to apply
	 */
	private void applyGlobalDefaults(UIDefaults defaults, Map<String, Object> globalDefaults) {
		for (Object key : defaults.keySet()) {
			if (key instanceof String strKey && strKey.contains(".")) {
				String globalKey = strKey.substring(strKey.lastIndexOf('.') + 1);
				Object globalValue = globalDefaults.get(globalKey);
				if (globalValue != null) {
					defaults.put(key, globalValue);
				}
			}
		}
	}

	/**
	 * Applies non-global properties to the UIDefaults.
	 *
	 * @param defaults   the UIDefaults to update
	 * @param properties the properties to apply
	 */
	private void applyNonGlobalProperties(UIDefaults defaults, Properties properties) {
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			if (!key.startsWith(VARIABLE_PREFIX) && !key.startsWith(GLOBAL_PREFIX)) {
				String value = resolveVariable(properties, (String) entry.getValue());
				defaults.put(key, parseValue(key, value));
			}
		}
	}

	/**
	 * Resolves a variable from the properties.
	 *
	 * @param properties the properties to resolve from
	 * @param value      the value to resolve
	 * @return the resolved value
	 */
	private String resolveVariable(Properties properties, String value) {
		if (!value.startsWith(VARIABLE_PREFIX)) {
			return value;
		}

		if (value.startsWith(REF_PREFIX)) {
			value = value.substring(REF_PREFIX.length());
		}

		String resolvedValue = properties.getProperty(value);
		if (resolvedValue == null) {
			System.err.println("Variable or reference '" + value + "' not found");
		}

		return resolvedValue;
	}

	/**
	 * Parses a value from a string.
	 *
	 * @param key   the key associated with the value
	 * @param value the string value to parse
	 * @return the parsed value
	 */
	private Object parseValue(String key, String value) {
		value = value.trim();

		switch (value) {
			case "null":
				return null;
			case "false":
				return false;
			case "true":
				return true;
		}

		if (key.endsWith(".border")) {
			return parseInstance(value);
		}

		if (key.endsWith(".icon")) {
			return parseInstance(value);
		}

		if (key.endsWith(".margin") || key.endsWith(".padding") || key.endsWith("Insets")) {
			return parseInsets(value);
		}

		if (key.endsWith("Size") && !key.equals("SplitPane.dividerSize")) {
			return parseSize(value);
		}

		if (key.endsWith("Width") || key.endsWith("Height")) {
			return parseInteger(value, true);
		}

		ColorUIResource color = parseColor(value);
		if (color != null) {
			return color;
		}

		Integer integer = parseInteger(value, false);
		if (integer != null) {
			return integer;
		}

		return value;
	}

	/**
	 * Parses an instance from a string value.
	 *
	 * @param value the string value to parse
	 * @return a LazyValue that instantiates the instance
	 */
	private Object parseInstance(String value) {
		return (UIDefaults.LazyValue) t -> {
			try {
				return Class.forName(value).getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
				System.err.println(ex.getMessage());
				return null;
			} catch (InvocationTargetException | NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Parses insets from a string value.
	 *
	 * @param value the string value to parse
	 * @return the parsed insets
	 */
	private Insets parseInsets(String value) {
		List<String> numbers = split(value);
		try {
			return new InsetsUIResource(Integer.parseInt(numbers.get(0)), Integer.parseInt(numbers.get(1)), Integer.parseInt(numbers.get(2)), Integer.parseInt(numbers.get(3)));
		} catch (NumberFormatException ex) {
			System.err.println("Invalid insets: " + value);
			throw ex;
		}
	}

	/**
	 * Parses a size from a string value.
	 *
	 * @param value the string value to parse
	 * @return the parsed size
	 */
	private Dimension parseSize(String value) {
		List<String> numbers = split(value);
		try {
			return new DimensionUIResource(Integer.parseInt(numbers.get(0)), Integer.parseInt(numbers.get(1)));
		} catch (NumberFormatException ex) {
			System.err.println("Invalid size: " + value);
			throw ex;
		}
	}

	/**
	 * Parses a color from a string value.
	 *
	 * @param value the string value to parse
	 * @return the parsed color, or null if the value is not a valid color
	 */
	private ColorUIResource parseColor(String value) {
		try {
			if (value.length() == 6) {
				int rgb = Integer.parseInt(value, 16);
				return new ColorUIResource(rgb);
			}
			if (value.length() == 8) {
				int rgba = Integer.parseInt(value, 16);
				return new ColorUIResource(new Color(rgba, true));
			}
		} catch (NumberFormatException ex) {
			// not a color --> ignore
		}
		return null;
	}

	/**
	 * Parses an integer from a string value.
	 *
	 * @param value       the string value to parse
	 * @param reportError whether to report an error if the value is not a valid integer
	 * @return the parsed integer, or null if the value is not a valid integer
	 */
	private Integer parseInteger(String value, boolean reportError) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			if (reportError) {
				System.err.println("Invalid integer: " + value);
				throw ex;
			}
		}
		return null;
	}

	/**
	 * Splits a string by a delimiter.
	 *
	 * @param str the string to split
	 * @return a list of split strings
	 */
	private static List<String> split(String str) {
		List<String> result = new ArrayList<>();
		int delimIndex = str.indexOf(',');
		int index = 0;
		while (delimIndex >= 0) {
			result.add(str.substring(index, delimIndex));
			index = delimIndex + 1;
			delimIndex = str.indexOf(',', index);
		}
		result.add(str.substring(index));
		return result;
	}
}
