// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import com.twentyfeel.laf.core.util.ZiziUIUtils;

import javax.swing.*;

import static com.twentyfeel.laf.core.util.UIScale.scale;

/**
 * A border with rounded corners for various components (e.g. {@link javax.swing.JComboBox}).
 * <p>
 * This class extends {@link ZiziBorder} to provide a rounded border by utilizing
 * the arc value defined in {@link ZiziUIUtils}.
 * </p>
 */
public class ZiziRoundBorder extends ZiziBorder {

	protected final int arc = UIManager.getInt("Component.arc");

	/**
	 * Returns the arc size for the rounded border.
	 * <p>
	 * This arc size is fetched from {@link ZiziUIUtils} to ensure consistency with
	 * other UI elements that use rounded borders.
	 * </p>
	 *
	 * @return the arc size for the rounded corners
	 */
	@Override
	protected float getArc() {
		return scale((float) arc);
	}
}
