/**
 * Tabs.java
 *
 * Creato il 20/nov/07 18:22:24
 */
package it.matteopic.jrb;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Matteo Piccinini
 */
public class Tabs extends JTabbedPane implements ChangeListener {

	private static final long serialVersionUID = 8140998875952461968L;
	private String currentRegexp;
    private List<RegexpPanel>panels;

    public Tabs(JRegexp buddy) {
        addChangeListener(this); 

        panels = new ArrayList<RegexpPanel>();
        panels.add(new TestTab(buddy));
        panels.add(new JakartaRegexpTab(buddy));
        panels.add(new ReplacementTab(buddy));
        
        for (RegexpPanel panel : panels) {
            add(panel.getName(), panel);
        }
    }

    
    public void setUseAnchoringBounds(boolean useAnchoringBounds) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setUseAnchoringBounds(useAnchoringBounds);
    }

    public void setUnixLine(boolean unixLine) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setUnixLine(unixLine);
    }

    public void setAllowComments(boolean comments) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setAllowComments(comments);
    }

    public void setCanonEq(boolean canonEq) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setCanonEq(canonEq);
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setCaseInsensitive(caseInsensitive);
    }

    public void setDotall(boolean dotall) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setDotall(dotall);
    }

    public void setMultiline(boolean multiline) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setMultiline(multiline);
    }

    public void setUnicodeCase(boolean unicodeCase) {
        int index = getSelectedIndex();
        if(index != -1)panels.get(index).setUnicodeCase(unicodeCase);
    }

    public void setRegexp(String regexp) {
        this.currentRegexp = regexp;
        process(currentRegexp);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        process(currentRegexp);
    }

    public void process(String regexp){
        if(regexp == null)return;
        int index = getSelectedIndex();
        try{
            if(index != -1)panels.get(index).process(regexp);
        }catch(Exception e){}
    }
}
