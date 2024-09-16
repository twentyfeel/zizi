// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.ui;

import javax.swing.*;
import java.awt.*;

public class EditorWindow extends JPanel implements Runnable {

	public EditorWindow() {
		this.setPreferredSize(new Dimension(1280, 720));

		this.setDoubleBuffered(true);
	}

	Thread editorThread;

	public void startEditorThread() {
		editorThread = new Thread(this);

		editorThread.start();
	}

	@Override
	public void run() {
		while (editorThread != null) {
//			System.out.println("Editor thread running");
		}
	}
}
