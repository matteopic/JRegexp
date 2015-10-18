/**
 * RegexpPanel.java
 *
 * Creato il 29/nov/07 10:11:56
 */
package it.matteopic.jrb;

import it.matteopic.jrb.core.RegexpManager;
import it.matteopic.jrb.core.RegexpProcessor;

import javax.swing.JPanel;

/**
 *
 * @author Matteo Piccinini
 */
public abstract class RegexpPanel extends JPanel implements RegexpProcessor {

	private static final long serialVersionUID = 2296736166997669175L;
	private RegexpManager manager;

    public RegexpPanel(String name){
        manager = new RegexpManager();
        setName(name);
    }

    public void setUseAnchoringBounds(boolean useAnchoringBounds) {
        manager.setUseAnchoringBounds(useAnchoringBounds);
    }

    public void setUnixLine(boolean unixLine) {
        manager.setUnixLine(unixLine);
    }

    public void setAllowComments(boolean comments) {
        manager.setAllowComments(comments);
    }

    public void setCanonEq(boolean canonEq) {
        manager.setCanonEq(canonEq);
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        manager.setCaseInsensitive(caseInsensitive);
    }

    public void setDotall(boolean dotall) {
        manager.setDotall(dotall);
    }

    public void setMultiline(boolean multiline) {
        manager.setMultiline(multiline);
    }

    public void setUnicodeCase(boolean unicodeCase) {
        manager.setUnicodeCase(unicodeCase);
    }

    protected RegexpManager getManager(){
        return manager;
    }

}
