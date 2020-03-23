package edu.hm.hafner.analysis;

public class LineRangeBuilder {
    private int start = -1;
    private int end = -1;

    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    public LineRange build() {
        return new LineRange(start, end);
    }
}
