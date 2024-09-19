// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.util;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class ZiziUIUtils {

	// Flag to determine if Quartz graphics should be used on macOS
	public static final boolean USE_QUARTZ_GRAPHICS = Boolean.getBoolean("apple.awt.graphics.UseQuartz");

	/**
	 * Subtracts the given insets from the specified rectangle.
	 *
	 * @param r      the rectangle to be adjusted
	 * @param insets the insets to subtract from the rectangle
	 * @return a new rectangle with the adjusted dimensions
	 */
	public static Rectangle subtract(Rectangle r, Insets insets) {
		return new Rectangle(r.x + insets.left, r.y + insets.top, r.width - insets.left - insets.right, r.height - insets.top - insets.bottom);
	}

	/**
	 * Adjusts the given rectangle by the specified insets.
	 *
	 * @param rect   the rectangle to be adjusted
	 * @param insets the insets to be subtracted from the rectangle
	 * @return a new rectangle with adjusted dimensions
	 */
	public static Rectangle adjustRectangle(Rectangle rect, Insets insets) {
		return new Rectangle(rect.x + insets.left, rect.y + insets.top, rect.width - insets.left - insets.right, rect.height - insets.top - insets.bottom);
	}

	/**
	 * Retrieves a UI color from the UIManager or returns a default color if not found.
	 *
	 * @param key        the UIManager key for the color
	 * @param defaultRGB the default color in RGB format if not found
	 * @return the color associated with the key or the default color
	 */
	public static Color getUIColor(String key, int defaultRGB) {
		Color color = UIManager.getColor(key);
		return (color != null) ? color : new Color(defaultRGB);
	}

	/**
	 * Retrieves an integer value from the UIManager or returns a default value if not found.
	 *
	 * @param key          the UIManager key for the integer value
	 * @param defaultValue the default value if the key is not found
	 * @return the integer value associated with the key or the default value
	 */
	public static int getUIInt(String key, int defaultValue) {
		Object value = UIManager.get(key);
		return (value instanceof Integer) ? (Integer) value : defaultValue;
	}

	/**
	 * Sets rendering hints for the provided Graphics2D object.
	 *
	 * @param graphics the Graphics2D object to configure
	 */
	public static void configureRenderingHints(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, USE_QUARTZ_GRAPHICS ? RenderingHints.VALUE_STROKE_PURE : RenderingHints.VALUE_STROKE_NORMALIZE);
	}

	/**
	 * Draws a rounded rectangle with focus and line width.
	 *
	 * @param graphics   the Graphics2D object to draw on
	 * @param x          the x-coordinate of the rectangle
	 * @param y          the y-coordinate of the rectangle
	 * @param width      the width of the rectangle
	 * @param height     the height of the rectangle
	 * @param focusWidth the width of the focus indicator
	 * @param lineWidth  the width of the borderline
	 * @param arc        the arc size for the rounded corners
	 */
	public static void drawRoundedRectangle(Graphics2D graphics, int x, int y, int width, int height, float focusWidth, float lineWidth, float arc) {
		float adjustedArc = arc > lineWidth ? arc - lineWidth : 0f;

		RoundRectangle2D.Float outerRectangle = new RoundRectangle2D.Float(x + focusWidth, y + focusWidth, width - focusWidth * 2, height - focusWidth * 2, arc, arc);
		RoundRectangle2D.Float innerRectangle = new RoundRectangle2D.Float(outerRectangle.x + lineWidth, outerRectangle.y + lineWidth, outerRectangle.width - lineWidth * 2, outerRectangle.height - lineWidth * 2, adjustedArc, adjustedArc);

		Path2D borderPath = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		borderPath.append(outerRectangle, false);
		borderPath.append(innerRectangle, false);
		graphics.fill(borderPath);
	}

	/**
	 * Fills a rounded rectangle with focus width.
	 *
	 * @param graphics   the Graphics2D object to fill
	 * @param x          the x-coordinate of the rectangle
	 * @param y          the y-coordinate of the rectangle
	 * @param width      the width of the rectangle
	 * @param height     the height of the rectangle
	 * @param focusWidth the width of the focus indicator
	 * @param arc        the arc size for the rounded corners
	 */
	public static void fillRoundedRectangle(Graphics2D graphics, int x, int y, int width, int height, float focusWidth, float arc) {
		graphics.fill(new RoundRectangle2D.Float(x + focusWidth, y + focusWidth, width - focusWidth * 2, height - focusWidth * 2, arc, arc));
	}

	/**
	 * Paints the background of the component's parent to handle focus decoration.
	 *
	 * @param graphics  the Graphics object to paint with
	 * @param component the component whose background is to be painted
	 */
	public static void paintParentBackground(Graphics graphics, JComponent component) {
		Container opaqueParent = findOpaqueParent(component);
		if (opaqueParent != null) {
			graphics.setColor(opaqueParent.getBackground());
			graphics.fillRect(0, 0, component.getWidth(), component.getHeight());
		}
	}

	/**
	 * Finds the first opaque parent of the given component.
	 *
	 * @param component the component to search from
	 * @return the first opaque parent or null if none found
	 */
	private static Container findOpaqueParent(Container component) {
		while ((component = component.getParent()) != null) {
			if (component.isOpaque()) {
				return component;
			}
		}
		return null;
	}

	/**
	 * Paints an outline border with a focus width and line width.
	 *
	 * @param graphics   the Graphics2D object to draw on
	 * @param x          the x-coordinate of the outline
	 * @param y          the y-coordinate of the outline
	 * @param width      the width of the outline
	 * @param height     the height of the outline
	 * @param focusWidth the width of the focus indicator
	 * @param lineWidth  the width of the borderline
	 * @param arc        the arc size for the rounded corners
	 */
	public static void paintOutlineBorder(Graphics2D graphics, int x, int y, int width, int height, float focusWidth, float lineWidth, float arc) {
		float xEnd = (float) x + width;
		float yEnd = (float) y + height;

		float outerArc = arc > 0 ? arc + focusWidth - UIScale.scale(2f) : focusWidth;
		Path2D outerOutline = createOutlinePath((float) x, (float) y, xEnd, yEnd, outerArc);

		float offsetWidth = focusWidth + lineWidth;
		Path2D innerOutline = createOutlinePath((float) x + offsetWidth, (float) y + offsetWidth, xEnd - offsetWidth, yEnd - offsetWidth, outerArc - offsetWidth);

		Path2D outlinePath = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		outlinePath.append(outerOutline, false);
		outlinePath.append(innerOutline, false);
		graphics.fill(outlinePath);
	}

	/**
	 * Creates a path for an outlined rectangle with rounded corners.
	 *
	 * @param x1  the x-coordinate of the starting point
	 * @param y1  the y-coordinate of the starting point
	 * @param x2  the x-coordinate of the ending point
	 * @param y2  the y-coordinate of the ending point
	 * @param arc the arc size for the rounded corners
	 * @return a Path2D object representing the outline path
	 */
	private static Path2D createOutlinePath(float x1, float y1, float x2, float y2, float arc) {
		Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		path.moveTo(x2 - arc, y1);
		path.quadTo(x2, y1, x2, y1 + arc);
		path.lineTo(x2, y2 - arc);
		path.quadTo(x2, y2, x2 - arc, y2);
		path.lineTo(x1 + arc, y2);
		path.quadTo(x1, y2, x1, y2 - arc);
		path.lineTo(x1, y1 + arc);
		path.quadTo(x1, y1, x1 + arc, y1);
		path.closePath();
		return path;
	}
}
