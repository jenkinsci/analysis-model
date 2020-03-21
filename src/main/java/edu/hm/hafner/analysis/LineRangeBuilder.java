package edu.hm.hafner.analysis;


/**
 * Creates new {@link LineRange} using the builder pattern.
 * @author Elena Lilova
 */

public class LineRangeBuilder {

    private  int start;
    private  int end;

    /**
     * Sets the the start value
     * @param start
     * @return this
     */
    public LineRangeBuilder setStart(int start){
        this.start = start;
        return this;
    }

    /**
     * Sets the the end value
     * @param end
     * @return this
     */
    public LineRangeBuilder setEnd(int end){
        this.end = end;
        return this;
    }

    /**
     * Creates a new {@link LineRange} based on the specified properties.
     *
     * @return the created lineRange
     */
    public LineRange build(){
        return new LineRange(start, end);
    }


}
