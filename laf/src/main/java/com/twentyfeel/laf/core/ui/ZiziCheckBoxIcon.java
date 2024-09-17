// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import com.twentyfeel.laf.core.util.UIScale;
import com.twentyfeel.laf.core.util.ZiziUIUtils;

/**
 * Custom icon for {@link javax.swing.JCheckBox}.
 * Implements {@link Icon} and {@link UIResource}.
 */
public class ZiziCheckBoxIcon implements Icon, UIResource {

	/**
	 * Paints the icon.
	 *
	 * @param component the component to paint the icon on
	 * @param graphics  the graphics context
	 * @param x         the x-coordinate of the icon
	 * @param y         the y-coordinate of the icon
	 */
	@Override
	public void paintIcon(Component component, Graphics graphics, int x, int y) {
		Graphics2D g2 = (Graphics2D) graphics.create();
		try {
			ZiziUIUtils.configureRenderingHints(g2);

			g2.translate(x, y);
			UIScale.scaleGraphics(g2);

			boolean enabled = component.isEnabled();
			boolean focused = component.hasFocus();
			boolean selected = (component instanceof AbstractButton) && ((AbstractButton) component).isSelected();

			if (focused) {
				g2.setColor(UIManager.getColor("Component.focusColor"));
				paintFocusBorder(g2);
			}

			g2.setColor(UIManager.getColor(enabled ? (selected ? (focused ? "CheckBox.icon.selectedFocusedBorderColor" : "CheckBox.icon.selectedBorderColor") : (focused ? "CheckBox.icon.focusedBorderColor" : "CheckBox.icon.borderColor")) : "CheckBox.icon.disabledBorderColor"));
			paintBorder(g2);

			g2.setColor(UIManager.getColor(enabled ? (selected ? "CheckBox.icon.selectedBackground" : "CheckBox.icon.background") : "CheckBox.icon.disabledBackground"));
			paintBackground(g2);

			if (selected) {
				g2.setColor(UIManager.getColor(enabled ? "CheckBox.icon.checkmarkColor" : "CheckBox.icon.disabledCheckmarkColor"));
				paintCheckmark(g2);
			}
		} finally {
			g2.dispose();
		}
	}

	/**
	 * Paints the focus border of the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintFocusBorder(Graphics2D g2) {
		g2.fillRoundRect(1, 0, 18, 18, 8, 8);
	}

	/**
	 * Paints the border of the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintBorder(Graphics2D g2) {
		g2.fillRoundRect(3, 2, 14, 14, 4, 4);
	}

	/**
	 * Paints the background of the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintBackground(Graphics2D g2) {
		g2.fillRoundRect(4, 3, 12, 12, 4, 4);
	}

	/**
	 * Paints the checkmark inside the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintCheckmark(Graphics2D g2) {
		g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		Path2D.Float path = new Path2D.Float();
		path.moveTo(6.5f, 9.5f);
		path.lineTo(8.6f, 12f);
		path.lineTo(13.25f, 5.5f);
		g2.draw(path);
	}

	/**
	 * Returns the width of the icon.
	 *
	 * @return the width of the icon
	 */
	@Override
	public int getIconWidth() {
		return scale(19);
	}

	/**
	 * Returns the height of the icon.
	 *
	 * @return the height of the icon
	 */
	@Override
	public int getIconHeight() {
		return scale(19);
	}
}
