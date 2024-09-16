// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.core;

import com.twentyfeel.os.OS;
import com.twentyfeel.os.OSConfigurator;
import com.twentyfeel.ui.EditorWindow;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		OS os = OS.getInstance();

		OS.OSType osType = os.getType();

		System.out.println("Operating System: " + os.getName());
		System.out.println("OS Version: " + os.getVersion());
		System.out.println("OS Architecture: " + os.getArch());
		System.out.println("Detected OS Type: " + osType);

		OSConfigurator configurator = os.getConfigurator();
		if (configurator != null) {
			configurator.configure();
		}

		JFrame window = new JFrame();

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		window.setResizable(true);

		window.setTitle("Zizi");

		EditorWindow editorWindow = new EditorWindow();

		window.add(editorWindow);

		window.pack();

		window.setLocationRelativeTo(null);

		window.setVisible(true);

		editorWindow.startEditorThread();
	}
}
