// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JTextArea}.
 * <p>
 * This class customizes the appearance of JTextArea components,
 * setting different background colors based on the component's state.
 * </p>
 */
public class ZiziTextAreaUI extends BasicTextAreaUI {

	/**
	 * Creates a new instance of ZiziTextAreaUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziTextAreaUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziTextAreaUI();
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent textComponent = getComponent();

		// Determine the appropriate background color based on component state
		g.setColor(getBackgroundColor(textComponent));
		g.fillRect(0, 0, textComponent.getWidth(), textComponent.getHeight());
	}

	/**
	 * Returns the background color based on the component's state.
	 *
	 * @param c the JTextComponent to get the background color for
	 * @return the appropriate background color
	 */
	private java.awt.Color getBackgroundColor(JTextComponent c) {
		if (!c.isEnabled()) {
			return UIManager.getColor("TextArea.disabledBackground");
		} else if (!c.isEditable()) {
			return UIManager.getColor("TextArea.inactiveBackground");
		} else {
			return c.getBackground();
		}
	}
}
