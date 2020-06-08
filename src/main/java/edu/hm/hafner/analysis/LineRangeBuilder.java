package edu.hm.hafner.analysis;

/**
 * Provides a builder for class {@link LineRange}
 *
 * @author Matthias König
 */

public final class LineRangeBuilder {
    int start;
    int end;
    int line;

    public LineRangeBuilder lineStartAndEndDifferent (int start, int end) {
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
        return this;
    }

    public LineRangeBuilder lineStartAndEndEqual (int singleLine) {
        start = singleLine;
        end = singleLine;
        return this;
    }

    public LineRange build() {
        LineRange lineRange = new LineRange(start, end);
        return lineRange;
    }


}
