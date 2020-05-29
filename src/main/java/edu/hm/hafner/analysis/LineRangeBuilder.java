package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.LineRange;

public class LineRangeBuilder {

    /**
     * Setter for start.
     *
     * @param start point.
     *
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     *
     * Setter for end point.
     * @param end point.
     *
     */
    public void setEnd(final int end) {
        this.end = end;
    }

    /**
     * Start point.
     */
    private int start;

    /**
     * End point.
     */
    private int end;

    /**
     * Builder for lineRange
     * @return new LineRange.
     */
    public LineRange build() {
        return new LineRange(start,end);
    }
}
