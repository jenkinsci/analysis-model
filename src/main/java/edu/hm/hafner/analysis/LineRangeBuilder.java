package edu.hm.hafner.analysis;

/**
 * Builderclass for {@link LineRange}.
 * Default properties for start and end are -1
 * One can change start and end by using the Fluent Interface
 *
 * <pre>{@code
 * LineRange linerange = new LineRangeBuilder()
 *                      .setStart(0)
 *                      .setEnd(10)
 *                      .build();
 * }</pre>
 *
 * @author Daniel Soukup
 */

public class LineRangeBuilder {
    private int start = -1;
    private int end = -1;

    /**
     * Sets the start value of the range.
     * @param start
     *          start of the range
     *
     * @return this
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Sets the start value of the range.
     * @param end
     *          end of the range
     *
     * @return this
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Creates a new {@link LineRange} Object based on the defined properties.
     * @return the created LineRange Object
     */
    public LineRange build() {
        return new LineRange(start, end);
    }
}
