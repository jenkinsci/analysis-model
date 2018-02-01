package edu.hm.hafner.analysis.parser.dry.dupfinder;

/**
 * Java Bean class for a Reshaper DupFinder fragment.
 *
 * @author Rafal Jasica
 */
public class Fragment {
    private String fileName;
    private String text;
    private Range lineRange;
    private Range offsetRange;

    /**
     * Returns the file name.
     *
     * @return the path of this file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name to the specified value.
     *
     * @param fileName the value to set
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text to the specified value.
     *
     * @param text the value to set
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Returns the line range.
     *
     * @return the line range
     */
    public Range getLineRange() {
        return lineRange;
    }


    /**
     * Sets the line range to the specified value.
     *
     * @param lineRange the value to set
     */
    public void setLineRange(final Range lineRange) {
        this.lineRange = lineRange;
    }

    /**
     * Returns the offset range.
     *
     * @return the offset range
     */
    public Range getOffsetRange() {
        return offsetRange;
    }


    /**
     * Sets the offset range to the specified value.
     *
     * @param offsetRange the value to set
     */
    public void setOffsetRange(final Range offsetRange) {
        this.offsetRange = offsetRange;
    }
}

