/**
 * RegexpProcessor.java
 *
 * Creato il 27/nov/07 15:33:08
 */
package it.matteopic.jrb.core;

/**
 *
 * @author Matteo Piccinini
 */
public interface RegexpProcessor {

    public void process(String regexp);
    
    public void setUseAnchoringBounds(boolean useAnchoringBounds);

    public void setUnixLine(boolean unixLine);

    public void setAllowComments(boolean comments);

    public void setCanonEq(boolean canonEq);

    public void setCaseInsensitive(boolean caseInsensitive);

    public void setDotall(boolean dotall);

    public void setMultiline(boolean multiline);

    public void setUnicodeCase(boolean unicodeCase);
}
