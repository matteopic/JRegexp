package it.matteopic.jrb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Icon;

public class StringIcon implements Icon {

	private String txt;
	private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	private int iconWidth, iconHeight;

	public StringIcon(char txt) {
		this( Character.toString(txt) );
	}

	public StringIcon(String txt) {
		this.txt = txt;
		iconHeight = 12;
		iconWidth = 24;
	}

	@Override
	public int getIconWidth() {
		return iconWidth;
	}

	@Override
	public int getIconHeight() {
		return iconHeight;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g = g.create();
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
//		int txtWidth = fm.stringWidth(txt);
//		x = (iconWidth - txtWidth) / 2;
		y = fm.getHeight();
		g.setColor(Color.DARK_GRAY);
		g.drawString(txt, x, y);
//		g.drawRect(0, 0, 12, 12);
//		g.setColor(color);
//		g.fillRect(1, 1, 11, 11);
	}
}
