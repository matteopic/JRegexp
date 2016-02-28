/**
 * ReplacementTab.java
 *
 * Creato il 29/nov/07 10:10:54
 */
package it.matteopic.jrb;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import it.matteopic.jrb.core.RegexpManager;


/**
 *
 * @author Matteo Piccinini
 */
public class SplitTab extends RegexpPanel {

	private static final long serialVersionUID = -3136184685926548094L;
	private JTextPane toSplitText;
    private JTextPane splitText;
    private JRegexp jregexp;

    /**
     * @param name
     */
    public SplitTab(JRegexp b) {
        super("Dividi");
        this.jregexp = b;

        ReplacementPopupComposer rpc = new ReplacementPopupComposer();
        toSplitText = new JTextPane();
        rpc.install(toSplitText);
        

        splitText = new JTextPane();
        splitText.setEditable(false);
        
        StandardPopup sp = new StandardPopup();
        sp.install(splitText, true);

        JScrollPane toSplitScroll = scroll(toSplitText, "Text to split");
        JScrollPane resultScroll = scroll(splitText, "Result");

        toSplitText.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) { jregexp.fireRegexpChanged(); }
            public void removeUpdate(DocumentEvent e) { jregexp.fireRegexpChanged();  }
            public void changedUpdate(DocumentEvent e) {  }
        });

        setLayout(new GridLayout(2,1));
        add(toSplitScroll);
        add(resultScroll);
        resetStyle(toSplitText);
        resetStyle(splitText);
    }

    /* (non-Javadoc)
     * @see it.matteopic.jrb.core.RegexpProcessor#process(java.lang.String)
     */
    public void process(String regexp) {
        RegexpManager manager = getManager();
        manager.setRegexp(regexp);

        String replacement = toSplitText.getText();
        String[] split = manager.split(replacement);
        StringBuilder sb = new StringBuilder( replacement.length() );
        for(String line : split){
        	if(sb.length() > 0)sb.append('\n');
        	sb.append(line);
        }
        splitText.setText(sb.toString());
    }

    private JScrollPane scroll(JTextPane area, String label){
        JScrollPane scroll = new JScrollPane(area);

        Border border = BorderFactory.createTitledBorder(label);
        scroll.setBorder(border);

        scroll.setRowHeaderView(new LineNumbers(area));

        return scroll;
    }

}
