/**
 * PopupComposer.java
 *
 * Creato il 29/nov/07 12:30:53
 */
package it.matteopic.jrb;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author Matteo Piccinini
 */
public class PopupComposer extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 7232073341644184108L;
	private JTextPane textPane;
    private StandardPopup sp;
    private static final String CURSOR_PLACEHOLDER = "${cursor}";

    public PopupComposer(){
        JMenuItem literalText = createItem("Testo...");
        literalText.setActionCommand("literal.text");
        add( literalText );
        add( makeCharactersMenu() );
        add( makeCharactersClass() );
        add( makeBoundary() );
        add( makeLookAround() );
        add(new JSeparator());
        
        sp = new StandardPopup();
        add(sp.createCutMenuItem());
        add(sp.createCopyMenuItem());
        add(sp.createPasteMenuItem());
        
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
        
        textPane.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == ' ' && e.isControlDown()){
					try{
						JTextPane tp = (JTextPane)e.getComponent();
						int dot = tp.getCaret().getDot();
						int lineHeight = tp.getFontMetrics(tp.getFont()).getHeight();
			            Rectangle caretCoords = tp.modelToView(dot);
						show(tp, (int)caretCoords.getX(), (int)caretCoords.getY() + lineHeight);
					}catch(BadLocationException ignroed){}	
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
        
        sp.install(tp, false);
    }

    public JMenu makeCharactersMenu(){
        JMenu characters = new JMenu("Caratteri invisibili");

        JMenuItem tabChar = createItem("Tabulazione");
        tabChar.setActionCommand("INSERT_\\t");

        JMenuItem returnCarrChar = createItem("Return Carriage");
        returnCarrChar.setActionCommand("INSERT_\\r");

        JMenuItem lineFeedChar = createItem("Line Feed");
        lineFeedChar.setActionCommand("INSERT_\\n");

        JMenuItem windowsEndlLineChar = createItem("CR LF (Fine riga Windows)");
        windowsEndlLineChar.setActionCommand("INSERT_\\r\\n");

        JMenuItem bellChar = createItem("Bell");
        bellChar.setActionCommand("INSERT_\\a");

        JMenuItem escapeChar = createItem("Escape");
        escapeChar.setActionCommand("INSERT_\\e");

        JMenuItem formFeedChar = createItem("Form Feed");
        formFeedChar.setActionCommand("INSERT_\\f");

        JMenuItem verticalTabChar = createItem("Vertical Tab");
        verticalTabChar.setActionCommand("INSERT_\\v");

        characters.add(tabChar);
        characters.add(returnCarrChar);
        characters.add(lineFeedChar);
        characters.add(windowsEndlLineChar);
        characters.add(new JPopupMenu.Separator());
        characters.add(bellChar);
        characters.add(escapeChar);
        characters.add(formFeedChar);
        characters.add(verticalTabChar);
        return characters;
    }

    public JMenu makeLookAround(){
        JMenu lookaround = new JMenu("Guardati intorno");

        JMenuItem posLookAhed = createItem("Guarda avanti se uguale");
        posLookAhed.setActionCommand("INSERT_(?="+CURSOR_PLACEHOLDER+")");
        
        JMenuItem negLookAhed = createItem("Guarda avanti se diverso");
        negLookAhed.setActionCommand("INSERT_(?!"+CURSOR_PLACEHOLDER+")");
        
        JMenuItem posLookBehind = createItem("Guarda dietro se uguale");
        posLookBehind.setActionCommand("INSERT_(?<="+CURSOR_PLACEHOLDER+")");

        JMenuItem negLookBehind = createItem("Guarda dietro se diverso");
        negLookBehind.setActionCommand("INSERT_(?<!"+CURSOR_PLACEHOLDER+")");

        lookaround.add(posLookAhed);
        lookaround.add(negLookAhed);
        lookaround.add(posLookBehind);
        lookaround.add(negLookBehind);
        return lookaround;
    }
    
    public JMenu makeCharactersClass(){
        JMenu charClasses = new JMenu("Classi di caratteri");

        JMenuItem lowcaseChars = createItem("Lettere minuscole");
        lowcaseChars.setActionCommand("INSERT_[a-z]");
        
        JMenuItem upcaseChars = createItem("Lettere maiuscole");
        upcaseChars.setActionCommand("INSERT_[A-Z]");
        
        JMenuItem allChars = createItem("Qualsiasi carattere");
        allChars.setActionCommand("INSERT_.");

        JMenuItem digit = createItem("Una cifra [0-9]");
        digit.setActionCommand("INSERT_\\d");
        
        JMenuItem notDigit = createItem("Non una cifra [^0-9]");
        notDigit.setActionCommand("INSERT_\\D");

        charClasses.add(lowcaseChars);
        charClasses.add(upcaseChars);
        charClasses.add(allChars);
        charClasses.add(digit);
        charClasses.add(notDigit);
        return charClasses;
    }
    
    public JMenu makeBoundary(){
        JMenu borders = new JMenu("Bordi");

        JMenuItem initRow = createItem("Inizio della riga");
        initRow.setActionCommand("INSERT_^");

        JMenuItem endRow = createItem("Fine della riga");
        endRow.setActionCommand("INSERT_$");
        
        JMenuItem wordBoundary = createItem("Bordo della parola");
        wordBoundary.setActionCommand("INSERT_\\b");

        JMenuItem notWordBoundary = createItem("Bordo di una non-parola");
        notWordBoundary.setActionCommand("INSERT_\\B");
        
        JMenuItem inputStart = createItem("Inizio del testo");
        inputStart.setActionCommand("INSERT_\\A");

        JMenuItem inputEnd = createItem("Fine del testo");
        inputEnd.setActionCommand("INSERT_\\Z");
        
        borders.add(initRow);
        borders.add(endRow);
        borders.add(wordBoundary);
        borders.add(notWordBoundary);
        borders.add(inputStart);
        borders.add(inputEnd);
        return borders;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if( command.equals("literal.text")){
            String ret = JOptionPane.showInputDialog(this,
                    "Inserisci il testo che la regexp deve corrispondere",
                    "Inserisci il testo",
                    JOptionPane.INFORMATION_MESSAGE);
            if(ret != null){
            	ret = escapeText(ret);
            	command = "INSERT_" + ret;
            }
        }


        if(command.startsWith("INSERT_")){
            Document doc = textPane.getDocument();
            if (doc == null)return;
            int pos = textPane.getCaretPosition();
            String insert = command.substring(7);
            int cursorIndex = insert.indexOf(CURSOR_PLACEHOLDER);

            //Se presente rimuovo il placeholder del cursore
            if (cursorIndex >= 0){
            	String selectedText = textPane.getSelectedText();
            	StringBuilder sb = new StringBuilder();
            	if(selectedText != null){
            		sb.append(insert.substring(0, cursorIndex));
            		sb.append(selectedText);
            		sb.append(insert.substring(cursorIndex + CURSOR_PLACEHOLDER.length()));
            		textPane.replaceSelection(sb.toString());
            	}else{
                    sb.append(insert);
                    sb.delete(cursorIndex, cursorIndex + CURSOR_PLACEHOLDER.length());
                    try {
                        doc.insertString(pos, sb.toString(), null);
                        if(cursorIndex > 0)textPane.setCaretPosition(pos + cursorIndex);
                    } catch (BadLocationException e1) {}
            	}
            }
            //Se non � presente il placeholder del cursore, lo aggiungo nel punto in cui si trova
            else{
            	 try {
                     doc.insertString(pos, insert, null);
                 } catch (BadLocationException e1) {}
            }
        }
    }
    
    public JMenuItem createItem(String label){
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(this);
        return item;
    }
    
    private String escapeText(CharSequence txt){
    	StringBuilder sb = new StringBuilder();
    	int length = txt.length();
    	for(int i = 0; i < length; i++){
    		char c = txt.charAt(i);
    		switch (c) {
			case '[':
			case ']':
			case '\\':
			case '(':
			case ')':
			case '.':
			case '^':
			case '$':
			case '?':
			case '*':
			case '+':
			case '{':
			case '}':
			case '|':
				sb.append('\\').append(c);
				break;

			default:
				sb.append(c);
				break;
			}
    	}
    	return sb.toString();
    }

}
