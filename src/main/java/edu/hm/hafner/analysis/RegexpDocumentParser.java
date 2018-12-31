package edu.hm.hafner.analysis;

/**
 * Parses an input stream as a whole document for compiler warnings or issues from a static analysis tool using the
 * provided regular expression.
 *
 * @author Ullrich Hafner
 * @deprecated use {@link LookaheadParser} if possible since it is much faster when parsing large files
 */
@Deprecated
public abstract class RegexpDocumentParser extends RegexpParser {
    private static final long serialVersionUID = -4985090860783261124L;

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param pattern
     *         pattern of compiler warnings
     * @param useMultiLine
     *         Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match just after or
     *         just before, respectively, a line terminator or the end of the input sequence. By default these
     *         expressions only match at the beginning and the end of the
     */
    protected RegexpDocumentParser(final String pattern, final boolean useMultiLine) {
        super(pattern, useMultiLine);
    }

    @Override
    public Report parse(final ReaderFactory reader) throws ParsingException {
        Report warnings = new Report();
        findIssues(reader.readString() + "\n", warnings);
        return warnings;
    }
}
