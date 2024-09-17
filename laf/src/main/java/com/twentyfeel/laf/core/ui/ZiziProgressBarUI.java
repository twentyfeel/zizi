// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.twentyfeel.laf.core.util.UIScale;
import com.twentyfeel.laf.core.util.ZiziUIUtils;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JProgressBar}.
 * <p>
 * This UI delegate customizes the appearance of the progress bar to fit the Zizi Look and Feel (LaF).
 * It extends {@link BasicProgressBarUI} to provide specific rendering for both determinate and indeterminate progress bars.
 * </p>
 */
public class ZiziProgressBarUI extends BasicProgressBarUI {

	/**
	 * Creates a new instance of {@code ZiziProgressBarUI}.
	 *
	 * @param c the component for which the UI delegate is being created
	 * @return a new instance of {@code ZiziProgressBarUI}
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziProgressBarUI();
	}

	/**
	 * Scales the preferred size for horizontal progress bars.
	 *
	 * @return the scaled preferred size
	 */
	@Override
	protected Dimension getPreferredInnerHorizontal() {
		return UIScale.scale(super.getPreferredInnerHorizontal());
	}

	/**
	 * Scales the preferred size for vertical progress bars.
	 *
	 * @return the scaled preferred size
	 */
	@Override
	protected Dimension getPreferredInnerVertical() {
		return UIScale.scale(super.getPreferredInnerVertical());
	}

	/**
	 * Updates the component's graphics context if the component is opaque.
	 *
	 * @param g the {@code Graphics} context
	 * @param c the component to be updated
	 */
	@Override
	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			ZiziUIUtils.paintParentBackground(g, c);
		}
		paint(g, c);
	}

	/**
	 * Paints the progress bar, including both the background and the progress.
	 *
	 * @param g the {@code Graphics} context
	 * @param c the component to be painted
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		Insets insets = progressBar.getInsets();
		int x = insets.left;
		int y = insets.top;
		int width = progressBar.getWidth() - (insets.right + insets.left);
		int height = progressBar.getHeight() - (insets.top + insets.bottom);

		if (width <= 0 || height <= 0) return;

		boolean horizontal = (progressBar.getOrientation() == JProgressBar.HORIZONTAL);
		int arc = horizontal ? height : width;

		ZiziUIUtils.configureRenderingHints((Graphics2D) g);

		// Paint the background of the progress bar
		g.setColor(progressBar.getBackground());
		((Graphics2D) g).fill(new RoundRectangle2D.Float(x, y, width, height, arc, arc));

		if (progressBar.isIndeterminate()) {
			// Paint indeterminate progress
			boxRect = getBox(boxRect);
			if (boxRect != null) {
				g.setColor(progressBar.getForeground());
				((Graphics2D) g).fill(new RoundRectangle2D.Float(boxRect.x, boxRect.y, boxRect.width, boxRect.height, arc, arc));
			}

			if (progressBar.isStringPainted()) {
				paintString(g, x, y, width, height, 0, insets);
			}
		} else {
			// Paint determinate progress
			int amountFull = getAmountFull(insets, width, height);

			g.setColor(progressBar.getForeground());
			((Graphics2D) g).fill(horizontal ? new RoundRectangle2D.Float(x, y, amountFull, height, arc, arc) : new RoundRectangle2D.Float(x, y + (height - amountFull), width, amountFull, arc, arc));

			if (progressBar.isStringPainted()) {
				paintString(g, x, y, width, height, amountFull, insets);
			}
		}
	}
}
