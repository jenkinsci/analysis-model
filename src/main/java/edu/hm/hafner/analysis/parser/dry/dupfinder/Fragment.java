package edu.hm.hafner.analysis.parser.dry.dupfinder;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Java Bean class for a Reshaper DupFinder fragment.
 *
 * @author Rafal Jasica
 */
@SuppressWarnings("PMD.DataClass")
@SuppressFBWarnings("EI")
public class Fragment {
    @CheckForNull
    private String fileName;
    @CheckForNull
    private String text;
    @CheckForNull
    private Range lineRange;
    @CheckForNull
    private Range offsetRange;

    /**
     * Returns the file name.
     *
     * @return the path of this file
     */
    @CheckForNull
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name to the specified value.
     *
     * @param fileName the value to set
     */
    @SuppressFBWarnings("NM")
    public void setFileName(@CheckForNull final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the text.
     *
     * @return the text
     */
    @CheckForNull
    public String getText() {
        return text;
    }

    /**
     * Sets the text to the specified value.
     *
     * @param text the value to set
     */
    public void setText(@CheckForNull final String text) {
        this.text = text;
    }

    /**
     * Returns the line range.
     *
     * @return the line range
     */
    public Range getLineRange() {
        if (lineRange == null) {
            return new Range();
        }
        return lineRange;
    }

    /**
     * Sets the line range to the specified value.
     *
     * @param lineRange the value to set
     */
    public void setLineRange(@CheckForNull final Range lineRange) {
        this.lineRange = lineRange;
    }

    /**
     * Returns the offset range.
     *
     * @return the offset range
     */
    @CheckForNull
    public Range getOffsetRange() {
        return offsetRange;
    }

    /**
     * Sets the offset range to the specified value.
     *
     * @param offsetRange the value to set
     */
    public void setOffsetRange(@CheckForNull final Range offsetRange) {
        this.offsetRange = offsetRange;
    }
}

