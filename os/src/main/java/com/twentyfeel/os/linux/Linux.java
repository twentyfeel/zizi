// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.os.linux;

import com.twentyfeel.os.OSConfigurator;

public class Linux implements OSConfigurator {
	@Override
	public void configure() {
		System.out.println("Linux");
	}
}
