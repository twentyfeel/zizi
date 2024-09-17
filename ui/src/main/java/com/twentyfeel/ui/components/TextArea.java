// Copyright (C) 2024 Twentyfeel and contributors. Use of this source code is governed by the Apache License, Version 2.0, that can be found in the LICENSE file.
package com.twentyfeel.ui.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class TextArea extends JPanel {
	private JTextArea textArea;
	private LineNumbers lineNumbers;
	private JScrollPane textScrollPane;

	public TextArea() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();

		// CONFIG
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setMargin(new Insets(1, 1, 1, 1));
		textArea.setFont(new Font("IBM Plex Mono", Font.PLAIN, 14));
		int tabSize = 4;
		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, tabSize);

		lineNumbers = new LineNumbers(textArea);
		textScrollPane = new JScrollPane(textArea);
		textScrollPane.setBorder(null);
		textScrollPane.setRowHeaderView(lineNumbers);
		lineNumbers.setScrollPane(textScrollPane);

		add(textScrollPane, BorderLayout.CENTER);

		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				lineNumbers.refresh();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				lineNumbers.refresh();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lineNumbers.refresh();
			}
		});
	}

	public JTextArea getTextArea() {
		return textArea;
	}
}
