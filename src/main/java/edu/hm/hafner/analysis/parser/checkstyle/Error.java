package edu.hm.hafner.analysis.parser.checkstyle;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Java Bean class for a violation of the Checkstyle format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"all", "JavaLangClash"})
public class Error {
    @CheckForNull
    private String source;
    @CheckForNull
    private String severity;
    @CheckForNull
    private String message;
    private int line;
    private int column;

    public int getColumn() {
        return column;
    }

    public void setColumn(final int column) {
        this.column = column;
    }

    @CheckForNull
    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    @CheckForNull
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    @CheckForNull
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
