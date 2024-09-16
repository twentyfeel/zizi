// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.os.macos;

import com.twentyfeel.os.OSConfigurator;

public class MacOS implements OSConfigurator {
	@Override
	public void configure() {
		System.setProperty("apple.awt.application.name", "Zizi");
		System.setProperty("apple.awt.application.appearance", "system");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
	}
}
