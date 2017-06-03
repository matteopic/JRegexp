package it.matteopic.jrb;

import it.matteopic.jrb.core.RegexpManager;
import it.matteopic.jrb.tree.RegexTree;
import net.amygdalum.regexparser.EmptyNode;
import net.amygdalum.regexparser.RegexNode;
import net.amygdalum.regexparser.RegexParser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
	private RegexTree tree;

	

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
        
        tree = new RegexTree(new EmptyNode());
        JScrollPane scrollTree = new JScrollPane(tree);
        JSplitPane bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, scroll);
        bottomSplit.setDividerLocation(150);
        
		setLayout(new BorderLayout());
		add(tools, BorderLayout.NORTH);
		add(bottomSplit, BorderLayout.CENTER);
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
		
		RegexParser parser = new RegexParser(regexp);
		RegexNode node = parser.parse();
		tree.set(node);
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
		int colorIndex = groupIndex % Colors.colors500.length;
		Color color = Colors.colors500[colorIndex];
		double b = Colors.getBrightness(color);

		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.ColorConstants.Background, color);
		sas.addAttribute(StyleConstants.ColorConstants.Foreground, b > 140 ? new Color(0x212121) : Color.WHITE);
		return sas;
	}

	private AttributeSet getAttributeForGroup(int groupIndex) {
		int colorIndex = groupIndex % Colors.colors900.length;
		Color color = Colors.colors900[colorIndex];
		double b = Colors.getBrightness(color);

		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.ColorConstants.Background, color);
		sas.addAttribute(StyleConstants.ColorConstants.Foreground, b > 140 ? new Color(0x212121) : Color.WHITE);

		// Border border = BorderFactory.createLineBorder(Color.black);
		// JLabel jl = new JLabel();
		// jl.setBorder(border);
		// StyleConstants.setComponent(sas, jl);
		return sas;
	}


	private JLabel resultLabel;
	private JTextPane textPane;
	private JToolBar tools;
	private JRegexp buddy;
}
