// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalRadioButtonUI;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JRadioButton}.
 * <p>
 * This class customizes the appearance of the radio button by
 * painting the focus border directly in the radio button icon.
 * </p>
 */
public class ZiziRadioButtonUI extends MetalRadioButtonUI {
	private static ComponentUI instance;

	/**
	 * Creates a new instance of ZiziRadioButtonUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziRadioButtonUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		if (instance == null) {
			instance = new ZiziRadioButtonUI();
		}
		return instance;
	}

	/**
	 * Override to prevent painting the focus border outside the icon.
	 * The focus border is painted directly in the icon.
	 *
	 * @param g the graphics context
	 * @param t the rectangle area to paint
	 * @param d the dimension of the area to paint
	 */
	@Override
	protected void paintFocus(Graphics g, Rectangle t, Dimension d) {
		// Focus border painted in icon
	}
}
