// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Paint;
import javax.swing.UIManager;

import static com.twentyfeel.laf.core.util.UIScale.scale;

/**
 * A custom border for {@link javax.swing.JButton} components.
 * It applies different border styles based on button state (e.g., default, focused).
 */
public class ZiziButtonBorder extends ZiziBorder {

	protected final Color startBorderColor = UIManager.getColor("Button.startBorderColor");
	protected final Color endBorderColor = UIManager.getColor("Button.endBorderColor");
	protected final Color disabledBorderColor = UIManager.getColor("Button.disabledBorderColor");
	protected final Color focusedBorderColor = UIManager.getColor("Button.focusedBorderColor");
	protected final Color defaultStartBorderColor = UIManager.getColor("Button.default.startBorderColor");
	protected final Color defaultEndBorderColor = UIManager.getColor("Button.default.endBorderColor");
	protected final Color defaultFocusedBorderColor = UIManager.getColor("Button.default.focusedBorderColor");
	protected final Color defaultFocusColor = UIManager.getColor("Button.default.focusColor");
	protected final int arc = UIManager.getInt("Button.arc");

	/**
	 * Paints the border of the button component.
	 *
	 * @param component the button component to paint the border for
	 * @param graphics  the Graphics object used for painting
	 * @param x         the x-coordinate of the border
	 * @param y         the y-coordinate of the border
	 * @param width     the width of the border
	 * @param height    the height of the border
	 */
	@Override
	public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
		if (ZiziButtonUI.isContentAreaFilled(component)) {
			super.paintBorder(component, graphics, x, y, width, height);
		}
	}

	/**
	 * Retrieves the color used for the focus indicator based on whether the button is default.
	 *
	 * @param component the button component
	 * @return the focus color
	 */
	@Override
	protected Color getFocusColor(Component component) {
		return ZiziButtonUI.isDefaultButton(component) ? defaultFocusColor : super.getFocusColor(component);
	}

	/**
	 * Determines the paint used for the button border based on its enabled state and focus.
	 *
	 * @param component the button component
	 * @return the paint for the border
	 */
	@Override
	protected Paint getBorderPaint(Component component) {
		if (component.isEnabled()) {
			boolean isDefaultButton = ZiziButtonUI.isDefaultButton(component);
			if (component.hasFocus()) {
				return isDefaultButton ? defaultFocusedBorderColor : focusedBorderColor;
			}

			Color startColor = isDefaultButton ? defaultStartBorderColor : startBorderColor;
			Color endColor = isDefaultButton ? defaultEndBorderColor : endBorderColor;

			return startColor.equals(endColor) ? startColor : new GradientPaint(0, getFocusWidth(), startColor, 0, component.getHeight() - getFocusWidth() - 1f, endColor);
		} else {
			return disabledBorderColor;
		}
	}

	/**
	 * Provides the arc size for the rounded corners of the button border.
	 *
	 * @return the arc size
	 */
	@Override
	protected float getArc() {
		return scale((float) arc);
	}
}
