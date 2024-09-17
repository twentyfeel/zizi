// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.JTextComponent;

import com.twentyfeel.laf.core.util.UIScale;
import com.twentyfeel.laf.core.util.ZiziUIUtils;

/**
 * Provides the Zizi LaF (Look and Feel) UI delegate for {@link javax.swing.JComboBox}.
 * This class customizes the appearance and behavior of JComboBox components.
 */
public class ZiziComboBoxUI extends BasicComboBoxUI {

	/**
	 * Creates a new UI instance for the specified component.
	 *
	 * @param c the component to create a UI for
	 * @return a new instance of {@link ZiziComboBoxUI}
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziComboBoxUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		padding = UIScale.scale(padding); // Scale padding
	}

	@Override
	protected LayoutManager createLayoutManager() {
		return new BasicComboBoxUI.ComboBoxLayoutManager() {
			@Override
			public void layoutContainer(Container parent) {
				super.layoutContainer(parent);

				// Adjust editor bounds based on padding
				if (editor != null && padding != null) {
					editor.setBounds(ZiziUIUtils.adjustRectangle(editor.getBounds(), padding));
				}
			}
		};
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return new BasicComboBoxUI.PropertyChangeHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				super.propertyChange(e);

				Object source = e.getSource();
				String propertyName = e.getPropertyName();

				// Update editor colors based on property changes
				if (editor != null && ((source == comboBox && (propertyName.equals("background") || propertyName.equals("foreground"))) || (source == editor && propertyName.equals("enabled")))) {
					updateEditorColors();
				}
			}
		};
	}

	@Override
	protected void configureEditor() {
		super.configureEditor();
		updateEditorColors(); // Update editor colors initially
	}

	/**
	 * Updates the editor's background and foreground colors based on its enabled state.
	 */
	private void updateEditorColors() {
		boolean enabled = editor.isEnabled();
		editor.setBackground(enabled ? comboBox.getBackground() : UIManager.getColor("ComboBox.disabledBackground"));
		editor.setForeground((enabled || editor instanceof JTextComponent) ? comboBox.getForeground() : UIManager.getColor("ComboBox.disabledForeground"));
		if (editor instanceof JTextComponent) {
			((JTextComponent) editor).setDisabledTextColor(UIManager.getColor("ComboBox.disabledForeground"));
		}
	}

	@Override
	protected JButton createArrowButton() {
		return new ZiziArrowButton(); // Use custom arrow button
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			ZiziUIUtils.paintParentBackground(g, c);

			Graphics2D g2 = (Graphics2D) g;
			ZiziUIUtils.configureRenderingHints(g2);

			int width = c.getWidth();
			int height = c.getHeight();
			float focusWidth = ZiziUIUtils.getFocusWidth(c);
			float arc = ZiziUIUtils.getComponentArc(c);
			int arrowX = arrowButton.getX();

			// Paint the background
			g2.setColor(comboBox.isEnabled() ? c.getBackground() : UIManager.getColor("ComboBox.disabledBackground"));
			ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, width, height, focusWidth, arc);

			// Paint the button background
			g2.setColor(UIManager.getColor(comboBox.isEnabled() ? (comboBox.isEditable() ? "ComboBox.buttonEditableBackground" : "ComboBox.buttonBackground") : "ComboBox.disabledBackground"));
			Shape oldClip = g2.getClip();
			g2.clipRect(arrowX, 0, width - arrowX, height);
			ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, width, height, focusWidth, arc);
			g2.setClip(oldClip);

			// Paint border for editable combo boxes
			if (comboBox.isEditable()) {
				g2.setColor(ZiziUIUtils.getBorderColor(comboBox.isEnabled(), false));
				g2.fill(new Rectangle2D.Float(arrowX, focusWidth, scale(1f), height - (focusWidth * 2)));
			}
		}

		paint(g, c); // Call superclass method for additional painting
	}

	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		super.paintCurrentValue(g, bounds, false); // Custom painting of current value
	}

	/**
	 * Custom arrow button used in the combo box.
	 */
	private static class ZiziArrowButton extends BasicArrowButton {
		ZiziArrowButton() {
			super(SOUTH, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
			setOpaque(false);
			setBorder(null);
		}

		@Override
		public void paint(Graphics g) {
			ZiziUIUtils.configureRenderingHints((Graphics2D) g);

			int w = scale(9);
			int h = scale(5);
			int x = Math.round((getWidth() - w) / 2f);
			int y = Math.round((getHeight() - h) / 2f);

			Path2D arrow = new Path2D.Float();
			arrow.moveTo(x, y);
			arrow.lineTo(x + w, y);
			arrow.lineTo(x + (w / 2f), y + h);
			arrow.closePath();

			g.setColor(UIManager.getColor(isEnabled() ? "ComboBox.buttonArrowColor" : "ComboBox.buttonDisabledArrowColor"));
			((Graphics2D) g).fill(arrow);
		}
	}
}
