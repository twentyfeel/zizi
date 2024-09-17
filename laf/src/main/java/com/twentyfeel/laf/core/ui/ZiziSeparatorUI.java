// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JSeparator}.
 * <p>
 * This class customizes the appearance of separators, allowing them to be styled
 * consistently with the rest of the Zizi Look and Feel.
 * </p>
 */
public class ZiziSeparatorUI extends BasicSeparatorUI {
	private static final int WIDTH = 2;

	private static ComponentUI instance;

	/**
	 * Creates a new instance of ZiziSeparatorUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziSeparatorUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		if (instance == null) {
			instance = new ZiziSeparatorUI();
		}
		return instance;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		JSeparator separator = (JSeparator) c;
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(c.getForeground());

		if (separator.getOrientation() == JSeparator.VERTICAL) {
			g2.fill(new Rectangle2D.Float(0, 0, scale((float) WIDTH), c.getHeight()));
		} else {
			g2.fill(new Rectangle2D.Float(0, 0, c.getWidth(), scale((float) WIDTH)));
		}
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		JSeparator separator = (JSeparator) c;
		if (separator.getOrientation() == JSeparator.VERTICAL) {
			return new Dimension(scale(WIDTH), 0);
		} else {
			return new Dimension(0, scale(WIDTH));
		}
	}
}
