// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JScrollPane}.
 * <p>
 * This class customizes the scroll pane's UI by handling container and focus events
 * to ensure proper repainting when the viewport or its view gains or loses focus.
 * </p>
 */
public class ZiziScrollPaneUI extends BasicScrollPaneUI {
	private Handler handler;

	/**
	 * Creates a new instance of ZiziScrollPaneUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziScrollPaneUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziScrollPaneUI();
	}

	@Override
	protected void installListeners(JScrollPane c) {
		super.installListeners(c);

		JViewport viewport = scrollpane.getViewport();
		if (viewport != null) {
			viewport.addContainerListener(getHandler());
		}
	}

	@Override
	protected void uninstallListeners(JComponent c) {
		super.uninstallListeners(c);

		JViewport viewport = scrollpane.getViewport();
		if (viewport != null) {
			viewport.removeContainerListener(getHandler());
		}
		handler = null;
	}

	public Handler getHandler() {
		if (handler == null) handler = new Handler();
		return handler;
	}

	@Override
	protected void updateViewport(PropertyChangeEvent e) {
		super.updateViewport(e);

		JViewport oldViewport = (JViewport) e.getOldValue();
		JViewport newViewport = (JViewport) e.getNewValue();

		if (oldViewport != null) {
			oldViewport.removeContainerListener(getHandler());

			Component oldView = oldViewport.getView();
			if (oldView != null) {
				oldView.removeFocusListener(getHandler());
			}
		}
		if (newViewport != null) {
			newViewport.addContainerListener(getHandler());

			Component newView = newViewport.getView();
			if (newView != null) {
				newView.addFocusListener(getHandler());
			}
		}
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			ZiziUIUtils.paintParentBackground(g, c);

			Insets insets = c.getInsets();
			g.setColor(c.getBackground());
			g.fillRect(insets.left, insets.top, c.getWidth() - insets.left - insets.right, c.getHeight() - insets.top - insets.bottom);
		}

		paint(g, c);
	}

	/**
	 * Handles container and focus events to keep the view's focus listener up-to-date.
	 */
	private class Handler implements ContainerListener, FocusListener {
		@Override
		public void componentAdded(ContainerEvent e) {
			e.getChild().addFocusListener(this);
		}

		@Override
		public void componentRemoved(ContainerEvent e) {
			e.getChild().removeFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent e) {
			scrollpane.repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
			scrollpane.repaint();
		}
	}
}
