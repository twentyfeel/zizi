// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * Provides the Zizi Look and Feel (LaF) UI delegate for {@link javax.swing.JFormattedTextField}.
 * This class customizes the UI for formatted text fields according to the Zizi LaF.
 */
public class ZiziFormattedTextFieldUI extends ZiziTextFieldUI {

	/**
	 * Creates a new instance of ZiziFormattedTextFieldUI.
	 *
	 * @param c the JComponent to which this UI delegate will be applied
	 * @return a new ZiziFormattedTextFieldUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziFormattedTextFieldUI();
	}

	/**
	 * Returns the property prefix used for this UI delegate.
	 * This prefix is used to fetch UI defaults from the look and feel.
	 *
	 * @return the property prefix as a String
	 */
	@Override
	protected String getPropertyPrefix() {
		return "FormattedTextField";
	}
}
