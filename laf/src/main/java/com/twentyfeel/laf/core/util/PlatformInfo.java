// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.laf.core.util;

import java.util.Locale;
import java.util.StringTokenizer;

/**
 * PlatformInfo provides information about the operating system and Java runtime environment.
 * It determines the OS type, version, architecture, and other system properties.
 */
public class PlatformInfo {

	private static final PlatformInfo INSTANCE = new PlatformInfo();

	public static final boolean IS_JAVA_9_OR_LATER;
	public static final boolean IS_JETBRAINS_JVM;
	public static final boolean IS_WINDOWS;
	public static final boolean IS_MAC;
	public static final boolean IS_LINUX;

	private final String osName;
	private final String osVersion;
	private final String osArch;
	private final OSType osType;

	static {
		String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		IS_WINDOWS = osName.startsWith("windows");
		IS_MAC = osName.startsWith("mac");
		IS_LINUX = osName.startsWith("linux");

		int javaVersion = parseJavaVersion(System.getProperty("java.version"));
		IS_JAVA_9_OR_LATER = javaVersion >= versionToInt(9, 0, 0, 0);

		IS_JETBRAINS_JVM = System.getProperty("java.vm.vendor", "Unknown").toLowerCase(Locale.ENGLISH).contains("jetbrains");
	}

	private PlatformInfo() {
		this.osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		this.osVersion = System.getProperty("os.version");
		this.osArch = System.getProperty("os.arch");
		this.osType = determineOSType(osName);
	}

	/**
	 * Enumeration for representing the type of operating system.
	 */
	public enum OSType {
		WINDOWS, LINUX, MACOS, UNKNOWN
	}

	private OSType determineOSType(String osName) {
		if (osName.startsWith("mac")) return OSType.MACOS;
		if (osName.startsWith("win")) return OSType.WINDOWS;
		if (osName.contains("nix") || osName.contains("nux")) return OSType.LINUX;
		return OSType.UNKNOWN;
	}

	public String getOSName() {
		return osName;
	}

	public String getOSVersion() {
		return osVersion;
	}

	public String getOSArch() {
		return osArch;
	}

	public OSType getOSType() {
		return osType;
	}

	public boolean isAarch64() {
		return "aarch64".equals(osArch) || "arm64".equals(osArch);
	}

	public boolean isUnix() {
		return IS_MAC || IS_LINUX;
	}

	public static PlatformInfo getInstance() {
		return INSTANCE;
	}

	private static int parseJavaVersion(String version) {
		int major = 1;
		int minor = 0;
		int micro = 0;
		int patch = 0;
		try {
			StringTokenizer tokenizer = new StringTokenizer(version, "._-+");
			major = Integer.parseInt(tokenizer.nextToken());
			minor = Integer.parseInt(tokenizer.nextToken());
			micro = Integer.parseInt(tokenizer.nextToken());
			patch = Integer.parseInt(tokenizer.nextToken());
		} catch (Exception ex) {
			// Ignore parsing errors and use defaults
		}

		return versionToInt(major, minor, micro, patch);
	}

	private static int versionToInt(int major, int minor, int micro, int patch) {
		return (major << 24) + (minor << 16) + (micro << 8) + patch;
	}
}
