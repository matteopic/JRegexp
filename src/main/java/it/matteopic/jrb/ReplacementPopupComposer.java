package it.matteopic.jrb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ReplacementPopupComposer extends JPopupMenu implements
		ActionListener {

	private static final long serialVersionUID = -755831608967846705L;
	private StandardPopup sp;
	private JTextPane textPane;

	public ReplacementPopupComposer(){
		add(makeSpecialChars());

		add(new JSeparator());

		sp = new StandardPopup();
        add(sp.createCutMenuItem());
        add(sp.createCopyMenuItem());
        add(sp.createPasteMenuItem());
	}

	private JMenu makeSpecialChars(){
		JMenu menu = new JMenu("Caratteri speciali");

		JMenuItem insertMatch = createItem("Selezione");
		insertMatch.setActionCommand("$0");
				
		JMenuItem insertBacksapce = createItem("\\");
		insertBacksapce.setActionCommand("\\\\");

		JMenuItem insertDollar = createItem("$");
		insertDollar.setActionCommand("\\$");
		
		menu.add(insertMatch);
		menu.add(insertBacksapce);
		menu.add(insertDollar);
		return menu;
	}
	
    public void install(JTextPane tp){
        this.textPane = tp;
        textPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        sp.install(tp, false);
    }
    
    public JMenuItem createItem(String label){
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(this);
        return item;
    }

	public void actionPerformed(ActionEvent e) {
		 String insert = e.getActionCommand();
		 Document doc = textPane.getDocument();
		 if (doc == null)return;

		 try {
			 int pos = textPane.getCaretPosition();
             doc.insertString(pos, insert, null);
         } catch (BadLocationException e1) {}

	}

}
