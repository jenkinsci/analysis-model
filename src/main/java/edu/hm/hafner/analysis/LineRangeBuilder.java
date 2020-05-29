package edu.hm.hafner.analysis;
/**
 * Builder for LineRange.
 *
 * @author Viet Phuoc Ho (v.ho@hm.edu)
 */
public class LineRangeBuilder {

    /**
     * Setter for start value.
     * @param start
     *              Start value.
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * Setter for end.
     * @param end
     *              End value.
     */
    public void setEnd(final int end) {
        this.end = end;
    }

    /**
     * The Start index of the line range.
     */
    private int start;

    /**
     * The End index of the line range.
     */
    private int end;

    /**
     * Builder for a lineRange.
     * @return new Object of LineRange.
     */
    public LineRange build() {
        return new LineRange(start,end);
    }
}