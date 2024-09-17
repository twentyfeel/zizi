// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.ui;

import javax.swing.plaf.BorderUIResource;

/**
 * Provides an empty border for various components.
 * This border has zero thickness on all sides, effectively providing no visible border.
 */
public class ZiziEmptyBorder extends BorderUIResource.EmptyBorderUIResource {

	/**
	 * Creates a new instance of ZiziEmptyBorder with zero thickness on all sides.
	 */
	public ZiziEmptyBorder() {
		super(0, 0, 0, 0);
	}
}
