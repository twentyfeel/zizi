// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import static com.twentyfeel.laf.core.util.UIScale.scale;

/**
 * Custom UI delegate for {@link javax.swing.JButton}.
 * This class handles the rendering of button components with custom styles.
 */
public class ZiziButtonUI extends BasicButtonUI {

	protected int focusWidth;
	protected int arc;
	protected Color disabledText;
	protected Color defaultBackground;
	protected Color defaultForeground;

	private static ComponentUI instance;

	/**
	 * Creates and returns a UI delegate for a JButton component.
	 *
	 * @param component the button component
	 * @return the UI delegate
	 */
	public static ComponentUI createUI(JComponent component) {
		if (instance == null) {
			instance = new ZiziButtonUI();
		}
		return instance;
	}

	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		focusWidth = UIManager.getInt("Component.focusWidth");
		arc = UIManager.getInt("Button.arc");
		disabledText = UIManager.getColor("Button.disabledText");
		defaultBackground = UIManager.getColor("Button.default.background");
		defaultForeground = UIManager.getColor("Button.default.foreground");
	}

	/**
	 * Checks if the content area of the button should be filled.
	 *
	 * @param component the button component
	 * @return true if the content area should be filled, false otherwise
	 */
	static boolean isContentAreaFilled(Component component) {
		return component instanceof AbstractButton && ((AbstractButton) component).isContentAreaFilled();
	}

	/**
	 * Determines if the button is the default button.
	 *
	 * @param component the button component
	 * @return true if the button is the default button, false otherwise
	 */
	static boolean isDefaultButton(Component component) {
		return component instanceof JButton && ((JButton) component).isDefaultButton();
	}

	/**
	 * Updates the button's appearance.
	 *
	 * @param graphics  the graphics context
	 * @param component the button component
	 */
	@Override
	public void update(Graphics graphics, JComponent component) {
		if (component.isOpaque() && ZiziButtonUI.isContentAreaFilled(component)) {
			ZiziUIUtils.paintParentBackground(graphics, component);

			if (component.isEnabled()) {
				Graphics2D g2d = (Graphics2D) graphics.create();
				try {
					ZiziUIUtils.configureRenderingHints(g2d);

					float focusWidth = (component.getBorder() instanceof ZiziBorder) ? scale((float) this.focusWidth) : 0;
					float arc = (component.getBorder() instanceof ZiziButtonBorder) ? scale((float) this.arc) : 0;

					g2d.setColor(getBackgroundColor(component));
					ZiziUIUtils.fillRoundedRectangle(g2d, 0, 0, component.getWidth(), component.getHeight(), focusWidth, arc);
				} finally {
					g2d.dispose();
				}
			}
		}

		paint(graphics, component);
	}

	/**
	 * Paints the text of the button.
	 *
	 * @param graphics  the graphics context
	 * @param component the button component
	 * @param textRect  the rectangle area where text should be drawn
	 * @param text      the text to be drawn
	 */
	@Override
	protected void paintText(Graphics graphics, JComponent component, Rectangle textRect, String text) {
		AbstractButton button = (AbstractButton) component;
		FontMetrics fontMetrics = graphics.getFontMetrics();
		int mnemonicIndex = button.getDisplayedMnemonicIndex();

		graphics.setColor(button.getModel().isEnabled() ? getForegroundColor(component) : disabledText);

		// Draw the text
		graphics.drawString(text, textRect.x + getTextShiftOffset(), textRect.y + fontMetrics.getAscent() + getTextShiftOffset());

		// Draw the underline for mnemonic character
		if (mnemonicIndex >= 0 && mnemonicIndex < text.length()) {
			char mnemonicChar = text.charAt(mnemonicIndex);
			int charWidth = fontMetrics.charWidth(mnemonicChar);
			int underlineY = textRect.y + fontMetrics.getDescent(); // Adjust as needed
			graphics.drawLine(textRect.x + fontMetrics.stringWidth(text.substring(0, mnemonicIndex)), underlineY, textRect.x + fontMetrics.stringWidth(text.substring(0, mnemonicIndex)) + charWidth, underlineY);
		}
	}

	/**
	 * Retrieves the background color of the button.
	 *
	 * @param component the button component
	 * @return the background color
	 */
	private Color getBackgroundColor(Component component) {
		boolean isDefaultButton = ZiziButtonUI.isDefaultButton(component);
		return isDefaultButton ? defaultBackground : component.getBackground();
	}

	/**
	 * Retrieves the foreground color of the button.
	 *
	 * @param component the button component
	 * @return the foreground color
	 */
	private Color getForegroundColor(Component component) {
		boolean isDefaultButton = ZiziButtonUI.isDefaultButton(component);
		return isDefaultButton ? defaultForeground : component.getForeground();
	}
}
