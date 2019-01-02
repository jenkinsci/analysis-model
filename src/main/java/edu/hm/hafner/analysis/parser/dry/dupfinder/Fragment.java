package edu.hm.hafner.analysis.parser.dry.dupfinder;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Java Bean class for a Reshaper DupFinder fragment.
 *
 * @author Rafal Jasica
 */
@SuppressWarnings("PMD.DataClass")
public class Fragment {
    @Nullable
    private String fileName;
    @Nullable
    private String text;
    @Nullable
    private Range lineRange;
    @Nullable
    private Range offsetRange;

    /**
     * Returns the file name.
     *
     * @return the path of this file
     */
    @Nullable
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
    @Nullable
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
    public void setLineRange(final Range lineRange) {
        this.lineRange = lineRange;
    }

    /**
     * Returns the offset range.
     *
     * @return the offset range
     */
    @Nullable
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

