// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Paint;
import javax.swing.UIManager;

/**
 * A custom border for {@link javax.swing.JButton} components.
 * It applies different border styles based on button state (e.g., default, focused).
 */
public class ZiziButtonBorder extends ZiziBorder {

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
		String colorKey = ZiziButtonUI.isDefaultButton(component) ? "Button.default.focusColor" : "Component.focusColor";
		return UIManager.getColor(colorKey);
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
				return UIManager.getColor(isDefaultButton ? "Button.default.focusedBorderColor" : "Button.focusedBorderColor");
			}

			Color startColor = UIManager.getColor(isDefaultButton ? "Button.default.startBorderColor" : "Button.startBorderColor");
			Color endColor = UIManager.getColor(isDefaultButton ? "Button.default.endBorderColor" : "Button.endBorderColor");

			return startColor.equals(endColor) ? startColor : new GradientPaint(0, getFocusWidth(), startColor, 0, component.getHeight() - getFocusWidth() - 1f, endColor);
		} else {
			return UIManager.getColor("Button.disabledBorderColor");
		}
	}

	/**
	 * Provides the arc size for the rounded corners of the button border.
	 *
	 * @return the arc size
	 */
	@Override
	protected float getArc() {
		return ZiziUIUtils.getButtonArc();
	}
}
