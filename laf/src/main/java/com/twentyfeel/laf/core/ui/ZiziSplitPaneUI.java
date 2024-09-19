// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.twentyfeel.laf.core.util.UIScale;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JSplitPane}.
 *
 * @uiDefault SplitPane.background              Color
 * @uiDefault SplitPane.foreground              Color unused
 * @uiDefault SplitPane.dividerSize             int
 * @uiDefault SplitPane.continuousLayout        boolean
 * @uiDefault SplitPane.border                  Border
 * @uiDefault SplitPaneDivider.border           Border
 * @uiDefault SplitPaneDivider.draggingColor    Color only used if continuousLayout is false
 */
public class ZiziSplitPaneUI extends BasicSplitPaneUI {
	private Boolean continuousLayout;

	/**
	 * Creates a new ZiziSplitPaneUI instance.
	 *
	 * @param c the component for which this UI is being created
	 * @return a new ZiziSplitPaneUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziSplitPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		continuousLayout = (Boolean) UIManager.get("SplitPane.continuousLayout");
	}

	@Override
	public boolean isContinuousLayout() {
		return super.isContinuousLayout() || (Boolean.TRUE.equals(continuousLayout));
	}

	@Override
	public BasicSplitPaneDivider createDefaultDivider() {
		return new ZiziSplitPaneDivider(this);
	}

	/**
	 * Custom divider for the ZiziSplitPaneUI.
	 */
	private static class ZiziSplitPaneDivider extends BasicSplitPaneDivider {
		/**
		 * Creates a new ZiziSplitPaneDivider.
		 *
		 * @param ui the BasicSplitPaneUI instance
		 */
		public ZiziSplitPaneDivider(BasicSplitPaneUI ui) {
			super(ui);
		}

		@Override
		public void setDividerSize(int newSize) {
			super.setDividerSize(UIScale.scale(newSize));
		}
	}
}
