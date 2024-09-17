// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import static com.twentyfeel.laf.core.util.UIScale.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.JTextComponent;

/**
 * A custom border used for various Swing components like {@link JTextField}.
 * It applies focus and border styling based on component state.
 */
public class ZiziBorder extends BasicBorders.MarginBorder {

	/**
	 * Paints the border of the component.
	 *
	 * @param component the component to paint the border for
	 * @param graphics  the Graphics object used for painting
	 * @param x         the x-coordinate of the border
	 * @param y         the y-coordinate of the border
	 * @param width     the width of the border
	 * @param height    the height of the border
	 */
	@Override
	public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) graphics.create();
		try {
			ZiziUIUtils.configureRenderingHints(g2d);

			float focusWidth = getFocusWidth();
			float lineWidth = getLineWidth();
			float arc = getArc();

			if (isComponentFocused(component)) {
				g2d.setColor(getFocusColor(component));
				ZiziUIUtils.paintOutlineBorder(g2d, x, y, width, height, focusWidth, lineWidth, arc);
			}

			g2d.setPaint(getBorderPaint(component));
			ZiziUIUtils.drawRoundedRectangle(g2d, x, y, width, height, focusWidth, lineWidth, arc);
		} finally {
			g2d.dispose();
		}
	}

	/**
	 * Retrieves the color used for the focus indicator.
	 *
	 * @param component the component whose focus color is to be retrieved
	 * @return the focus color
	 */
	protected Color getFocusColor(Component component) {
		return UIManager.getColor("Component.focusColor");
	}

	/**
	 * Determines the paint used for the border based on component state.
	 *
	 * @param component the component whose border color is to be retrieved
	 * @return the paint used for the border
	 */
	protected Paint getBorderPaint(Component component) {
		boolean isEnabled = component.isEnabled() && (!(component instanceof JTextComponent) || ((JTextComponent) component).isEditable());
		return ZiziUIUtils.getBorderColor(isEnabled, isComponentFocused(component));
	}

	/**
	 * Checks if the component or its view has focus.
	 *
	 * @param component the component to check
	 * @return true if the component or its view is focused, false otherwise
	 */
	protected boolean isComponentFocused(Component component) {
		if (component instanceof JScrollPane) {
			JViewport viewport = ((JScrollPane) component).getViewport();
			Component view = (viewport != null) ? viewport.getView() : null;
			return view != null && view.hasFocus();
		} else {
			return component.hasFocus();
		}
	}

	/**
	 * Gets the border insets for the component, adjusting for focus width and line width.
	 *
	 * @param component the component to get border insets for
	 * @param insets    the Insets object to populate
	 * @return the adjusted Insets object
	 */
	@Override
	public Insets getBorderInsets(Component component, Insets insets) {
		float outerWidth = getFocusWidth() + getLineWidth();

		insets = super.getBorderInsets(component, insets);
		insets.top = Math.round(scale((float) insets.top) + outerWidth);
		insets.left = Math.round(scale((float) insets.left) + outerWidth);
		insets.bottom = Math.round(scale((float) insets.bottom) + outerWidth);
		insets.right = Math.round(scale((float) insets.right) + outerWidth);
		return insets;
	}

	/**
	 * Provides the width of the focus indicator.
	 *
	 * @return the focus width
	 */
	protected float getFocusWidth() {
		return ZiziUIUtils.getFocusWidth();
	}

	/**
	 * Provides the width of the borderline.
	 *
	 * @return the line width
	 */
	protected float getLineWidth() {
		return ZiziUIUtils.getLineWidth();
	}

	/**
	 * Provides the arc size for rounded corners.
	 *
	 * @return the arc size
	 */
	protected float getArc() {
		return 0;
	}
}
