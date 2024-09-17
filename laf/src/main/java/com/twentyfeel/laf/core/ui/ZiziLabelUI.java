// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Graphics;
import java.awt.FontMetrics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * Provides the Zizi Look and Feel (LaF) UI delegate for {@link javax.swing.JLabel}.
 * This class customizes the UI for labels according to the Zizi LaF.
 */
public class ZiziLabelUI extends BasicLabelUI {

	private static ComponentUI instance;

	/**
	 * Creates a new instance of ZiziLabelUI.
	 *
	 * @param c the JComponent to which this UI delegate will be applied
	 * @return a new ZiziLabelUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		if (instance == null) instance = new ZiziLabelUI();
		return instance;
	}

	/**
	 * Paints the text of a disabled JLabel.
	 * This method is used to render the text of a JLabel when it is in a disabled state,
	 * including handling the mnemonic (shortcut) character.
	 *
	 * @param l     the JLabel to paint
	 * @param g     the Graphics object used for painting
	 * @param s     the text to paint
	 * @param textX the x-coordinate where the text should start
	 * @param textY the y-coordinate where the text should be painted
	 */
	@Override
	protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY) {
		g.setColor(UIManager.getColor("Label.disabledForeground"));
		FontMetrics fm = g.getFontMetrics();
		int mnemIndex = l.getDisplayedMnemonicIndex();

		// Draw the text
		g.drawString(s, textX, textY);

		// Draw the underline for the mnemonic character
		if (mnemIndex >= 0 && mnemIndex < s.length()) {
			char mnemonicChar = s.charAt(mnemIndex);
			int charWidth = fm.charWidth(mnemonicChar);
			int underlineY = textY + fm.getDescent();
			g.drawLine(textX + fm.stringWidth(s.substring(0, mnemIndex)), underlineY, textX + fm.stringWidth(s.substring(0, mnemIndex)) + charWidth, underlineY);
		}
	}
}
