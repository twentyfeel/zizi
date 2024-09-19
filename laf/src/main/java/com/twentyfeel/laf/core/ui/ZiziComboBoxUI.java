// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.JTextComponent;

import com.twentyfeel.laf.core.util.UIScale;
import com.twentyfeel.laf.core.util.ZiziUIUtils;

/**
 * Provides the Zizi LaF (Look and Feel) UI delegate for {@link javax.swing.JComboBox}.
 * This class customizes the appearance and behavior of JComboBox components.
 */
public class ZiziComboBoxUI extends BasicComboBoxUI {

	protected int focusWidth;
	protected int arc;
	protected Color borderColor;
	protected Color disabledBorderColor;
	protected Color disabledBackground;
	protected Color disabledForeground;
	protected Color buttonBackground;
	protected Color buttonEditableBackground;
	protected Color buttonArrowColor;
	protected Color buttonDisabledArrowColor;

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

		focusWidth = UIManager.getInt("Component.focusWidth");
		arc = UIManager.getInt("Component.arc");
		borderColor = UIManager.getColor("Component.borderColor");
		disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
		disabledBackground = UIManager.getColor("ComboBox.disabledBackground");
		disabledForeground = UIManager.getColor("ComboBox.disabledForeground");
		buttonBackground = UIManager.getColor("ComboBox.buttonBackground");
		buttonEditableBackground = UIManager.getColor("ComboBox.buttonEditableBackground");
		buttonArrowColor = UIManager.getColor("ComboBox.buttonArrowColor");
		buttonDisabledArrowColor = UIManager.getColor("ComboBox.buttonDisabledArrowColor");

		padding = UIScale.scale(padding);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();
		borderColor = null;
		disabledBorderColor = null;
		disabledBackground = null;
		disabledForeground = null;
		buttonBackground = null;
		buttonEditableBackground = null;
		buttonArrowColor = null;
		buttonDisabledArrowColor = null;
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
	protected FocusListener createFocusListener() {
		return new BasicComboBoxUI.FocusHandler() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				if (comboBox != null && comboBox.isEditable()) comboBox.repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				if (comboBox != null && comboBox.isEditable()) comboBox.repaint();
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

		if (editor instanceof JTextComponent) {
			((JTextComponent) editor).setBorder(BorderFactory.createEmptyBorder());
		}

		updateEditorColors(); // Update editor colors initially
	}

	/**
	 * Updates the editor's background and foreground colors based on its enabled state.
	 */
	private void updateEditorColors() {
		boolean enabled = editor.isEnabled();
		editor.setBackground(nonUIResource(enabled ? comboBox.getBackground() : disabledBackground));
		editor.setForeground(nonUIResource((enabled || editor instanceof JTextComponent) ? comboBox.getForeground() : disabledForeground));
		if (editor instanceof JTextComponent)
			((JTextComponent) editor).setDisabledTextColor(nonUIResource(disabledForeground));
	}

	private Color nonUIResource(Color c) {
		return (c instanceof ColorUIResource) ? new Color(c.getRGB(), true) : c;
	}

	@Override
	protected JButton createArrowButton() {
		return new ZiziArrowButton(SwingConstants.SOUTH, buttonArrowColor, buttonDisabledArrowColor, null, null);
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			ZiziUIUtils.paintParentBackground(g, c);

			Graphics2D g2 = (Graphics2D) g;
			ZiziUIUtils.configureRenderingHints(g2);

			int width = c.getWidth();
			int height = c.getHeight();
			float focusWidth = (c.getBorder() instanceof ZiziBorder) ? scale((float) this.focusWidth) : 0;
			float arc = (c.getBorder() instanceof ZiziRoundBorder) ? scale((float) this.arc) : 0;
			int arrowX = arrowButton.getX();
			int arrowWidth = arrowButton.getWidth();
			boolean enabled = comboBox.isEnabled();
			boolean isLeftToRight = comboBox.getComponentOrientation().isLeftToRight();

			g2.setColor(enabled ? c.getBackground() : disabledBackground);
			ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, width, height, focusWidth, arc);

			if (enabled) {
				g2.setColor(comboBox.isEditable() ? buttonEditableBackground : buttonBackground);
				Shape oldClip = g2.getClip();
				if (isLeftToRight) g2.clipRect(arrowX, 0, width - arrowX, height);
				else g2.clipRect(0, 0, arrowX + arrowWidth, height);
				ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, width, height, focusWidth, arc);
				g2.setClip(oldClip);
			}

			if (comboBox.isEditable()) {
				g2.setColor(enabled ? borderColor : disabledBorderColor);
				float lw = scale(1f);
				float lx = isLeftToRight ? arrowX : arrowX + arrowWidth - lw;
				g2.fill(new Rectangle2D.Float(lx, focusWidth, lw, height - (focusWidth * 2)));
			}
		}

		paint(g, c); // Call superclass method for additional painting
	}

	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		ListCellRenderer<Object> renderer = comboBox.getRenderer();
		Component c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
		c.setFont(comboBox.getFont());
		boolean enabled = comboBox.isEnabled();
		c.setForeground(enabled ? comboBox.getForeground() : disabledForeground);
		c.setBackground(enabled ? comboBox.getBackground() : disabledBackground);
		boolean shouldValidate = (c instanceof JPanel);
		if (padding != null) bounds = ZiziUIUtils.subtract(bounds, padding);
		currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, shouldValidate);
	}

	@Override
	public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		g.setColor(comboBox.isEnabled() ? comboBox.getBackground() : disabledBackground);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
