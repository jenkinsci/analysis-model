package edu.hm.hafner.analysis;

/**
 * Builder for lineRange.
 */
public class LineRangeBuilder {

    /**
     * Setter for start value.
     * @param start value.
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * Setter for end value.
     * @param end value.
     */
    public void setEnd(final int end) {
        this.end = end;
    }

    /**
     * Start value.
     */
    private int start;

    /**
     * end value.
     */
    private int end;

    /**
     * builder for lineRange.
     * @return new LineRange object.
     */
    public LineRange build() {
        return new LineRange(start, end);
    }
}
