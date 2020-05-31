package edu.hm.hafner.analysis;

/**
 * Eine Builderklasse um das Erstellen eines LineRange Objekts zu vereinfachen.
 * @author Michael Schober
 */
public class LineRangeBuilder {

    /** Start der LineRange. */
    private int start;

    /** Ende der LineRange. */
    private int end;

    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Eine neue LineRange.
     * @return LineRange
     */
    public LineRange build() {
        return new LineRange(start, end);
    }
}
