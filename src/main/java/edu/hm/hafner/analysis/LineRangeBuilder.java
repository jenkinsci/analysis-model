package edu.hm.hafner.analysis;

/**
 * LineRange Builder.
 */
public class LineRangeBuilder {

    private int start = -1;

    private int end = -1;

    /**
     * Creates a new LineRange from Builder parameters.
     * @return LineRange
     */
    public LineRange build() {
        if (start == -1) {
            throw new IllegalStateException("Start has to be set first");
        }
        if (end == -1) {
            throw new IllegalStateException("End has to be set first");
        }
        return new LineRange(start, end);
    }

    /**
     * Sets the start line.
     * @param start line start
     * @return Builder instance
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Sets the end line.
     * @param end line end
     * @return Builder instance
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }
}
