// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.twentyfeel.laf.core.util.UIScale;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JScrollBar}.
 * <p>
 * This class customizes the appearance of the scrollbar by making
 * the increase and decrease buttons invisible and customizing the
 * appearance of the scrollbar thumb.
 * </p>
 */
public class ZiziScrollBarUI extends BasicScrollBarUI {

	/**
	 * Creates a new instance of ZiziScrollBarUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziScrollBarUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziScrollBarUI();
	}

	/**
	 * Returns the preferred size of the scrollbar, scaled as per the UI scale.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the preferred size of the scrollbar
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		return UIScale.scale(super.getPreferredSize(c));
	}

	/**
	 * Creates an invisible button for the scrollbar's decrease button.
	 *
	 * @param orientation the orientation of the button (up or down)
	 * @return the invisible button
	 */
	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createInvisibleButton();
	}

	/**
	 * Creates an invisible button for the scrollbar's increase button.
	 *
	 * @param orientation the orientation of the button (up or down)
	 * @return the invisible button
	 */
	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createInvisibleButton();
	}

	/**
	 * Creates an invisible button with no dimensions and focus behavior.
	 *
	 * @return the invisible button
	 */
	private JButton createInvisibleButton() {
		JButton button = new JButton();
		button.setMinimumSize(new Dimension());
		button.setMaximumSize(new Dimension());
		button.setPreferredSize(new Dimension());
		button.setFocusable(false);
		button.setRequestFocusEnabled(false);
		return button;
	}

	/**
	 * Override to prevent painting of the decrease highlight.
	 *
	 * @param g the graphics context
	 */
	@Override
	protected void paintDecreaseHighlight(Graphics g) {
		// Do not paint the decrease highlight
	}

	/**
	 * Override to prevent painting of the increase highlight.
	 *
	 * @param g the graphics context
	 */
	@Override
	protected void paintIncreaseHighlight(Graphics g) {
		// Do not paint the increase highlight
	}

	/**
	 * Paints the scrollbar thumb.
	 *
	 * @param g           the graphics context
	 * @param c           the component to which the UI delegate is applied
	 * @param thumbBounds the bounds of the thumb
	 */
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;

		g.setColor(thumbColor);
		g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
	}
}
