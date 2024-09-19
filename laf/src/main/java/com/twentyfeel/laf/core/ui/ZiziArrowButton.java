// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * Button that draws a scaled arrow in one direction.
 */
public class ZiziArrowButton extends BasicArrowButton implements UIResource {
	private final Color foreground;
	private final Color disabledForeground;
	private final Color hoverForeground;
	private final Color hoverBackground;

	private int xOffset = 0;
	private int yOffset = 0;

	private boolean hover;

	public ZiziArrowButton(int direction, Color foreground, Color disabledForeground, Color hoverForeground, Color hoverBackground) {
		super(direction, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);

		this.foreground = foreground;
		this.disabledForeground = disabledForeground;
		this.hoverForeground = hoverForeground;
		this.hoverBackground = hoverBackground;

		setOpaque(false);
		setBorder(null);

		if (hoverForeground != null || hoverBackground != null) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					hover = true;
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					hover = false;
					repaint();
				}
			});
		}
	}

	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	@Override
	public Dimension getPreferredSize() {
		return scale(super.getPreferredSize());
	}

	@Override
	public Dimension getMinimumSize() {
		return scale(super.getMinimumSize());
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		ZiziUIUtils.configureRenderingHints(g2);

		int width = getWidth();
		int height = getHeight();
		boolean enabled = isEnabled();

		if (enabled && hover && hoverBackground != null) {
			g.setColor(hoverBackground);
			g.fillRect(0, 0, width, height);
		}

		boolean vert = (direction == NORTH || direction == SOUTH);

		int w = scale(9);
		int h = scale(5);
		int x = Math.round((width - (vert ? w : h)) / 2f + scale((float) xOffset));
		int y = Math.round((height - (vert ? h : w)) / 2f + scale((float) yOffset));


		g.setColor(enabled ? (hover && hoverForeground != null ? hoverForeground : foreground) : disabledForeground);
		g.translate(x, y);
		g2.fill(createArrowShape(direction, w, h));
		g.translate(-x, -y);
	}

	public static Shape createArrowShape(int direction, int w, int h) {
		Path2D arrow = new Path2D.Float();
		switch (direction) {
			case NORTH:
				arrow.moveTo(0, h);
				arrow.lineTo(w, h);
				arrow.lineTo((w / 2f), 0);
				arrow.closePath();
				break;
			case SOUTH:
				arrow.moveTo(0, 0);
				arrow.lineTo(w, 0);
				arrow.lineTo((w / 2f), h);
				arrow.closePath();
				break;
			case WEST:
				arrow.moveTo(h, 0);
				arrow.lineTo(h, w);
				arrow.lineTo(0, (w / 2f));
				arrow.closePath();
				break;
			case EAST:
				arrow.moveTo(0, 0);
				arrow.lineTo(0, w);
				arrow.lineTo(h, (w / 2f));
				arrow.closePath();
				break;
		}
		return arrow;
	}
}
