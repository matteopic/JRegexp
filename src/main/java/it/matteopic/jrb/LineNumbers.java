package it.matteopic.jrb;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class LineNumbers extends JComponent {

	private static final long serialVersionUID = 1323961031433479593L;

	public LineNumbers(JTextPane editor) {
		this.editor = editor;
		countLines();
		initDocumentListener(editor);
	}

	private void countLines() {
		String text = editor.getText();
		int charsCount = text.length();
		rows = 0;
		for(int i = 0; i < charsCount; i++){
			switch (text.charAt(i)) {
			case '\n':
				rows++;
				break;
			}
		}
		rows++;
	}

	private void initDocumentListener(JEditorPane editor) {
		DocumentPropertyListener dpl = new DocumentPropertyListener();
		Document doc = editor.getDocument();
		doc.addDocumentListener(dpl);
	}

	private void fireTextChanged() {
		countLines();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		FontMetrics fontMetrics = editor.getGraphics().getFontMetrics();
		int width = fontMetrics.stringWidth(String.valueOf(rows));
		return new Dimension(width + MARGIN_LEFT + MARGIN_RIGHT,  editor.getHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		Rectangle drawHere = g.getClipBounds();
		g.setColor(editor.getBackground());
		g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

		g.setColor(GREY);
//		g.drawLine(drawHere.width - 1, 0, drawHere.width - 1, getHeight());

		g.setFont(editor.getFont());
		Insets insets = editor.getInsets();
		String text = null;
		FontMetrics metrics = g.getFontMetrics();
		int height = metrics.getHeight();
		int ascent = metrics.getAscent();

		int value = 0;
		int txtWidth;
		for (int i = 0; i < rows; i++) {
			value = i + 1;
			text = Integer.toString(value);
			txtWidth = metrics.stringWidth(text);
			g.drawString(text, drawHere.width - txtWidth - MARGIN_RIGHT, (i * height) + ascent + insets.top);
		}
	}

	private class DocumentPropertyListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			// fireTextChanged();
		}

		public void insertUpdate(DocumentEvent e) {
			fireTextChanged();
		}

		public void removeUpdate(DocumentEvent e) {
			fireTextChanged();
		}
	}

	private int rows;
	private JTextPane editor;
	private static final Color GREY = new Color(128, 128, 128);
	private static final int MARGIN_RIGHT = 3;
	private static final int MARGIN_LEFT = 6;
}
