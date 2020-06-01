package edu.hm.hafner.analysis;

public class LineRangeBuilder {

    private int lineStart = 0;
    private int lineEnd = 0;

    public LineRange build() {
        return new LineRange(lineStart, lineEnd);
    }

    public LineRangeBuilder withLine(final int line) {
        return withLineRange(line, line);
    }


    public LineRangeBuilder withLineRange(final int iLineStart, final int iLineEnd) {
        if (iLineStart < 0) {
            throw new IllegalArgumentException("Lines values have to be greater or equal to zero");
        }

        if (iLineEnd < iLineStart) {
            throw new IllegalArgumentException("Line End has to be greater than Line Start");
        }

        lineStart = iLineStart;
        lineEnd = iLineEnd;

        return this;
    }

}
