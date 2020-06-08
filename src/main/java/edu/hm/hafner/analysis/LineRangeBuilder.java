package edu.hm.hafner.analysis;

public class LineRangeBuilder {

    /**
     * Start point.
     */
    private int start;

    /**
     * End point.
     */
    private int end;

    /**
     * Set the value of the Start point
     * @param start
     * @return this
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Set the value of the End point
     * @param end
     * @return this
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * Builds an LineRange Object
     * @return a new LineRange Object
     */
    public LineRange build(){
        return new LineRange(start,end);
    }
}
