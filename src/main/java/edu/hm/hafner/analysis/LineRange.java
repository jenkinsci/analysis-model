package edu.hm.hafner.analysis;

import java.io.Serializable;

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
     * Builder for the LineRange Class.
     *
     * @author Colin Mikolajczak
     */
    public static class LineRangeBuilder {
        private int startLine;
        private int endLine;
        private boolean endSet = false;
        private boolean ready = false;

        /**
         * Sets the start line of LineRange.
         *
         * @param start
         *         start line of LineRange
         *
         * @return this
         */
        public LineRangeBuilder setStartLine(final int start) {
            this.startLine = start;
            ready = true;
            return this;
        }

        /**
         * Sets the end line of LineRange.
         *
         * @param end
         *         end line of LineRange
         *
         * @return this
         */
        public LineRangeBuilder setEndLine(final int end) {
            this.endLine = end;
            this.endSet = true;
            return this;
        }

        /**
         * Builds the defined LineRange.
         *
         * @return Defined LineRange object
         * @throws IllegalStateException
         *         when start line is not set
         */
        public LineRange build() {
            if (ready) {
                if (!endSet) {
                    endLine = startLine;
                }
                return new LineRange(startLine, endLine);
            }
            else {
                throw new IllegalStateException("Start Line has to be set");
            }

        }
    }
}

