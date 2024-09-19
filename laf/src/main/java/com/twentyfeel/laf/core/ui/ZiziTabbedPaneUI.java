// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.*;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JTabbedPane}.
 *
 * @clientProperty JTabbedPane.hasFullBorder              boolean
 * @uiDefault TabbedPane.font                             Font
 * @uiDefault TabbedPane.background                       Color
 * @uiDefault TabbedPane.foreground                       Color
 * @uiDefault TabbedPane.shadow                           Color
 * @uiDefault TabbedPane.disabledForeground               Color
 * @uiDefault TabbedPane.selectedForeground               Color
 * @uiDefault TabbedPane.underlineColor                   Color
 * @uiDefault TabbedPane.disabledUnderlineColor           Color
 * @uiDefault TabbedPane.hoverColor                       Color
 * @uiDefault TabbedPane.focusColor                       Color
 * @uiDefault TabbedPane.contentAreaColor                 Color
 * @uiDefault TabbedPane.textIconGap                      int
 * @uiDefault TabbedPane.tabInsets                        Insets
 * @uiDefault TabbedPane.tabAreaInsets                    Insets
 * @uiDefault TabbedPane.tabHeight                        int
 * @uiDefault TabbedPane.tabSelectionHeight               int
 * @uiDefault TabbedPane.contentSeparatorHeight           int
 * @uiDefault TabbedPane.hasFullBorder                    boolean
 */
public class ZiziTabbedPaneUI extends BasicTabbedPaneUI {
	protected Color disabledForeground;
	protected Color selectedForeground;
	protected Color underlineColor;
	protected Color disabledUnderlineColor;
	protected Color hoverColor;
	protected Color focusColor;
	protected Color contentAreaColor;
	protected int tabHeight;
	protected int tabSelectionHeight;
	protected int contentSeparatorHeight;
	protected boolean hasFullBorder;
	protected boolean tabsOverlapBorder;

	/**
	 * Creates a new ZiziTabbedPaneUI instance.
	 *
	 * @param c the component for which this UI is being created
	 * @return a new ZiziTabbedPaneUI instance
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ZiziTabbedPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		disabledForeground = UIManager.getColor("TabbedPane.disabledForeground");
		selectedForeground = UIManager.getColor("TabbedPane.selectedForeground");
		underlineColor = UIManager.getColor("TabbedPane.underlineColor");
		disabledUnderlineColor = UIManager.getColor("TabbedPane.disabledUnderlineColor");
		hoverColor = UIManager.getColor("TabbedPane.hoverColor");
		focusColor = UIManager.getColor("TabbedPane.focusColor");
		contentAreaColor = UIManager.getColor("TabbedPane.contentAreaColor");
		tabHeight = UIManager.getInt("TabbedPane.tabHeight");
		tabSelectionHeight = UIManager.getInt("TabbedPane.tabSelectionHeight");
		contentSeparatorHeight = UIManager.getInt("TabbedPane.contentSeparatorHeight");
		hasFullBorder = UIManager.getBoolean("TabbedPane.hasFullBorder");

		textIconGap = scale(textIconGap);
		tabInsets = scale(tabInsets);
		selectedTabPadInsets = scale(selectedTabPadInsets);
		tabAreaInsets = scale(tabAreaInsets);
		tabHeight = scale(tabHeight);
		tabSelectionHeight = scale(tabSelectionHeight);
		contentSeparatorHeight = scale(contentSeparatorHeight);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();
		disabledForeground = null;
		selectedForeground = null;
		underlineColor = null;
		disabledUnderlineColor = null;
		hoverColor = null;
		focusColor = null;
		contentAreaColor = null;
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return new BasicTabbedPaneUI.PropertyChangeHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				super.propertyChange(e);
				if ("JTabbedPane.hasFullBorder".equals(e.getPropertyName())) {
					tabPane.revalidate();
					tabPane.repaint();
				}
			}
		};
	}

	@Override
	protected JButton createScrollButton(int direction) {
		return new ZiziArrowButton(direction, UIManager.getColor("TabbedPane.shadow"), UIManager.getColor("TabbedPane.disabledForeground"), null, UIManager.getColor("TabbedPane.hoverColor"));
	}

	@Override
	protected void setRolloverTab(int index) {
		int oldIndex = getRolloverTab();
		super.setRolloverTab(index);
		if (index == oldIndex) return;
		repaintTab(oldIndex);
		repaintTab(index);
	}

	private void repaintTab(int tabIndex) {
		if (tabIndex < 0 || tabIndex >= tabPane.getTabCount()) return;
		Rectangle r = getTabBounds(tabPane, tabIndex);
		if (r != null) tabPane.repaint(r);
	}

	@Override
	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
		return super.calculateTabWidth(tabPlacement, tabIndex, metrics) - 3 + (!isTopOrBottom(tabPlacement) && isScrollTabLayout() ? contentSeparatorHeight : 0);
	}

	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		return Math.max(tabHeight, super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) - 2) + (isTopOrBottom(tabPlacement) && isScrollTabLayout() ? contentSeparatorHeight : 0);
	}

	@Override
	protected Insets getContentBorderInsets(int tabPlacement) {
		boolean hasFullBorder = this.hasFullBorder || (tabPane.getClientProperty("JTabbedPane.hasFullBorder") == Boolean.TRUE);
		int sh = contentSeparatorHeight;
		Insets insets = hasFullBorder ? new Insets(sh, sh, sh, sh) : new Insets(sh, 0, 0, 0);
		if (isScrollTabLayout()) insets.top = 0;
		rotateInsets(insets, contentBorderInsets, tabPlacement);
		return contentBorderInsets;
	}

	@Override
	protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
		if (isScrollTabLayout() && !isTopOrBottom(tabPlacement)) {
			float shift = contentSeparatorHeight / 2f;
			return Math.round(tabPlacement == LEFT ? -shift : shift);
		} else return 0;
	}

	@Override
	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
		if (isScrollTabLayout() && isTopOrBottom(tabPlacement)) {
			float shift = contentSeparatorHeight / 2f;
			return Math.round(tabPlacement == TOP ? -shift : shift);
		} else return 0;
	}

	@Override
	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		if (isScrollTabLayout()) {
			Rectangle bounds = g.getClipBounds();
			g.setColor(contentAreaColor);
			if (tabPlacement == TOP || tabPlacement == BOTTOM) {
				int y = (tabPlacement == TOP) ? bounds.y + bounds.height - contentSeparatorHeight : bounds.y;
				g.fillRect(bounds.x, y, bounds.x + bounds.width, contentSeparatorHeight);
			} else {
				int x = (tabPlacement == LEFT) ? bounds.x + bounds.width - contentSeparatorHeight : bounds.x;
				g.fillRect(x, bounds.y, contentSeparatorHeight, bounds.y + bounds.height);
			}
		}
		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
		g.setFont(font);
		View view = getTextViewForTab(tabIndex);
		if (view != null) {
			view.paint(g, textRect);
			return;
		}

		Color color;
		if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
			color = tabPane.getForegroundAt(tabIndex);
			if (isSelected && (color instanceof UIResource) && selectedForeground != null) {
				color = selectedForeground;
			}
		} else {
			color = disabledForeground;
		}

		int mnemonicIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
		g.setColor(color);
		// Draw the string
		g.drawString(title, textRect.x, textRect.y + metrics.getAscent());

		// Draw the underline for the mnemonic character if present
		if (mnemonicIndex >= 0 && mnemonicIndex < title.length()) {
			int underlineX = textRect.x + metrics.stringWidth(title.substring(0, mnemonicIndex));
			int underlineY = textRect.y + metrics.getAscent() + 1;
			int underlineWidth = metrics.charWidth(title.charAt(mnemonicIndex));
			g.drawLine(underlineX, underlineY, underlineX + underlineWidth, underlineY);
		}
	}

	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		if (isScrollTabLayout()) {
			if (tabPlacement == TOP || tabPlacement == BOTTOM) {
				if (tabPlacement == BOTTOM) y += contentSeparatorHeight;
				h -= contentSeparatorHeight;
			} else {
				if (tabPlacement == RIGHT) x += contentSeparatorHeight;
				w -= contentSeparatorHeight;
			}
		}

		boolean enabled = tabPane.isEnabled();
		g.setColor(enabled && tabPane.isEnabledAt(tabIndex) && getRolloverTab() == tabIndex ? hoverColor : (enabled && isSelected && tabPane.hasFocus() ? focusColor : tabPane.getBackgroundAt(tabIndex)));
		g.fillRect(x, y, w, h);
	}

	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		if (!isSelected) return;
		g.setColor(tabPane.isEnabled() ? underlineColor : disabledUnderlineColor);
		Insets contentInsets = getContentBorderInsets(tabPlacement);

		switch (tabPlacement) {
			case TOP:
			default:
				int sy = y + h + contentInsets.top - tabSelectionHeight;
				g.fillRect(x, sy, w, tabSelectionHeight);
				break;
			case BOTTOM:
				g.fillRect(x, y - contentInsets.bottom, w, tabSelectionHeight);
				break;
			case LEFT:
				int sx = x + w + contentInsets.left - tabSelectionHeight;
				g.fillRect(sx, y, tabSelectionHeight, h);
				break;
			case RIGHT:
				g.fillRect(x - contentInsets.right, y, tabSelectionHeight, h);
				break;
		}
	}

	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
		if (tabPane.getTabCount() <= 0) return;

		Insets insets = tabPane.getInsets();
		Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

		int x = insets.left;
		int y = insets.top;
		int w = tabPane.getWidth() - insets.right - insets.left;
		int h = tabPane.getHeight() - insets.top - insets.bottom;

		switch (tabPlacement) {
			case LEFT:
				x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				if (tabsOverlapBorder) x -= tabAreaInsets.right;
				w -= (x - insets.left);
				break;
			case RIGHT:
				w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				if (tabsOverlapBorder) w += tabAreaInsets.left;
				break;
			case BOTTOM:
				h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				if (tabsOverlapBorder) h += tabAreaInsets.top;
				break;
			case TOP:
			default:
				y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				if (tabsOverlapBorder) y -= tabAreaInsets.bottom;
				h -= (y - insets.top);
		}

		g.setColor(contentAreaColor);
		g.fillRect(x, y, w, h);
	}

	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
	}

	private boolean isScrollTabLayout() {
		return tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT;
	}

	private boolean isTopOrBottom(int tabPlacement) {
		return tabPlacement == TOP || tabPlacement == BOTTOM;
	}
}
