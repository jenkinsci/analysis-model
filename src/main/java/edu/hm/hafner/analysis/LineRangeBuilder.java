package edu.hm.hafner.analysis;

import java.util.UUID;

/**
 * Creates a new {@link LineRange} using the builder pattern.
 *
 * <p>Example:</p>
 * <blockquote><pre>
 * LineRange lineRange = new LineRangeBuilder()
 *                    .setLineRange(1,4)
 *                    .build();
 * </pre></blockquote>
 *
 * @author mbauerness
 */
public class LineRangeBuilder {
    private int start;
    private int end;

    /**
     * Sets one line instead of a line range.
     *
     * @param line
     *          a single line
     *
     * @return this
     */
    public LineRangeBuilder setLine(final int line) {
        if (start <= 0) {
            this.start = 0;
            this.end = 0;
        }

        this.start = line;
        this.end = line;

        return this;
    }

    /**
     * Sets a line range from start to end.
     *
     * @param pStart
     *          the start of the range
     *
     * @param pEnd
     *          the end of the range
     *
     * @return this
     */
    public LineRangeBuilder setLineRange(final int pStart, final int pEnd) {
        if (pStart <= 0) {
            this.start = 0;
            this.end = 0;
        }
        if (pEnd < pStart) {
            this.start = pEnd;
            this.end = pStart;
        }

        this.start = pStart;
        this.end = pEnd;

        return this;
    }

    /**
     * Creates a new {@link LineRange} based on the specified properties.
     *
     * @return the created {@link LineRange}
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
