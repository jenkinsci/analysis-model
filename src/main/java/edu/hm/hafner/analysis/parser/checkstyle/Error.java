package edu.hm.hafner.analysis.parser.checkstyle;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Java Bean class for a violation of the Checkstyle format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"all", "JavaLangClash"})
public class Error {
    @Nullable
    private String source;
    @Nullable
    private String severity;
    @Nullable
    private String message;
    private int line;
    private int column;

    public int getColumn() {
        return column;
    }

    public void setColumn(final int column) {
        this.column = column;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    @Nullable
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getLine() {
        return line;
    }

    public void setLine(final int line) {
        this.line = line;
    }
}

