// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.core;

import com.twentyfeel.laf.core.ZiziTwilightLaf;
import com.twentyfeel.laf.core.util.PlatformInfo;
import com.twentyfeel.ui.EditorWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args) {
		PlatformInfo platformInfo = PlatformInfo.getInstance();
		PlatformInfo.OSType osType = platformInfo.getOSType();

		System.out.println("Operating System: " + platformInfo.getOSName());
		System.out.println("OS Version: " + platformInfo.getOSVersion());
		System.out.println("OS Architecture: " + platformInfo.getOSArch());
		System.out.println("Detected OS Type: " + osType);

		if (PlatformInfo.IS_WINDOWS) {
			System.out.println("Running on Windows");
		} else if (PlatformInfo.IS_MAC) {
			System.out.println("Running on macOS");
		} else if (PlatformInfo.IS_LINUX) {
			System.out.println("Running on Linux");
		}

		try {
			UIManager.setLookAndFeel(new ZiziTwilightLaf());
		} catch (UnsupportedLookAndFeelException laf) {
			laf.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			JFrame window = new JFrame();
			EditorWindow editorWindow = new EditorWindow();

			window.add(editorWindow);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setResizable(true);
			window.setMinimumSize(new Dimension(800, 600));
			window.pack();
			window.setLocationRelativeTo(null);
			window.setVisible(true);

			editorWindow.startEditorThread();
		});

	}
}
