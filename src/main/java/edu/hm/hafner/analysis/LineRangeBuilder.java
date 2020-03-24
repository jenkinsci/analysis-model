package edu.hm.hafner.analysis;

/***
 * Creates new {@link LineRange lineRanges} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value.
 *
 * <p>Example:</p>
 *  * <blockquote><pre>
 *  * LineRange lineRange = new LineRangeBuilder()
 *  *                      .setStart(1)
 *  *                      .setEnd(2)
 *  *                      .build();
 *  * </pre></blockquote>
 *
 * @author budelmann
 */
public class LineRangeBuilder {

    private int start;
    private int end;

    /**
     * Sets the end value of the range.
     *
     * @param end
     *         the end of the range
     *
     * @return this
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Sets the start value of the range.
     *
     * @param start
     *         the start of the range
     *
     * @return this
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /***
     * Creates a new {@link LineRange} based on the specified properties.
     * 
     * @return the created LineRange
     */
    public LineRange build() {
        return new LineRange(start, end);
    }
}
