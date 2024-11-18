package edu.hm.hafner.analysis.parser.dry.dupfinder;

/**
 * Java Bean class for a Reshaper DupFinder range.
 *
 * @author Rafal Jasica
 */
@SuppressWarnings("PMD.DataClass")
public class Range {
    private int start;
    private int end;

    /**
     * Returns the start.
     *
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the start to the specified value.
     *
     * @param start the value to set
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * Returns the line range start.
     *
     * @return the line range start
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the end to the specified value.
     *
     * @param end the value to set
     */
    public void setEnd(final int end) {
        this.end = end;
    }
}
