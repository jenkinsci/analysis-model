package edu.hm.hafner.analysis;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Creates a new {@link LineRange} using the Builder Pattern.
 *
 * @author Bastian Kersting
 */
public class LineRangeBuilder {

    @Nullable
    private Integer start = null;
    @Nullable
    private Integer end = null;

    /**
     * Creates a new LineRangeBuilder.
     */
    public LineRangeBuilder() { }

    /**
     * Sets the start of the LineRange.
     * @param start start of the LineRange
     * @return this
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Sets the end of the LineRange.
     * @param end end of the LineRange
     * @return this
     */
    public  LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Creates a new {@link LineRange} based on the specified properties.
     * @return a new LineRange
     */
    public LineRange build() {
        if (start == null || end == null) {
            throw new IllegalStateException("Can't build empty LineRange");
        }
        return new LineRange(start, end);
    }
}
