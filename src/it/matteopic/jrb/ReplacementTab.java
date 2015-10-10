/**
 * ReplacementTab.java
 *
 * Creato il 29/nov/07 10:10:54
 */
package it.matteopic.jrb;

import it.matteopic.jrb.core.RegexpManager;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 *
 * @author Matteo Piccinini
 */
public class ReplacementTab extends RegexpPanel {

	private static final long serialVersionUID = -3136184685926548094L;
	private JTextPane replacementText;
    private JTextPane toReplaceText;
    private JTextPane replacedText;
    private JRegexp buddy;

    /**
     * @param name
     */
    public ReplacementTab(JRegexp b) {
        super("Sostituisci");
        this.buddy = b;

        ReplacementPopupComposer rpc = new ReplacementPopupComposer();
        replacementText = new JTextPane();
        rpc.install(replacementText);
        
        toReplaceText = new JTextPane();
        replacedText = new JTextPane();
        replacedText.setEditable(false);
        StandardPopup sp = new StandardPopup();

        sp.install(toReplaceText, true);
        sp.install(replacedText, true);

        JScrollPane replacementScroll = scroll(replacementText, "Regexp di sostituzione");
        JScrollPane toReplaceScroll = scroll(toReplaceText, "Testo");
        JScrollPane replacedScroll = scroll(replacedText, "Risultato");

        replacementText.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) { buddy.fireRegexpChanged(); }
            public void removeUpdate(DocumentEvent e) { buddy.fireRegexpChanged();  }
            public void changedUpdate(DocumentEvent e) {  }
        });

        toReplaceText.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) { buddy.fireRegexpChanged(); }
            public void removeUpdate(DocumentEvent e) { buddy.fireRegexpChanged();  }
            public void changedUpdate(DocumentEvent e) {  }
        });

        setLayout(new GridLayout(3,1));
        add(toReplaceScroll);
        add(replacementScroll);
        add(replacedScroll);
    }

    /* (non-Javadoc)
     * @see it.matteopic.jrb.core.RegexpProcessor#process(java.lang.String)
     */
    public void process(String regexp) {
        RegexpManager manager = getManager();
        manager.setRegexp(regexp);
        
        String replacement = replacementText.getText();
        String toReplace = toReplaceText.getText();
        String replaced = manager.replace(replacement, toReplace);
        replacedText.setText(replaced);
    }

    private JScrollPane scroll(JTextPane area, String label){
        JScrollPane scroll = new JScrollPane(area);
        
        Border border = BorderFactory.createTitledBorder(label);
        scroll.setBorder(border);
        return scroll;
    }
    
    
    

}
