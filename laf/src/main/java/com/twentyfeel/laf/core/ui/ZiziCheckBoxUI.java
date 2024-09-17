// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalCheckBoxUI;

/**
 * Provides the Zizi LaF (Look and Feel) UI delegate for {@link javax.swing.JCheckBox}.
 * This class extends {@link MetalCheckBoxUI} to customize the appearance of checkboxes.
 */
public class ZiziCheckBoxUI extends MetalCheckBoxUI {
	private static ComponentUI instance;

	/**
	 * Creates a new UI instance for the specified component.
	 *
	 * @param component the component to create a UI for
	 * @return a new instance of {@link ZiziCheckBoxUI}
	 */
	public static ComponentUI createUI(JComponent component) {
		if (instance == null) {
			instance = new ZiziCheckBoxUI();
		}
		return instance;
	}

	/**
	 * Overridden to handle focus painting.
	 *
	 * @param g the graphics context
	 * @param t the focus rectangle
	 * @param d the dimension of the component
	 */
	@Override
	protected void paintFocus(Graphics g, Rectangle t, Dimension d) {
	}
}
