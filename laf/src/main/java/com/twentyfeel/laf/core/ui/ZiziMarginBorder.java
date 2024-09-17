// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import static com.twentyfeel.laf.core.util.UIScale.scale;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.plaf.basic.BasicBorders;

/**
 * A border that scales the component margin according to the UIScale utility.
 * This border adjusts the insets of a component by scaling them, which helps maintain consistent margins across different screen resolutions and sizes.
 */
public class ZiziMarginBorder extends BasicBorders.MarginBorder {

	/**
	 * Gets the insets of the border, scaling them according to the UIScale utility.
	 *
	 * @param c      the component for which the insets are being queried
	 * @param insets the object to be filled with the border insets
	 * @return the insets of the border, scaled appropriately
	 */
	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets = super.getBorderInsets(c, insets);
		insets.top = scale(insets.top);
		insets.left = scale(insets.left);
		insets.bottom = scale(insets.bottom);
		insets.right = scale(insets.right);
		return insets;
	}
}
