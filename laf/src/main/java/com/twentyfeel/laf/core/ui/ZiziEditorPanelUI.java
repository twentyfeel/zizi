// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;

/**
 * Provides the Zizi LaF (Look and Feel) UI delegate for {@link javax.swing.JEditorPane}.
 * This class customizes the appearance and behavior of JEditorPane components.
 */
public class ZiziEditorPanelUI extends BasicEditorPaneUI {

	/**
	 * Creates a new UI instance for the specified component.
	 *
	 * @param c the component to create a UI for
	 * @return a new instance of {@link ZiziEditorPanelUI}
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziEditorPanelUI();
	}
}
