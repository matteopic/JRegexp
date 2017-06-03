package it.matteopic.jrb;

import java.awt.Color;

public class Colors {

	// http://www.google.com/design/spec/style/color.html#color-color-palette
		public static Color[] colors500 = new Color[] { new Color(0xF44336), // Red
				new Color(0xE91E63), // Pink
				new Color(0x9C27B0), // Purple
				new Color(0x673AB7), // Deep Purple
				new Color(0x3F51B5), // Indigo
				new Color(0x2196F3), // Blue
				new Color(0x03A9F4), // Light Blue
				new Color(0x00BCD4), // Cyan
				new Color(0x009688), // Teal
				new Color(0x4CAF50), // Green
				new Color(0x8BC34A), // Light Green
				new Color(0x8BC34A), // Lime
				new Color(0xFFEB3B), // Yellow
				new Color(0xFFC107), // Amber
				new Color(0xFF9800), // Orange
				new Color(0xFF5722), // Deep Orange
				new Color(0x795548), // Brown
				new Color(0x9E9E9E), // Grey
				new Color(0x607D8B), // Blue Grey
		};

		public static Color[] colors900 = new Color[] { new Color(0xB71C1C), // Red
				new Color(0x880E4F), // Pink
				new Color(0x4A148C), // Purple
				new Color(0x311B92), // Deep Purple
				new Color(0x1A237E), // Indigo
				new Color(0x0D47A1), // Blue
				new Color(0x01579B), // Light Blue
				new Color(0x006064), // Cyan
				new Color(0x004D40), // Teal
				new Color(0x1B5E20), // Green
				new Color(0x33691E), // Light Green
				new Color(0x827717), // Lime
				new Color(0xF57F17), // Yellow
				new Color(0xFF6F00), // Amber
				new Color(0xE65100), // Orange
				new Color(0xBF360C), // Deep Orange
				new Color(0x3E2723), // Brown
				new Color(0x212121), // Grey
				new Color(0x263238), // Blue Grey
		};
		
		public static double getBrightness(Color color) {
			return ((color.getRed() * 299) + (color.getGreen() * 587) + (color.getBlue() * 114)) / 1000;
		}
}
