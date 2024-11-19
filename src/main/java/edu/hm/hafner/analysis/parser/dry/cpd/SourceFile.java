package edu.hm.hafner.analysis.parser.dry.cpd;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Java Bean class for a file of the PMD CPD format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.DataClass")
public class SourceFile {
    /** Starting line number in file. */
    private int line;
    /** Path of the file. */
    @CheckForNull
    private String path;

    /**
     * Returns the path of this file.
     *
     * @return the path of this file
     */
    @CheckForNull
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of this file to the specified value.
     *
     * @param path
     *         the value to set
     */
    public void setPath(@CheckForNull final String path) {
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
     * @param line
     *         the value to set
     */
    public void setLine(final int line) {
        this.line = line;
    }
}
