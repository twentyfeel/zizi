// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.ui;

import com.twentyfeel.ui.components.StatusBar;
import com.twentyfeel.ui.components.TextArea;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class EditorWindow extends JPanel implements Runnable {
	private static final double SCREEN_WIDTH_RATIO = 0.55;
	private static final double SCREEN_HEIGHT_RATIO = 0.9;
	private static final int STATUS_BAR_HEIGHT = 25;
	private static final int TARGET_FPS = 120;
	private static final long FRAME_TIME = 1000000000 / TARGET_FPS;

	private final int screenWidth;
	private final int screenHeight;
	private TextArea textArea;
	private StatusBar statusBar;

	private Thread editorThread;
	private volatile boolean running = false;
	private volatile boolean needsUpdate = false;
	private static long lastFpsCheck = 0;
	private static int currentFps = 0;
	private static int totalFrames = 0;

	public EditorWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) (screenSize.width * SCREEN_WIDTH_RATIO);
		screenHeight = (int) (screenSize.height * SCREEN_HEIGHT_RATIO);

		initializeUI();
		setupListeners();
	}

	private void initializeUI() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setLayout(new BorderLayout());

		textArea = new TextArea();
		statusBar = new StatusBar();

		add(textArea, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		statusBar.setPreferredSize(new Dimension(screenWidth, STATUS_BAR_HEIGHT));

		// Focus CodeEditorArea on start
		SwingUtilities.invokeLater(() -> textArea.getTextArea().requestFocusInWindow());
	}

	private void setupListeners() {
		textArea.getTextArea().addCaretListener(this::updateStatusBar);
		textArea.getTextArea().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				needsUpdate = true;
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				needsUpdate = true;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				needsUpdate = true;
			}
		});
	}

	private void updateStatusBar(CaretEvent e) {
		SwingUtilities.invokeLater(() -> {
			try {
				int dot = e.getDot();
				int line = dot == 0 ? 1 : textArea.getTextArea().getLineOfOffset(dot) + 1;
				int col = dot - textArea.getTextArea().getLineStartOffset(line - 1) + 1;
				statusBar.updateStatus(line, col);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		});
	}

	public void startEditorThread() {
		if (editorThread == null || !editorThread.isAlive()) {
			editorThread = new Thread(this);
			running = true;
			editorThread.start();
		}
	}

	public void stopEditorThread() {
		running = false;
	}

	@Override
	public void run() {
		long lastFrame = System.nanoTime();

		// TODO: do I need this?
		while (running) {
			long now = System.nanoTime();
			long elapsed = now - lastFrame;

			if (elapsed >= FRAME_TIME) {
				totalFrames++;
				if (now > lastFpsCheck + 1000000000) {
					lastFpsCheck = now;
					currentFps = totalFrames;
					totalFrames = 0;

					update();
					repaint();
					System.out.println("FPS: " + currentFps);
					SwingUtilities.invokeLater(() -> statusBar.updateFps(currentFps));
				}

				lastFrame = now - (elapsed % FRAME_TIME);
			} else {
				long sleepTime = (FRAME_TIME - elapsed) / 1000000;
				try {
					Thread.sleep(sleepTime > 0 ? sleepTime : 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void update() {
		// Future usage: add any logic that needs to be updated each frame
		if (needsUpdate) {
			// Perform any necessary updates
			needsUpdate = false;
		}

		// Uncomment this to test FPS under load
		// TODO: this will do nothing because we already capped FPS to 120 from 700-800
		// for (int i = 0; i < 1000000; i++) {
		// Math.sin(i);
		// }
	}

	public TextArea getTextArea() {
		return textArea;
	}
}
