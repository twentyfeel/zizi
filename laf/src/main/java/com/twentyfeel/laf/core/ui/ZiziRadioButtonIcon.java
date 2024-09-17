// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Graphics2D;

/**
 * Icon for {@link javax.swing.JRadioButton}.
 * <p>
 * This class provides custom painting for the radio button icon in the Zizi Look and Feel (LaF).
 * It extends {@link ZiziCheckBoxIcon} to customize the appearance of the radio button.
 * </p>
 */
public class ZiziRadioButtonIcon extends ZiziCheckBoxIcon {

	/**
	 * Paints the focus border of the radio button.
	 *
	 * @param g2 the {@code Graphics2D} context
	 */
	@Override
	protected void paintFocusBorder(Graphics2D g2) {
		g2.fillOval(0, 0, 19, 19);
	}

	/**
	 * Paints the border of the radio button.
	 *
	 * @param g2 the {@code Graphics2D} context
	 */
	@Override
	protected void paintBorder(Graphics2D g2) {
		g2.fillOval(2, 2, 15, 15);
	}

	/**
	 * Paints the background of the radio button.
	 *
	 * @param g2 the {@code Graphics2D} context
	 */
	@Override
	protected void paintBackground(Graphics2D g2) {
		g2.fillOval(3, 3, 13, 13);
	}

	/**
	 * Paints the checkmark inside the radio button.
	 *
	 * @param g2 the {@code Graphics2D} context
	 */
	@Override
	protected void paintCheckmark(Graphics2D g2) {
		g2.fillOval(7, 7, 5, 5);
	}
}
