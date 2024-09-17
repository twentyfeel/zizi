// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JTextPane}.
 * <p>
 * This class extends {@link BasicTextPaneUI} to provide a custom look-and-feel
 * for JTextPane components in the Zizi LaF (Look-and-Feel) theme. Currently,
 * it uses the default implementation, but can be customized further if needed.
 * </p>
 */
public class ZiziTextPaneUI extends BasicTextPaneUI {

	/**
	 * Creates a new instance of ZiziTextPaneUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziTextPaneUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziTextPaneUI();
	}
}
