// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

import static com.twentyfeel.laf.core.util.UIScale.scale;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JTextField}.
 * <p>
 * This class customizes the appearance and behavior of JTextField components,
 * handling focus events and painting the background with rounded corners.
 * </p>
 */
public class ZiziTextFieldUI extends BasicTextFieldUI {

	private Handler focusHandler;

	protected int focusWidth;

	/**
	 * Creates a new instance of ZiziTextFieldUI.
	 *
	 * @param c the component to which the UI delegate is applied
	 * @return the ZiziTextFieldUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziTextFieldUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		focusWidth = UIManager.getInt("Component.focusWidth");
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		getComponent().addFocusListener(getFocusHandler());
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		getComponent().removeFocusListener(getFocusHandler());

		focusHandler = null;
	}

	public Handler getFocusHandler() {
		if (focusHandler == null) {
			focusHandler = new Handler();
		}
		return focusHandler;
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent textComponent = getComponent();

		ZiziUIUtils.paintParentBackground(g, textComponent);

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			ZiziUIUtils.configureRenderingHints(g2);

			float focusWidth = (textComponent.getBorder() instanceof ZiziBorder) ? scale((float) this.focusWidth) : 0;
			g2.setColor(textComponent.getBackground());

			ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, textComponent.getWidth(), textComponent.getHeight(), focusWidth, 0);
		} finally {
			g2.dispose();
		}
	}

	/**
	 * Handles focus events to trigger repaint.
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
