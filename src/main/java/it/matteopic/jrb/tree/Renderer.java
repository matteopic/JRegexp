package it.matteopic.jrb.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import it.matteopic.jrb.Colors;
import net.amygdalum.regexparser.AlternativesNode;
import net.amygdalum.regexparser.AnyCharNode;
import net.amygdalum.regexparser.BoundedLoopNode;
import net.amygdalum.regexparser.CharClassNode;
import net.amygdalum.regexparser.ConcatNode;
import net.amygdalum.regexparser.GroupNode;
import net.amygdalum.regexparser.OptionalNode;
import net.amygdalum.regexparser.RangeCharNode;
import net.amygdalum.regexparser.SingleCharNode;
import net.amygdalum.regexparser.SpecialCharClassNode;
import net.amygdalum.regexparser.StringNode;
import net.amygdalum.regexparser.UnboundedLoopNode;

public class Renderer extends DefaultTreeCellRenderer {
	
	private Color BLUE = Colors.colors500[5];
	private Color GREEN = Colors.colors500[10];
	private Color ORANGE = Colors.colors500[14];
	
	MutableCharIcon mci;
	public Renderer() {
		mci = new MutableCharIcon();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		RegexTreeNode rtn = (RegexTreeNode)value;
		value = rtn.getRegexNode();
		//		RegexNode rn = (RegexNode)value;
		setIcon(mci);
		mci.setValue("");
		if(value instanceof AlternativesNode){
			AlternativesNode an = (AlternativesNode)value;
			label.setText("One of the followings");
			mci.setColor(BLUE);
			mci.setValue("|");
		}
		else if(value instanceof StringNode){
			StringNode an = (StringNode)value;
			label.setText("Match the characters \"" + an.getValue() + "\" letteraly");
			mci.setColor(BLUE);
		}
		else if(value instanceof RangeCharNode){
			RangeCharNode an = (RangeCharNode)value;
			label.setText("Any character in the range between \"" + an.getFrom() + "\" and \"" + an.getTo()+"\"");
			mci.setValue("a-z");
			mci.setColor(BLUE);
		}
		else if(value instanceof SingleCharNode){
			SingleCharNode cn = (SingleCharNode)value;
			label.setText("The character \""+ cn.getValue() +"\"");
			mci.setValue("a");
			mci.setColor(BLUE);
		}
		else if(value instanceof SpecialCharClassNode){
			SpecialCharClassNode cn = (SpecialCharClassNode)value;
			char symbol = cn.getSymbol();
			switch (symbol) {
			case '.':
				label.setText("Any character (may or may not match line terminators)");				
				break;
				
			case 'd':
				label.setText("A digit: [0-9]");
				break;
			
			case 'D':
				label.setText("A non digit: [^0-9]");
				break;
				
			case 's':
				label.setText("A whitespace character: [ \\t\\n\\x0B\\f\\r]");
				break;
			
			case 'S':
				label.setText("A non-whitespace character: [^\\s]");
				break;

			case 'w':
				label.setText("A word character: [a-zA-Z_0-9]");
				break;
				
			case 'W':
				label.setText("A non-word character: [^\\w]");
				break;
				
			default:
				label.setText("!!!SpecialCharClassNode...unkonw char " + symbol);
				break;
			}
			mci.setColor(BLUE);
			mci.setValue("\\w");
		}
		else if(value instanceof AnyCharNode){
			AnyCharNode cn = (AnyCharNode)value;
			label.setText("Any character");
			mci.setColor(BLUE);
			mci.setValue(".");
		}
		else if(value instanceof CharClassNode){
			CharClassNode cn = (CharClassNode)value;
			label.setText("A single character in class " + cn);
			mci.setColor(BLUE);
			mci.setValue("\\x");
		}
		else if(value instanceof ConcatNode){
			ConcatNode cn = (ConcatNode)value;
			mci.setColor(GREEN);
			label.setText("Match the regular expression below");
		}
		else if(value instanceof GroupNode){
			GroupNode cn = (GroupNode)value;
			mci.setColor(GREEN);
			mci.setValue("(1)");
			
			if(cn.isCapturingGroup()){
				int number = cn.getGroupNumber();
				label.setText("Match the regular expression below and capture its match into a backreference " + number);				
			}else{
				label.setText("Match the regular expression below");
			}
		}

		else if(value instanceof OptionalNode){
			OptionalNode cn = (OptionalNode)value;
			label.setText("Between 0 and one time");
			mci.setColor(ORANGE);
			mci.setValue("?");
		}		
		else if(value instanceof UnboundedLoopNode){
			UnboundedLoopNode cn = (UnboundedLoopNode)value;
			int from = cn.getFrom();
			label.setText("Between " + from + " and unlimited times");
			mci.setColor(ORANGE);
			mci.setValue("{n,}");
			//TODO Greedy or lazy
		}
		else if(value instanceof BoundedLoopNode){
			BoundedLoopNode cn = (BoundedLoopNode)value;
			int from = cn.getFrom();
			int to = cn.getTo();
			if(from != to){
				label.setText("Between " + from + " and " + to + " times");
				mci.setValue("{,}");
			}else{
				label.setText("Exactly " + from + " times");
				mci.setValue("{n}");
			}
			mci.setColor(ORANGE);
			//TODO Greedy or lazy
		}
		
		else{
			label.setText(value.getClass().getName());
		}

		return label;
	}
	
	private class MutableCharIcon implements Icon{

		private String data = "";
		private Color color;
		
		private float[] hsb = new float[3];
		private Font font;

		public MutableCharIcon(){
			font = new Font("Courier New", Font.PLAIN, 11);
			Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>(1);
			attributes.put(TextAttribute.TRACKING, -0.2);
			font = font.deriveFont(attributes);
		}

		public void setColor(Color color) {
			this.color = color;
		}
		public void setValue(String value){
			data = value;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			Font prevFont = g.getFont();
			g.setFont(font);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
			        RenderingHints.VALUE_ANTIALIAS_ON);

			//Background
//g2d.
//	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			GradientPaint gp = new GradientPaint(0, 0, color.brighter(), getIconWidth(), 0, color);
//	        g2d.setPaint(gp);

			g.setColor(color);
			g.fillRoundRect(0, 0, 15, 15, 3, 3);

//			Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
////			float luminosity = hsb[2] / 2f; 
			double br = Colors.getBrightness(color);
			if(br < 128){
				g.setColor(Color.WHITE);
			}else{
				g.setColor(Color.BLACK);				
			}
//			g.setColor(Color.BLACK);

			FontMetrics fm = g.getFontMetrics();
			int h =  fm.getAscent();
			double txtWidth = fm.getStringBounds(data, g).getWidth();
			g.drawString(data, (int)((16-txtWidth) / 2), h + 1);

			//External border
			g.setColor(Color.BLACK);
			g.drawRoundRect(0, 0, 15, 15, 3, 3);
			g.setFont(prevFont);
		}

		@Override
		public int getIconWidth() {
			return 16;
		}

		@Override
		public int getIconHeight() {
			return 16;
		}
		
	}

}
