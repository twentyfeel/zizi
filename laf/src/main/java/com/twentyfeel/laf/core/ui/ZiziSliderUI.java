// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

import com.twentyfeel.laf.core.util.UIScale;
import com.twentyfeel.laf.core.util.ZiziUIUtils;

/**
 * Provides the Zizi LaF UI delegate for {@link javax.swing.JSlider}.
 *
 * @uiDefault Slider.font                    Font
 * @uiDefault Slider.background              Color
 * @uiDefault Slider.foreground              Color (unused)
 * @uiDefault Slider.disabledForeground      Color (used for track and thumb if disabled)
 * @uiDefault Slider.trackColor              Color
 * @uiDefault Slider.thumbColor              Color
 * @uiDefault Slider.tickColor               Color
 * @uiDefault Slider.trackWidth              int
 * @uiDefault Slider.thumbWidth              int
 * @uiDefault Slider.horizontalSize          Dimension (preferred horizontal size; height is ignored; computed slider height is used)
 * @uiDefault Slider.verticalSize            Dimension (preferred vertical size; width is ignored; computed slider width is used)
 * @uiDefault Slider.minimumHorizontalSize   Dimension (height is ignored; computed slider height is used)
 * @uiDefault Slider.minimumVerticalSize     Dimension (width is ignored; computed slider width is used)
 * @uiDefault Component.focusColor           Color
 */
public class ZiziSliderUI extends BasicSliderUI {
	private int trackWidth;
	private int thumbWidth;

	private Color trackColor;
	private Color thumbColor;
	private Color disabledForeground;
	private Color focusColor;

	public static ComponentUI createUI(JComponent c) {
		return new ZiziSliderUI();
	}

	public ZiziSliderUI() {
		super(null);
	}

	@Override
	protected void installDefaults(JSlider slider) {
		super.installDefaults(slider);

		trackWidth = UIManager.getInt("Slider.trackWidth");
		thumbWidth = UIManager.getInt("Slider.thumbWidth");

		trackColor = UIManager.getColor("Slider.trackColor");
		thumbColor = UIManager.getColor("Slider.thumbColor");
		disabledForeground = UIManager.getColor("Slider.disabledForeground");
		focusColor = UIManager.getColor("Component.focusColor");
	}

	@Override
	protected void uninstallDefaults(JSlider slider) {
		super.uninstallDefaults(slider);
		trackColor = null;
		thumbColor = null;
		disabledForeground = null;
		focusColor = null;
	}

	@Override
	public Dimension getPreferredHorizontalSize() {
		return UIScale.scale(super.getPreferredHorizontalSize());
	}

	@Override
	public Dimension getPreferredVerticalSize() {
		return UIScale.scale(super.getPreferredVerticalSize());
	}

	@Override
	public Dimension getMinimumHorizontalSize() {
		return UIScale.scale(super.getMinimumHorizontalSize());
	}

	@Override
	public Dimension getMinimumVerticalSize() {
		return UIScale.scale(super.getMinimumVerticalSize());
	}

	@Override
	protected int getTickLength() {
		return UIScale.scale(super.getTickLength());
	}

	@Override
	protected Dimension getThumbSize() {
		return new Dimension(UIScale.scale(thumbWidth), UIScale.scale(thumbWidth));
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		ZiziUIUtils.configureRenderingHints((Graphics2D) g);
		super.paint(g, c);
	}

	@Override
	public void paintFocus(Graphics g) {
		// Do not paint the dashed focus rectangle
	}

	@Override
	public void paintTrack(Graphics g) {
		boolean enabled = slider.isEnabled();

		float tw = UIScale.scale((float) trackWidth);
		float arc = tw;

		RoundRectangle2D coloredTrack = null;
		RoundRectangle2D track;
		if (slider.getOrientation() == JSlider.HORIZONTAL) {
			float y = trackRect.y + (trackRect.height - tw) / 2f;
			if (enabled && isRoundThumb()) {
				int cw = thumbRect.x + (thumbRect.width / 2) - trackRect.x;
				coloredTrack = new RoundRectangle2D.Float(trackRect.x, y, cw, tw, arc, arc);
				track = new RoundRectangle2D.Float(trackRect.x + cw, y, trackRect.width - cw, tw, arc, arc);
			} else track = new RoundRectangle2D.Float(trackRect.x, y, trackRect.width, tw, arc, arc);
		} else {
			float x = trackRect.x + (trackRect.width - tw) / 2f;
			if (enabled && isRoundThumb()) {
				int ch = thumbRect.y + (thumbRect.height / 2) - trackRect.y;
				track = new RoundRectangle2D.Float(x, trackRect.y, tw, ch, arc, arc);
				coloredTrack = new RoundRectangle2D.Float(x, trackRect.y + ch, tw, trackRect.height - ch, arc, arc);
			} else track = new RoundRectangle2D.Float(x, trackRect.y, tw, trackRect.height, arc, arc);
		}

		if (coloredTrack != null) {
			g.setColor(slider.hasFocus() ? focusColor : thumbColor);
			((Graphics2D) g).fill(coloredTrack);
		}

		g.setColor(enabled ? trackColor : disabledForeground);
		((Graphics2D) g).fill(track);
	}

	@Override
	public void paintThumb(Graphics g) {
		g.setColor(slider.hasFocus() ? focusColor : (slider.isEnabled() ? thumbColor : disabledForeground));

		if (isRoundThumb()) {
			g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
		} else {
			double w = thumbRect.width;
			double h = thumbRect.height;
			double wh = w / 2;

			Path2D thumb = new Path2D.Float(Path2D.WIND_NON_ZERO);
			thumb.moveTo(0, 0);
			thumb.lineTo(w, 0);
			thumb.lineTo(w, h - wh);
			thumb.lineTo(wh, h);
			thumb.lineTo(0, h - wh);
			thumb.closePath();

			Graphics2D g2 = (Graphics2D) g.create();
			try {
				g2.translate(thumbRect.x, thumbRect.y);
				if (slider.getOrientation() == JSlider.VERTICAL) {
					if (slider.getComponentOrientation().isLeftToRight()) {
						g2.translate(0, thumbRect.height);
						g2.rotate(Math.toRadians(270));
					} else {
						g2.translate(thumbRect.width, 0);
						g2.rotate(Math.toRadians(90));
					}
				}
				g2.fill(thumb);
			} finally {
				g2.dispose();
			}
		}
	}

	private boolean isRoundThumb() {
		return !slider.getPaintTicks() && !slider.getPaintLabels();
	}
}
