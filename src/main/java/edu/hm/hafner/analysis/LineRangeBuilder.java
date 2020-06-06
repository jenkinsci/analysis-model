package edu.hm.hafner.analysis;

/**
 * Creates a LineRange with builder pattern.
 * @author Fabian Diener
 */
public class LineRangeBuilder {
    /**
     * Line Start.
     */
    private int start;
    /**
     * Line end.
     */
    private int end;

    /**
     * Setter for a single Line.
     * @param line
     * @return this
     */
    public LineRangeBuilder setSingleLine(final int line){
        if (line <= 0){
            start = 0;
            end = 0;
        }
        else {
            start = line;
            end = line;
        }
        return this;
    }

    /**
     * Setter for a range of lines.
     * @param start
     * @param end
     * @return this
     */

    public LineRangeBuilder setLineRange(final int start, final int end){
        if (start <= 0){
            this.start = 0;
            this.end = 0;
        }
        else if (start > end){
            this.end = start;
            this.start = end;
        }
        else {
            this.start = start;
            this.end = end;
        }
        return this;
    }

    /**
     * Builds a new LineRange Object
     * with start and end.
     * @return a new LineRange Object
     */
    public LineRange build(){
        return new LineRange(getStart(), getEnd());
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
