/**
 * JakartaRegexp.java
 *
 * Creato il 20/nov/07 12:47:53
 */
package it.matteopic.jrb;

import it.matteopic.jrb.core.RegexpManager;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.OverlayLayout;
import javax.swing.border.Border;

/**
 * 
 * @author Matteo Piccinini
 */
public class JakartaRegexpTab extends RegexpPanel {

	private static final long serialVersionUID = -6399018153312271664L;
	private JTextPane textPane;

    public JakartaRegexpTab(JRegexp b) {
        super("Jakarta Regexp");
//        this.buddy = b;
        textPane = new JTextPane();
        textPane.setEditable(false);
        new StandardPopup().install(textPane, true);

        Border borderJakarta = BorderFactory
        .createTitledBorder("Jakarta Regexp Precompiled Code");
        JScrollPane scrollJakartaPane = new JScrollPane(textPane);
        scrollJakartaPane.setBorder(borderJakarta);

        setLayout(new OverlayLayout(this));
        add(scrollJakartaPane);
    }

    public void process(String regexp) {
        try {
            RegexpManager manager = getManager();
            manager.setRegexp(regexp);
            String txt = manager.jakartaRegexpPrecompile("subjectString");
            textPane.setText(txt);
        } catch (Exception e) {
            textPane.setText("Malformed Regexp");
        }
    }

//    private JRegexp buddy;
}
