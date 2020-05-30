package edu.hm.hafner.analysis;

import java.util.Objects;

/**
 * A Builder for a LineRange Object.
 *
 * @author Thorsten Schartel
 */
public class LineRangeBuilder {
    /**
     * The Line start.
     */
    private int start;
    /**
     * The Line end.
     */
    private int end;

    /**
     * Sets the LineStart.
     *
     * @param start
     *         lineStart.
     *
     * @return this Builder.
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Sets the lineEnd.
     *
     * @param end
     *         lineEnd.
     *
     * @return this Builder.
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Build a new LineRange Object.
     *
     * @return new LineRange Object.
     */
    public LineRange build() {
        return new LineRange(start, end);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineRangeBuilder that = (LineRangeBuilder) o;
        return start == that.start &&
                end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
