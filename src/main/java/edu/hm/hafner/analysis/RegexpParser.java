package edu.hm.hafner.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses an input stream for compiler warnings or issues from a static analysis tool using the provided regular
 * expression. Normally, this base class should not directly be extended. Rather extend from the base classes {@link
 * RegexpDocumentParser} or {@link RegexpLineParser}.
 *
 * @author Ullrich Hafner
 * @see RegexpDocumentParser
 * @see RegexpLineParser
 */
public abstract class RegexpParser extends AbstractParser {
    private static final long serialVersionUID = -82635675595933170L;

    /** Used to define a false positive warnings that should be excluded after the regular expression scan. */
    protected static final Issue FALSE_POSITIVE = new IssueBuilder().build();

    /** Pattern identifying an ant task debug output prefix. */
    protected static final String ANT_TASK = "^(?:.*\\[.*\\])?\\s*";

    /** Pattern of compiler warnings. */
    private final Pattern pattern;

    /**
     * Creates a new instance of {@link RegexpParser}.
     *
     * @param pattern
     *         pattern of compiler warnings.
     * @param useMultiLine
     *         Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match just after or
     *         just before, respectively, a line terminator or the end of the input sequence. By default these
     */
    protected RegexpParser(final String pattern, final boolean useMultiLine) {
        super();

        if (useMultiLine) {
            this.pattern = Pattern.compile(pattern, Pattern.MULTILINE);
        }
        else {
            this.pattern = Pattern.compile(pattern);
        }
    }

    /**
     * Parses the specified string {@code content} using a regular expression and creates a set of new issues for each
     * match. The new issues are added to the provided set of {@link Report}.
     *
     * @param content
     *         the content to scan
     * @param report
     *         the report to add the new issues to
     *
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    @SuppressWarnings({"ReferenceEquality", "PMD.CompareObjectsWithEquals"})
    protected void findIssues(final String content, final Report report)
            throws ParsingException, ParsingCanceledException {
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            Issue warning = createIssue(matcher, new IssueBuilder());
            if (warning != FALSE_POSITIVE) {
                report.add(warning);
            }
            if (Thread.interrupted()) {
                throw new ParsingCanceledException();
            }
        }
    }

    /**
     * Creates a new issue for the specified pattern. This method is called for each matching line in the specified
     * file. If a match is a false positive, then you can return the constant {@link #FALSE_POSITIVE} to ignore this
     * warning.
     *
     * @param matcher
     *         the regular expression matcher
     * @param builder
     *         the issue builder to use
     *
     * @return a new annotation for the specified pattern
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     */
    protected abstract Issue createIssue(Matcher matcher, IssueBuilder builder) throws ParsingException;
}
