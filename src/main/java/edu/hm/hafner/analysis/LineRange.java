package edu.hm.hafner.analysis;

import java.io.Serializable;

/**
 * A line range in a source file is defined by its first and last line.
 *
 * @author Ullrich Hafner
 */
public class LineRange implements Serializable {
    private static final long serialVersionUID = -4124143085672930110L;

    private int start;
    private int end;


    /**
     * Creates a new instance of LineRange with {@link LineRangeBuilder}
     */

    public LineRange (int start, int end) {
        this.start = start;
        this.end = end;
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
}

