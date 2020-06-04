package edu.hm.hafner.analysis;

/**
 * A builder for the LineRange class.
 */
public class LineRangeBuilder {
    private boolean startSet;
    private int start;

    private boolean endSet;
    private int end;

    /**
     * Builds a new LineRange from the set values.
     * @return The new LineRange
     */
    public LineRange build() {
        if(!startSet) {
            throw new IllegalStateException("Start has to be set in order to build a IssueDifference");
        }
        if(!endSet) {
            throw new IllegalStateException("End has to be set in order to build a IssueDifference");
        }
        return new LineRange(start, end);
    }

    /**
     * @param start The LineRanges start line.
     * @return The Builder instance.
     */
    public LineRangeBuilder setStart(int start) {
        if(startSet) {
            throw new IllegalStateException("Start can only be set once!");
        }
        startSet = true;
        this.start = start;
        return this;
    }

    /**
     * @param end The LineRanges end line.
     * @return The Builder instance.
     */
    public LineRangeBuilder setEnd(int end) {
        if(endSet) {
            throw new IllegalStateException("End can only be set once!");
        }
        endSet = true;
        this.end = end;
        return this;
    }
}
