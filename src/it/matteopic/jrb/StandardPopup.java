/**
 * PopupComposer.java
 *
 * Creato il 29/nov/07 12:30:53
 */
package it.matteopic.jrb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;

/**
 *
 * @author Matteo Piccinini
 */
public class StandardPopup extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = -6366739517427199680L;
//	private JTextPane textPane;

    public StandardPopup(){
        add(createCutMenuItem());
        add(createCopyMenuItem());
        add(createPasteMenuItem());
    }
    
    public JMenuItem createCutMenuItem(){
        JMenuItem cut = createItem("Taglia");
        cut.setActionCommand("cut");
        return cut;
    }
    
    public JMenuItem createCopyMenuItem(){
        JMenuItem copy = createItem("Copia");
        copy.setActionCommand("copy");
        return copy;
    }
    
    public JMenuItem createPasteMenuItem(){
        JMenuItem paste = createItem("Incolla");
        paste.setActionCommand("paste");
        return paste;
    }

    public void install(JTextPane tp, boolean includeMouseListener){
        
        if(includeMouseListener){
        tp.addMouseListener(new MouseAdapter() {
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
        }
    }

    

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
    	JTextPane invoker = (JTextPane)getInvoker();

        String command = e.getActionCommand();
        if("cut".equalsIgnoreCase(command)){
        	invoker.cut();
        }
        else if("copy".equalsIgnoreCase(command)){
        	invoker.copy();
        }
        else if("paste".equalsIgnoreCase(command)){
        	invoker.paste();
        }
    }
    
    public JMenuItem createItem(String label){
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(this);
        return item;
    }

}
