// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JSpinner}.
 *
 * @uiDefault Component.focusWidth                int
 * @uiDefault Component.arc                       int
 * @uiDefault Component.borderColor               Color
 * @uiDefault Component.disabledBorderColor       Color
 * @uiDefault Spinner.disabledBackground          Color
 * @uiDefault Spinner.disabledForeground          Color
 * @uiDefault Spinner.buttonBackground            Color
 * @uiDefault Spinner.buttonArrowColor            Color
 * @uiDefault Spinner.buttonDisabledArrowColor    Color
 * @uiDefault Spinner.padding                     Insets
 */
public class ZiziSpinnerUI extends BasicSpinnerUI {
	private Handler handler;

	protected int focusWidth;
	protected int arc;
	protected Color borderColor;
	protected Color disabledBorderColor;
	protected Color disabledBackground;
	protected Color disabledForeground;
	protected Color buttonBackground;
	protected Color buttonArrowColor;
	protected Color buttonDisabledArrowColor;
	protected Insets padding;

	public static ComponentUI createUI(JComponent c) {
		return new ZiziSpinnerUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		focusWidth = UIManager.getInt("Component.focusWidth");
		arc = UIManager.getInt("Component.arc");
		borderColor = UIManager.getColor("Component.borderColor");
		disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
		disabledBackground = UIManager.getColor("Spinner.disabledBackground");
		disabledForeground = UIManager.getColor("Spinner.disabledForeground");
		buttonBackground = UIManager.getColor("Spinner.buttonBackground");
		buttonArrowColor = UIManager.getColor("Spinner.buttonArrowColor");
		buttonDisabledArrowColor = UIManager.getColor("Spinner.buttonDisabledArrowColor");
		padding = UIManager.getInsets("Spinner.padding");

		padding = scale(padding);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();

		borderColor = null;
		disabledBorderColor = null;
		disabledBackground = null;
		disabledForeground = null;
		buttonBackground = null;
		buttonArrowColor = null;
		buttonDisabledArrowColor = null;
		padding = null;
	}

	@Override
	protected void installListeners() {
		super.installListeners();

		addEditorFocusListener(spinner.getEditor());
		spinner.addPropertyChangeListener(getHandler());
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();

		removeEditorFocusListener(spinner.getEditor());
		spinner.removePropertyChangeListener(getHandler());

		handler = null;
	}

	public Handler getHandler() {
		if (handler == null) handler = new Handler();
		return handler;
	}

	@Override
	protected JComponent createEditor() {
		JComponent editor = super.createEditor();
		updateEditorColors();
		return editor;
	}

	@Override
	protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
		super.replaceEditor(oldEditor, newEditor);

		removeEditorFocusListener(oldEditor);
		addEditorFocusListener(newEditor);
		updateEditorColors();
	}

	private void addEditorFocusListener(JComponent editor) {
		JTextField textField = getEditorTextField(editor);
		if (textField != null) textField.addFocusListener(getHandler());
	}

	private void removeEditorFocusListener(JComponent editor) {
		JTextField textField = getEditorTextField(editor);
		if (textField != null) textField.removeFocusListener(getHandler());
	}

	private void updateEditorColors() {
		JTextField textField = getEditorTextField(spinner.getEditor());
		if (textField != null) {
			textField.setBackground(nonUIResource(spinner.isEnabled() ? spinner.getBackground() : disabledBackground));
			textField.setForeground(nonUIResource(spinner.getForeground()));
			textField.setDisabledTextColor(nonUIResource(disabledForeground));
		}
	}

	private Color nonUIResource(Color c) {
		return (c instanceof ColorUIResource) ? new Color(c.getRGB(), true) : c;
	}

	private JTextField getEditorTextField(JComponent editor) {
		return editor instanceof JSpinner.DefaultEditor ? ((JSpinner.DefaultEditor) editor).getTextField() : null;
	}

	@Override
	protected LayoutManager createLayout() {
		return getHandler();
	}

	@Override
	protected Component createNextButton() {
		return createArrowButton(SwingConstants.NORTH, "Spinner.nextButton");
	}

	@Override
	protected Component createPreviousButton() {
		return createArrowButton(SwingConstants.SOUTH, "Spinner.previousButton");
	}

	private Component createArrowButton(int direction, String name) {
		ZiziArrowButton button = new ZiziArrowButton(direction, buttonArrowColor, buttonDisabledArrowColor, null, null);
		button.setName(name);
		button.setYOffset((direction == SwingConstants.NORTH) ? 1 : -1);

		if (direction == SwingConstants.NORTH) {
			installNextButtonListeners(button);
		} else {
			installPreviousButtonListeners(button);
		}

		return button;
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
			Component nextButton = getHandler().nextButton;
			int arrowX = nextButton.getX();
			int arrowWidth = nextButton.getWidth();
			boolean enabled = spinner.isEnabled();
			boolean isLeftToRight = spinner.getComponentOrientation().isLeftToRight();

			g2.setColor(enabled ? c.getBackground() : disabledBackground);
			ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, width, height, focusWidth, arc);

			if (enabled) {
				g2.setColor(buttonBackground);
				Shape oldClip = g2.getClip();
				if (isLeftToRight) g2.clipRect(arrowX, 0, width - arrowX, height);
				else g2.clipRect(0, 0, arrowX + arrowWidth, height);
				ZiziUIUtils.fillRoundedRectangle(g2, 0, 0, width, height, focusWidth, arc);
				g2.setClip(oldClip);
			}

			g2.setColor(enabled ? borderColor : disabledBorderColor);
			float lw = scale(1f);
			float lx = isLeftToRight ? arrowX : arrowX + arrowWidth - lw;
			g2.fill(new Rectangle2D.Float(lx, focusWidth, lw, height - (focusWidth * 2)));
		}

		paint(g, c);
	}

	private class Handler implements LayoutManager, FocusListener, PropertyChangeListener {

		private Component editor = null;
		private Component nextButton;
		private Component previousButton;

		@Override
		public void addLayoutComponent(String name, Component c) {
			switch (name) {
				case "Editor":
					editor = c;
					break;
				case "Next":
					nextButton = c;
					break;
				case "Previous":
					previousButton = c;
					break;
			}
		}

		@Override
		public void removeLayoutComponent(Component c) {
			if (c == editor) editor = null;
			else if (c == nextButton) nextButton = null;
			else if (c == previousButton) previousButton = null;
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Insets insets = parent.getInsets();
			Dimension editorSize = (editor != null) ? editor.getPreferredSize() : new Dimension(0, 0);

			int innerHeight = editorSize.height + padding.top + padding.bottom;
			return new Dimension(insets.left + insets.right + editorSize.width + padding.left + padding.right + innerHeight, insets.top + insets.bottom + innerHeight);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		@Override
		public void layoutContainer(Container parent) {
			Dimension size = parent.getSize();
			Insets insets = parent.getInsets();
			Rectangle r = ZiziUIUtils.subtract(new Rectangle(size), insets);

			if (nextButton == null && previousButton == null) {
				if (editor != null) editor.setBounds(r);
				return;
			}

			Rectangle editorRect = new Rectangle(r);
			Rectangle buttonsRect = new Rectangle(r);

			int buttonsWidth = r.height;
			buttonsRect.width = buttonsWidth;

			if (parent.getComponentOrientation().isLeftToRight()) {
				editorRect.width -= buttonsWidth;
				buttonsRect.x += editorRect.width;
			} else {
				editorRect.x += buttonsWidth;
				editorRect.width -= buttonsWidth;
			}

			if (editor != null) editor.setBounds(ZiziUIUtils.subtract(editorRect, padding));

			int nextHeight = Math.round(buttonsRect.height / 2f);
			if (nextButton != null) nextButton.setBounds(buttonsRect.x, buttonsRect.y, buttonsRect.width, nextHeight);
			if (previousButton != null) {
				previousButton.setBounds(buttonsRect.x, buttonsRect.y + nextHeight, buttonsRect.width, buttonsRect.height - nextHeight);
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
			spinner.repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
			spinner.repaint();
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			switch (e.getPropertyName()) {
				case "background":
				case "foreground":
				case "enabled":
					updateEditorColors();
					break;
			}
		}
	}
}
