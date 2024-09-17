// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.util;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

/**
 * Provides scaling for HiDPI displays.
 * Two modes are supported:
 * System Scaling Mode: Available from Java 9, handles per-monitor scaling.
 * User Scaling Mode: For Java 8 compatibility, calculates scaling based on font size.
 */
public class UIScale {

	private static final boolean DEBUG_MODE = false;

	private static Boolean isJreHiDPIEnabled;

	private static boolean checkJreHiDPI() {
		if (isJreHiDPIEnabled != null) return isJreHiDPIEnabled;

		isJreHiDPIEnabled = false;

		if (PlatformInfo.IS_JAVA_9_OR_LATER) {
			isJreHiDPIEnabled = true;
		} else if (PlatformInfo.IS_JETBRAINS_JVM) {
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				Class<?> sunGeClass = Class.forName("sun.java2d.SunGraphicsEnvironment");
				if (sunGeClass.isInstance(ge)) {
					Method method = sunGeClass.getDeclaredMethod("isUIScaleOn");
					isJreHiDPIEnabled = (Boolean) method.invoke(ge);
				}
			} catch (Throwable ex) {
				System.err.println(ex.getMessage());
			}
		}

		return isJreHiDPIEnabled;
	}

	private static float userScaleFactor = 1f;

	static {
		if (isScalingEnabled()) {
			PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					String propertyName = evt.getPropertyName();
					if ("lookAndFeel".equals(propertyName)) {
						if (evt.getNewValue() instanceof LookAndFeel)
							UIManager.getLookAndFeelDefaults().addPropertyChangeListener(this);
						updateUserScaleFactor();
					} else if ("Label.font".equals(propertyName)) {
						updateUserScaleFactor();
					}
				}
			};
			UIManager.addPropertyChangeListener(propertyChangeListener);
			UIManager.getLookAndFeelDefaults().addPropertyChangeListener(propertyChangeListener);

			updateUserScaleFactor();
		}
	}

	private static void updateUserScaleFactor() {
		if (!isScalingEnabled()) return;

		Font labelFont = UIManager.getFont("Label.font");
		float defaultFontSize = 12f;

		if (PlatformInfo.IS_WINDOWS) {
			if ("Tahoma".equals(labelFont.getFamily())) defaultFontSize = 11f;
		} else if (PlatformInfo.IS_LINUX) {
			defaultFontSize = 15f;
		}

		setUserScaleFactor(labelFont.getSize() / defaultFontSize);
	}

	private static boolean isScalingEnabled() {
		if (checkJreHiDPI()) return false;

		String hidpi = System.getProperty("hidpi");
		return hidpi == null || Boolean.parseBoolean(hidpi);
	}

	public static float getUserScaleFactor() {
		return userScaleFactor;
	}

	private static void setUserScaleFactor(float scaleFactor) {
		if (scaleFactor <= 1f) scaleFactor = 1f;
		else scaleFactor = Math.round(scaleFactor * 4f) / 4f;

		userScaleFactor = scaleFactor;

		if (DEBUG_MODE) System.out.println("HiDPI scale factor " + scaleFactor);
	}

	public static float scale(float value) {
		return (userScaleFactor == 1) ? value : (value * userScaleFactor);
	}

	public static int scale(int value) {
		return (userScaleFactor == 1) ? value : round(value * userScaleFactor);
	}

	public static float unscale(float value) {
		return (userScaleFactor == 1f) ? value : (value / userScaleFactor);
	}

	public static int unscale(int value) {
		return (userScaleFactor == 1f) ? value : round(value / userScaleFactor);
	}

	public static void scaleGraphics(Graphics2D g) {
		if (userScaleFactor != 1f) g.scale(userScaleFactor, userScaleFactor);
	}

	public static Dimension scale(Dimension dimension) {
		if (dimension == null || userScaleFactor == 1f) return dimension;
		if (dimension instanceof UIResource)
			return new DimensionUIResource(scale(dimension.width), scale(dimension.height));
		return new Dimension(scale(dimension.width), scale(dimension.height));
	}

	public static Insets scale(Insets insets) {
		if (insets == null || userScaleFactor == 1f) return insets;
		if (insets instanceof UIResource)
			return new InsetsUIResource(scale(insets.top), scale(insets.left), scale(insets.bottom), scale(insets.right));
		return new Insets(scale(insets.top), scale(insets.left), scale(insets.bottom), scale(insets.right));
	}

	public static int round(float value) {
		return Math.round(value - 0.01f);
	}
}
