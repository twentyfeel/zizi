// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
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

	protected final int focusWidth = UIManager.getInt("Component.focusWidth");
	protected final Color focusColor = UIManager.getColor("Component.focusColor");
	protected final Color borderColor = UIManager.getColor("CheckBox.icon.borderColor");
	protected final Color disabledBorderColor = UIManager.getColor("CheckBox.icon.disabledBorderColor");
	protected final Color selectedBorderColor = UIManager.getColor("CheckBox.icon.selectedBorderColor");
	protected final Color focusedBorderColor = UIManager.getColor("CheckBox.icon.focusedBorderColor");
	protected final Color selectedFocusedBorderColor = UIManager.getColor("CheckBox.icon.selectedFocusedBorderColor");
	protected final Color background = UIManager.getColor("CheckBox.icon.background");
	protected final Color disabledBackground = UIManager.getColor("CheckBox.icon.disabledBackground");
	protected final Color selectedBackground = UIManager.getColor("CheckBox.icon.selectedBackground");
	protected final Color checkmarkColor = UIManager.getColor("CheckBox.icon.checkmarkColor");
	protected final Color disabledCheckmarkColor = UIManager.getColor("CheckBox.icon.disabledCheckmarkColor");
	protected final int iconSize = 15 + (focusWidth * 2);

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
				g2.setColor(focusColor);
				paintFocusBorder(g2);
			}

			g2.setColor(UIManager.getColor(enabled ? (selected ? (focused ? selectedFocusedBorderColor : selectedBorderColor) : (focused ? focusedBorderColor : borderColor)) : disabledBorderColor));
			paintBorder(g2);

			g2.setColor(enabled ? (selected ? selectedBackground : background) : disabledBackground);
			paintBackground(g2);

			if (selected) {
				g2.setColor(enabled ? checkmarkColor : disabledCheckmarkColor);
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
		g2.fillRoundRect(1, 0, iconSize - 1, iconSize - 1, 8, 8);
	}

	/**
	 * Paints the border of the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintBorder(Graphics2D g2) {
		g2.fillRoundRect(focusWidth + 1, focusWidth, 14, 14, 4, 4);
	}

	/**
	 * Paints the background of the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintBackground(Graphics2D g2) {
		g2.fillRoundRect(focusWidth + 2, focusWidth + 1, 12, 12, 4, 4);
	}

	/**
	 * Paints the checkmark inside the checkbox icon.
	 *
	 * @param g2 the graphics context
	 */
	protected void paintCheckmark(Graphics2D g2) {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(4.5f, 7.5f);
		path.lineTo(6.6f, 10f);
		path.lineTo(11.25f, 3.5f);
		g2.translate(focusWidth, focusWidth);
		g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(path);
		g2.translate(-focusWidth, -focusWidth);
	}

	/**
	 * Returns the width of the icon.
	 *
	 * @return the width of the icon
	 */
	@Override
	public int getIconWidth() {
		return scale(iconSize);
	}

	/**
	 * Returns the height of the icon.
	 *
	 * @return the height of the icon
	 */
	@Override
	public int getIconHeight() {
		return scale(iconSize);
	}
}
