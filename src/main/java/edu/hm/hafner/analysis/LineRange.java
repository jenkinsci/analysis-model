package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.UUID;

/**
 * A line range in a source file is defined by its first and last line.
 *
 * @author Ullrich Hafner
 */
public class LineRange implements Serializable {
    private static final long serialVersionUID = -4124143085672930110L;

    private final int start;
    private final int end;

    /**
     * Creates a new instance of {@link LineRange}.
     *
     * @param line
     *         the single line of this range
     */
    public LineRange(final int line) {
        this(line, line);
    }

    /**
     * Creates a new instance of {@link LineRange}.
     *
     * @param start
     *         start of the range
     * @param end
     *         end of the range
     */
    public LineRange(final int start, final int end) {
        if (start <= 0) {
            this.start = 0;
            this.end = 0;
        }
        else if (start < end) {
            this.start = start;
            this.end = end;
        }
        else {
            this.start = end;
            this.end = start;
        }
    }

    /**
     * Returns the first line of this range.
     *
     * @return the first line of this range
     */
    public int getStart() {
        return start;
    }

    /**
     * Returns the last line of this range.
     *
     * @return the last line of this range
     */
    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LineRange lineRange = (LineRange) obj;

        if (start != lineRange.start) {
            return false;
        }
        return end == lineRange.end;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%d-%d]", start, end);
    }

    /**
     * Creates new {@link LineRange lineRange} using the builder pattern.
     *
     * <p>Example:</p>
     * <blockquote><pre>
     * LineRange lineRange = new LineRangeBuilder()
     *                      .setStart(0)
     *                      .setEnd(100)
     *                      .build()
     * </pre></blockquote>
     *
     * <p>Example 2:</p>
     * <blockquote><pre>
     * LineRange lineRange = new LineRangeBuilder()
     *                      .setLine(42)
     *                      .build()
     * </pre></blockquote>
     *
     * @author S. A. D.
     */
    public static class LineRangeBuilder {
        private int start;
        private int end;

        /**
         * Sets the start of the lineRange.
         *
         * @param start
         *         of range
         *
         * @return this
         */
        public LineRangeBuilder setStart(final int start) {
            this.start = start;
            return this;
        }

        /**
         * Sets the start of the lineRange.
         *
         * @param end
         *         of range
         *
         * @return this
         */
        public LineRangeBuilder setEnd(final int end) {
            this.end = end;
            return this;
        }

        /**
         * Sets the line.
         *
         * @param line
         *         the single line of range
         *
         * @return this
         */
        public LineRangeBuilder setLine(final int line) {
            this.start = line;
            this.end = line;
            return this;
        }

        /**
         * Builds a LineRange.
         *
         * @return LineRange
         */
        public LineRange build() {
            return (new LineRange(start, end));

        }
    }
}

