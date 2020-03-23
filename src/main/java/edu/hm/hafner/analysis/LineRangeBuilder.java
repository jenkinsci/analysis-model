package edu.hm.hafner.analysis;

/**
 * Creates new {@link LineRange lineRanges} using the builder pattern.
 *
 * <p>Example:</p>
 * <blockquote><pre>
 * LineRange lineRange = new LineRangeBuilder()
 *                    .setStart(0)
 *                    .setEnd(10)
 *                    .build();
 * </pre></blockquote>
 *
 * @author walli545
 */
public class LineRangeBuilder {
    private int start = 0;
    private int end = 0;

    /**
     * Sets the first line of the range. Defaults to 0. Inclusive.
     *
     * @param start
     *         start of the range
     *
     * @return this
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Sets the last line of the range. Defaults to 0. Inclusive.
     *
     * @param end
     *         end of the range
     *
     * @return this
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Creates a new {@link LineRange} using the specified values.
     * <p>
     * If only start is set, only one line is in the range.
     *
     * @return the newly created {@link LineRange}.
     */
    public LineRange build() {
        return new LineRange(getStart(), getEnd());
    }

    private int getStart() {
        return start;
    }

    private int getEnd() {
        return end;
    }
}
