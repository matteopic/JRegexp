/**
 * Match.java
 *
 * Creato il 27-lug-2005 18.33.10
 */
package it.matteopic.jrb.core;

/**
 *
 * @author Matteo Piccinini
 */
public class Match {
    /**
     * 
     */
    public Match(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @return Returns the end.
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return Returns the start.
     */
    public int getStart() {
        return start;
    }

    private int start, end;
}
