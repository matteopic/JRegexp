/**
 * JRegexpBuddy.java
 *
 * Creato il 27-lug-2005 18.14.53
 */
package it.matteopic.jrb;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Matteo Piccinini
 */
public class JRegexp extends JFrame {

    private static final long serialVersionUID = 3257852086443390261L;
    public JRegexp() {
    	String version = getClass().getPackage().getImplementationVersion();
        setTitle("JRegexp " + version);
//        manager = new RegexpManager();

        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                initGUI();
            } 
        });
    }

    private void initGUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints constr;

        Container cont = getContentPane();
        cont.setLayout(new GridBagLayout());

        tabbed = new Tabs(this);
        
        //RegexpEditor
        regexpEditor = new JTextPane();
        regexpEditor.setText("([A-Z])\\w+");
        regexpEditor.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) { fireRegexpChanged(); }
            public void removeUpdate(DocumentEvent e) { fireRegexpChanged(); }
            public void changedUpdate(DocumentEvent e) {  }
        });
        //regexpEditor.setTransferHandler(new FileTransferHandler());
        new PopupComposer().install(regexpEditor);

        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JScrollPane scrollEditor = new JScrollPane(regexpEditor);
        Border borderRegexp = BorderFactory.createTitledBorder("Regular Expression");
        scrollEditor.setBorder(borderRegexp);

        Insets insets = new Insets(5,5,5,5);
        constr = new GridBagConstraints();
        constr.weightx = 1;
        constr.weighty = 1;
        constr.insets = insets;
        constr.fill = GridBagConstraints.BOTH;
        cont.add(split, constr);

        constr = new GridBagConstraints();
        constr.gridx = GridBagConstraints.RELATIVE;
        constr.gridy = 0;
        constr.anchor = GridBagConstraints.NORTH;
        cont.add(getChecks(), constr);

        split.setTopComponent(scrollEditor);
        split.setBottomComponent(tabbed);

        CheckboxListener cbl = new CheckboxListener();
        caseInsensitive.addActionListener(cbl);
        multiline.addActionListener(cbl);
        dotall.addActionListener(cbl);
        unicodeCase.addActionListener(cbl);
        canonEq.addActionListener(cbl);
        unix.addActionListener(cbl);
        comments.addActionListener(cbl);
        useAnchoringBounds.addActionListener(cbl);
        useAnchoringBounds.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                unix.setEnabled(useAnchoringBounds.getModel().isSelected());
            }
        });
        fireRegexpChanged();
        
        ResolutionIndipendence.setCentimeterComponentSize(this, 20, 15);
        //setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
        split.setDividerLocation(0.5);
    }

    protected void fireRegexpChanged() {
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        tabbed.setRegexp(regexpEditor.getText());
                    }
                }
        );
    }
//        if(tabbed.getSelectedIndex() == 0)
//            performTest();
//        else
//            compileForJakarta();
//    }



//    public void compileForJakarta(){
//        try{
//            manager.setRegexp(regexpEditor.getText());
//            String txt = manager.jakartaRegexpPrecompile("subjectString");
//            jakartaPane.setText(txt);
//        }catch(Exception e){
//            jakartaPane.setText("Malformed Regexp");
//        }
//    }

    
    private Container getChecks(){
        JPanel checks = new JPanel(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();
        constr.anchor = GridBagConstraints.WEST;
        constr.gridx = 0;
        constr.gridy = GridBagConstraints.RELATIVE;

        caseInsensitive = new JCheckBox("Case insensitive");
        caseInsensitive.setToolTipText(
        		"<html>By default, case-insensitive matching assumes that only<br>" +
        		"characters in the US-ASCII charset are being matched.<br>" +
        		"Unicode-aware case-insensitive matching can be enabled<br>" +
        		"by specifying the UNICODE_CASE flag in conjunction with this flag.</html>");
        checks.add(caseInsensitive, constr);

        
        multiline = new JCheckBox("Multiline");
        multiline.setToolTipText(
        		"<html>In multiline mode the expressions ^ and $<br>" +
        		"match just after or just before, respectively,<br> " +
        		"a line terminator or the end of the input sequence.<br>" +
        		"By default these expressions only match at the <br>" +
        		"beginning and the end of the entire input sequence.</html>");
        
        checks.add(multiline, constr);

        dotall = new JCheckBox("Single-line/Dotall");
        dotall.setToolTipText(
        		"<html>Se abilitata il testo viene considerato come<br>" +
        		"su un'unica riga, in questo modo il carattere jolly \".\"<br>" +
        		"riesce a soddisfare anche il fine riga</html>");
        checks.add(dotall, constr);

        unicodeCase = new JCheckBox("Unicode Case");
        checks.add(unicodeCase, constr);

        canonEq = new JCheckBox("Canonical equivalence");
        checks.add(canonEq, constr);
        
        useAnchoringBounds = new JCheckBox("Use ^$");
        useAnchoringBounds.setSelected(true);
        checks.add(useAnchoringBounds, constr);

        
        Insets defaultInsets = constr.insets;

        unix = new JCheckBox("Unix lines");
        unix.setToolTipText(
        		"<html>Se abilitato viene considerato come terminatore<br>" +
        		"di riga solamente il carattere \\n</html>");
        constr.insets = new Insets(0,10,0,0);
        checks.add(unix, constr);

        constr.insets = defaultInsets;

        comments = new JCheckBox("Allow comments");
        checks.add(comments, constr);
        
        return checks;
    }

    private class CheckboxListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            tabbed.setCanonEq(canonEq.getModel().isSelected());
            tabbed.setCaseInsensitive(caseInsensitive.getModel().isSelected());
            tabbed.setDotall(dotall.getModel().isSelected());
            tabbed.setMultiline(multiline.getModel().isSelected());
            tabbed.setUnicodeCase(unicodeCase.getModel().isSelected());
            tabbed.setUnixLine(
                    unix.isEnabled() &&
                    unix.isSelected()
                    );
            tabbed.setAllowComments(comments.getModel().isSelected());
            tabbed.setUseAnchoringBounds(useAnchoringBounds.isSelected());
            fireRegexpChanged();
        }
    }

    public static void main(String[] args) {
        new JRegexp();
    }
    
    private Tabs tabbed;
    private JTextPane regexpEditor;
    private JSplitPane split; 
    
    private JCheckBox caseInsensitive, multiline, dotall, 
    unicodeCase, canonEq, unix, comments, useAnchoringBounds;
}
