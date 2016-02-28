package it.matteopic.jrb;

import it.matteopic.jrb.core.RegexpManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Matteo Piccinini
 */
public class TestTab extends RegexpPanel {

	private static final long serialVersionUID = 3257852069162530866L;

	// http://www.google.com/design/spec/style/color.html#color-color-palette
	private Color[] colors500 = new Color[] { new Color(0xF44336), // Red
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

	private Color[] colors900 = new Color[] { new Color(0xB71C1C), // Red
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

	public TestTab(JRegexp b) {
		super("Test");
		this.buddy = b;

		String version = getClass().getPackage().getImplementationVersion();
		textPane = new JTextPane();
		textPane.setText("Welcome to JRegp v" + version + " by matteopic!\r\n" + "\r\n" + "Sample text for testing:\r\n"
				+ "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\n"
				+ "0123456789 _+-.,!@#$%^&*();\\/|<>\"'\r\n" + "12345 -98.7 3.141 .6180 9,000 +42\r\n"
				+ "555.123.4567	+1-(800)-555-2468\r\n" + "foo@demo.net	bar.ba@test.co.uk\r\n"
				+ "www.demo.com	http://foo.co.uk/\r\n" + "https://github.com/matteopic/JRegexp?param=value");
		textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		// textPane.setTransferHandler(new FileTransferHandler());
		Document doc = textPane.getDocument();
		doc.addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				buddy.fireRegexpChanged();
			}

			public void removeUpdate(DocumentEvent e) {
				buddy.fireRegexpChanged();
			}

			public void changedUpdate(DocumentEvent e) {
			}
		});
		new StandardPopup().install(textPane, true);

		tools = new JToolBar();
		tools.setFloatable(false);

		resultLabel = new JLabel();
		resultLabel.setBorder( BorderFactory.createEmptyBorder(0,10,0,0) );

		final JToggleButton hilight = new JToggleButton("Higlight");
		hilight.getModel().setSelected(true);
		hilight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hilight.getModel().isSelected()) {
					buddy.fireRegexpChanged();
				} else {
					resetStyle(textPane);
				}
			}
		});
		tools.add(hilight);
		tools.add(resultLabel);

		Border borderTest = BorderFactory.createTitledBorder("Testing String");
		setBorder(borderTest);

		JScrollPane scroll = new JScrollPane(textPane);
        scroll.setRowHeaderView(new LineNumbers(textPane));

		setLayout(new BorderLayout());
		add(tools, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
	}

	public void setText(String txt) {
		textPane.setText(txt);
		textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
	}

	public String getText() {
		return textPane.getText();
	}

	public void process(String regexp) {
		RegexpManager manager = getManager();
		manager.setRegexp(regexp);
		resetStyle(textPane);

		String textToTest = getText();
		Matcher matcher = manager.startMatching(textToTest);

		int resultCount = 0;
		for (int i = 0; matcher.find(); i++) {
			resultCount++;
			MatchResult result = matcher.toMatchResult();
			process(i, result);
		}

		resultLabel.setText( String.format("%s matches", resultCount));
	}

	public void process(int index, MatchResult result) {
		StyledDocument doc = textPane.getStyledDocument();
		AttributeSet attributes = getAttributeForMatch(index);
		AttributeSet grAttributes = getAttributeForGroup(index);

		int start = result.start();
		int end = result.end();
		doc.setCharacterAttributes(start, end - start, attributes, false);

		// System.out.println("----------------------------------");
		// System.out.println("Start: " + result.start());
		// System.out.println("End " + result.end());
		// System.out.println("Value: " + result.group(0));
		int groups = result.groupCount();
		// System.out.println("Groups count " + groups);
		for (int i = 1; i <= groups; i++) {
			// System.out.println(" Group " + i);
			// System.out.println(" Start : " + result.start(i));
			// System.out.println(" End : " + result.end(i));
			// System.out.println(" Value : " + result.group(i));

			start = result.start(i);
			end = result.end(i);

			// JLabel jl = (JLabel)StyleConstants.getComponent(grAttributes);
			// jl.setText( result.group(i) );

			doc.setCharacterAttributes(start, end - start, grAttributes, false);
		}
	}

	private AttributeSet getAttributeForMatch(int groupIndex) {
		int colorIndex = groupIndex % colors500.length;
		Color color = colors500[colorIndex];
		double b = getBrightness(color);

		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.ColorConstants.Background, color);
		sas.addAttribute(StyleConstants.ColorConstants.Foreground, b > 140 ? new Color(0x212121) : Color.WHITE);
		return sas;
	}

	private AttributeSet getAttributeForGroup(int groupIndex) {
		int colorIndex = groupIndex % colors900.length;
		Color color = colors900[colorIndex];
		double b = getBrightness(color);

		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.ColorConstants.Background, color);
		sas.addAttribute(StyleConstants.ColorConstants.Foreground, b > 140 ? new Color(0x212121) : Color.WHITE);

		// Border border = BorderFactory.createLineBorder(Color.black);
		// JLabel jl = new JLabel();
		// jl.setBorder(border);
		// StyleConstants.setComponent(sas, jl);
		return sas;
	}

	private double getBrightness(Color color) {
		return ((color.getRed() * 299) + (color.getGreen() * 587) + (color.getBlue() * 114)) / 1000;
	}

	private JLabel resultLabel;
	private JTextPane textPane;
	private JToolBar tools;
	private JRegexp buddy;
}
