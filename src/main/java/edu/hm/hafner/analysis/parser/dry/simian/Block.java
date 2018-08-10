package edu.hm.hafner.analysis.parser.dry.simian;

/**
 * Java Bean class for a duplicated block of a Simian duplication warning.
 *
 * @author Ullrich Hafner
 */
public class Block {
    private String sourceFile;
    private int startLineNumber;
    private int endLineNumber;

    /**
     * Returns the file name.
     *
     * @return the file name
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the file name to the specified value.
     *
     * @param sourceFile
     *            the value to set
     */
    public void setSourceFile(final String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Returns the line number of the start of the duplication.
     *
     * @return the line number of the start of the duplication.
     */
    public int getStartLineNumber() {
        return startLineNumber;
    }

    /**
     * Sets the line number of the start of the duplication to the specified
     * value.
     *
     * @param startLineNumber
     *            the value to set
     */
    public void setStartLineNumber(final int startLineNumber) {
        this.startLineNumber = startLineNumber;
    }

    /**
     * Returns the line number of the end of the duplication.
     *
     * @return the line number of the end of the duplication.
     */
    public int getEndLineNumber() {
        return endLineNumber;
    }

    /**
     * Sets the line number of the end of the duplication to the specified
     * value.
     *
     * @param endLineNumber
     *            the value to set
     */
    public void setEndLineNumber(final int endLineNumber) {
        this.endLineNumber = endLineNumber;
    }
}

