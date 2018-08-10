package edu.hm.hafner.analysis.parser.dry.cpd;

/**
 * Java Bean class for a file of the PMD CPD format.
 *
 * @author Ullrich Hafner
 */
public class SourceFile {
    /** Starting line number in file. */
    private int line;
    /** Path of the file. */
    private String path;

    /**
     * Returns the path of this file.
     *
     * @return the path of this file
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of this file to the specified value.
     *
     * @param path the value to set
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Returns the line of the duplication.
     *
     * @return the line of the duplication
     */
    public int getLine() {
        return line;
    }

    /**
     * Sets the line of the duplication to the specified value.
     *
     * @param line the value to set
     */
    public void setLine(final int line) {
        this.line = line;
    }
}

