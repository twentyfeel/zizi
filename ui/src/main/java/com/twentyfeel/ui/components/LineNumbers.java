// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.ui.components;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LineNumbers extends JPanel {
	private JTextArea textArea;
	private int padding = 15;

	public LineNumbers(JTextArea textArea) {
		this.textArea = textArea;
		setBackground(textArea.getBackground());
		setForeground(textArea.getForeground());
		setFont(textArea.getFont());
	}

	public void setScrollPane(JScrollPane scrollPane) {
		scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> repaint());
		scrollPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				revalidate();
				repaint();
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getLineNumberWidth(), textArea.getHeight());
	}

	private int getLineNumberWidth() {
		int lineCount = textArea.getLineCount();
		int maxDigits = Math.max(3, String.valueOf(lineCount).length());
		return maxDigits * getFontMetrics(getFont()).charWidth('0') + 2 * padding;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		Rectangle clip = g2d.getClipBounds();
		int fontHeight = g2d.getFontMetrics().getHeight();
		int startOffset = textArea.viewToModel2D(new Point(0, clip.y));
		int endOffset = textArea.viewToModel2D(new Point(0, clip.y + clip.height));

		try {
			int startLine = textArea.getLineOfOffset(startOffset);
			int endLine = textArea.getLineOfOffset(endOffset);

			for (int line = startLine; line <= endLine; line++) {
				String lineNumber = String.valueOf(line + 1);
				int yText = textArea.modelToView2D(textArea.getLineStartOffset(line)).getBounds().y;
				// TODO: we will have problem with this prob
				int y = yText + fontHeight - 4;

				int stringWidth = g2d.getFontMetrics().stringWidth(lineNumber);
				int x = padding + (getWidth() - 2 * padding - stringWidth) / 2;

				g2d.drawString(lineNumber, x, y);
			}
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

	public void refresh() {
		revalidate();
		repaint();
	}
}
