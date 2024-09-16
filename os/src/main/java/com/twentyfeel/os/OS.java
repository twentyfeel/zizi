// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.os;

import com.twentyfeel.os.linux.Linux;
import com.twentyfeel.os.macos.MacOS;
import com.twentyfeel.os.windows.Windows;

import java.util.Locale;

public class OS {
	private static final OS INSTANCE = new OS();

	private final String name;
	private final String version;
	private final String arch;
	private final OSType type;

	private OS() {
		this.name = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		this.version = System.getProperty("os.version");
		this.arch = System.getProperty("os.arch");
		this.type = determineType(name);
	}

	public enum OSType {
		WINDOWS, LINUX, MACOS, UNKNOWN
	}

	private OSType determineType(String name) {
		if (name.startsWith("mac")) {
			return OSType.MACOS;
		}
		if (name.startsWith("win")) {
			return OSType.WINDOWS;
		}
		if (name.contains("nix") || name.contains("nux")) {
			return OSType.LINUX;
		}
		return OSType.UNKNOWN;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getArch() {
		return arch;
	}

	public OSType getType() {
		return type;
	}

	public boolean isAarch64() {
		return "aarch64".equals(arch) || "arm64".equals(arch);
	}

	public boolean isMac() {
		return type == OSType.MACOS;
	}

	public boolean isWindows() {
		return type == OSType.WINDOWS;
	}

	public boolean isLinux() {
		return type == OSType.LINUX;
	}

	public boolean isUnix() {
		return type == OSType.LINUX || type == OSType.MACOS;
	}

	public static OS getInstance() {
		return INSTANCE;
	}

	public OSConfigurator getConfigurator() {
		return switch (type) {
			case WINDOWS -> new Windows();
			case LINUX -> new Linux();
			case MACOS -> new MacOS();
			default -> null;
		};
	}
}
