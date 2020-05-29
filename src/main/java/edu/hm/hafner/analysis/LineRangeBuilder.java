package edu.hm.hafner.analysis;

/**
 * Creates new {@link LineRange} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value.

 * @author Tobias Karius
 */
public class LineRangeBuilder {

    /**
     * The start of the line range.
     */
    private int start;

    /**
     * The end of the line range.
     */
    private int end;

    /**
     * Sets the start of the line range.
     * @param start the start of the line range.
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * Sets the end of the line range.
     * @param end the end of the line range.
     */
    public void setEnd(final int end) {
        this.end = end;
    }

    /**
     * Creates a new {@link LineRange} based on the specified properties.
     * @return the created LineRange
     */
    public LineRange build() {
        return new LineRange(start, end);
    }
}
