// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.JTextComponent;

import static com.twentyfeel.laf.core.util.UIScale.scale;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JPasswordField}.
 * <p>
 * This UI delegate handles the custom painting and focus behavior for password fields
 * in the Zizi Look and Feel (LaF). It extends {@link BasicPasswordFieldUI} to provide
 * specific rendering and focus management.
 * </p>
 */
public class ZiziPasswordFieldUI extends BasicPasswordFieldUI {
	private Handler handler;

	protected int focusWidth;

	/**
	 * Creates a new instance of {@code ZiziPasswordFieldUI}.
	 *
	 * @param c the component for which the UI delegate is being created
	 * @return a new instance of {@code ZiziPasswordFieldUI}
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziPasswordFieldUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		focusWidth = UIManager.getInt("Component.focusWidth");
	}

	/**
	 * Installs the focus listeners required for custom focus handling.
	 */
	@Override
	protected void installListeners() {
		super.installListeners();
		getComponent().addFocusListener(getHandler());
	}

	/**
	 * Uninstalls the focus listeners added during installation.
	 */
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		getComponent().removeFocusListener(getHandler());
		handler = null;
	}

	public Handler getHandler() {
		if (handler == null) handler = new Handler();
		return handler;
	}

	/**
	 * Paints the background of the password field.
	 * <p>
	 * This method uses {@link ZiziUIUtils} to paint a rounded rectangle with the
	 * component's background color and a focus width.
	 * </p>
	 *
	 * @param g the {@code Graphics} context
	 */
	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent c = getComponent();

		ZiziUIUtils.paintParentBackground(g, c);

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			ZiziUIUtils.configureRenderingHints(g2);

			float focusWidth = (c.getBorder() instanceof ZiziBorder) ? scale((float) this.focusWidth) : 0;

			g2.setColor(c.getBackground());
			ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, c.getWidth(), c.getHeight(), focusWidth, 0);
		} finally {
			g2.dispose();
		}
	}

	/**
	 * Handler for focus events to trigger repainting of the component.
	 */
	private class Handler implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			getComponent().repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
			getComponent().repaint();
		}
	}
}
