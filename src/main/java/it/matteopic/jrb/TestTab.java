/**
 * TestEditor.java
 *
 * Creato il 27-lug-2005 18.38.20
 */
package it.matteopic.jrb;

import it.matteopic.jrb.core.Match;
import it.matteopic.jrb.core.RegexpManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Matteo Piccinini
 */
public class TestTab extends RegexpPanel {

    private static final long serialVersionUID = 3257852069162530866L;

    public TestTab(JRegexp b) {
        super("Test");
        this.buddy = b;
        defaultAttributes = new SimpleAttributeSet();

        
        textPane = new JTextPane();
        textPane.setText("Ma la volpe col suo balzo ha raggiunto il quieto Fido");
        //textPane.setTransferHandler(new FileTransferHandler());
        textPane.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) { buddy.fireRegexpChanged(); }
            public void removeUpdate(DocumentEvent e) { buddy.fireRegexpChanged();  }
            public void changedUpdate(DocumentEvent e) {  }
        });
        new StandardPopup().install(textPane, true);

        
        tools = new JToolBar();
        tools.setFloatable(false);


        final JToggleButton hilight = new JToggleButton("Hilight");
        hilight.getModel().setSelected(true);
        hilight.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(hilight.getModel().isSelected()){
                    buddy.fireRegexpChanged();
                }else{
                	List<Match>empty = Collections.emptyList();
                    setMatchList(empty);
                }
            }
        });
        tools.add(hilight);


//      Font f = regexpEditor.getFont();
//      int height = regexpEditor.getFontMetrics(f).getHeight();
//      scrollEditor.getVerticalScrollBar().setBlockIncrement(3 * height);

        Border borderTest = BorderFactory.createTitledBorder("Testing String");
        setBorder(borderTest);

        JScrollPane scroll = new JScrollPane(textPane);
        setLayout(new BorderLayout());
        add(tools, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void setText(String txt){
        textPane.setText(txt);
    }
    
    public String getText(){
        return textPane.getText();
    }

    public void setMatchList(List<Match> matches){
        String text = textPane.getText();
        StyledDocument doc = textPane.getStyledDocument();
        doc.setCharacterAttributes(0, text.length(), defaultAttributes, true);
        
        Match match;
        int size = matches.size();
        int offset, length;
        for(int i = 0; i < size; i++){
            match = (Match)matches.get(i);
            offset = match.getStart();
            length = match.getEnd() - offset;
            doc.setCharacterAttributes(offset, length, getAttributeForGroup(i), true);    
        }
    }

    
    /* (non-Javadoc)
     * @see it.matteopic.jrb.RegexpProcessor#process(java.lang.String)
     */
    public void process(String regexp) {
        RegexpManager manager = getManager();
        manager.setRegexp(regexp);
        List<Match> matchList = manager.process(getText());
        setMatchList(matchList);
    }

    private AttributeSet getAttributeForGroup(int groupIndex){
    	// http://www.materialdesigncolors.com/?ckattempt=1
        Color[]colors = new Color[]{
                new Color(0x8bc34a), //Light Green 
                new Color(0xffeb3b), //Yellow
                new Color(0xffc107), //Amber
                new Color(0xff5722), //Deep Orange
                new Color(0xe91e63), //Pink
                new Color(0x259b24), //Green
                new Color(0xcddc39), //Lime
                new Color(0xff9800), //Orange
                new Color(0xe51c23), //Red
                new Color(0x9c27b0), //Purple
                new Color(0xffeb3b), //Indigo
                new Color(0x03a9f4), //Light Blue
                new Color(0x00bcd4), //Cyan
                new Color(0x9e9e9e), //Grey
                new Color(0x607d8b), //Blue Grey
                new Color(0x673ab7), //Deep Purple
                new Color(0x5677fc), //Blue
                new Color(0x009688), //Teal
                new Color(0x795548), //Brown
                //new Color(0x212121), //Text Black     	
        };
        int colorIndex = groupIndex % colors.length;
        
        SimpleAttributeSet sas = new SimpleAttributeSet();
        sas.addAttribute(StyleConstants.FontConstants.Bold, Boolean.FALSE);
        sas.addAttribute(StyleConstants.ColorConstants.Background, colors[colorIndex]);
        sas.addAttribute(StyleConstants.ColorConstants.Foreground, new Color(0x212121));
        return sas;
    }

    private AttributeSet defaultAttributes;
    private JTextPane textPane;
    private JToolBar tools;
    private JRegexp buddy;
}
