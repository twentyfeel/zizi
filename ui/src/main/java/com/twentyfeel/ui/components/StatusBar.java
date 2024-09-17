// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.ui.components;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
	private static final int PADDING = 10;

	private JLabel positionLabel;
	private JLabel fpsLabel;

	public StatusBar() {
		setLayout(new BorderLayout());

		positionLabel = new JLabel("Line 1, Column 1");
		positionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, PADDING));

		fpsLabel = new JLabel("FPS 0");
		fpsLabel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, 0));

		add(fpsLabel, BorderLayout.WEST);
		add(positionLabel, BorderLayout.EAST);
	}

	public void updateStatus(int line, int col) {
		positionLabel.setText("Line " + line + ", Column " + col);
	}

	public void updateFps(int fps) {
		fpsLabel.setText("FPS " + fps);
	}
}
