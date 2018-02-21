package edu.hm.hafner.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses an input stream for compiler warnings using the provided regular expression. Sub classes of this parser
 * should create concrete {@link Issue issue} instances.
 *
 * @author Ullrich Hafner
 */
public abstract class RegexpParser extends AbstractParser<Issue> {
    private static final long serialVersionUID = -82635675595933170L;

    /** Used to define a false positive warnings that should be excluded after the regular expression scan. */
    protected static final Issue FALSE_POSITIVE = new IssueBuilder().build();

    /** Pattern identifying an ant task debug output prefix. */
    protected static final String ANT_TASK = "^(?:.*\\[.*\\])?\\s*";

    /** Pattern of compiler warnings. */
    private Pattern pattern;

    private void setPattern(final String warningPattern, final boolean useMultiLine) {
        if (useMultiLine) {
            pattern = Pattern.compile(warningPattern, Pattern.MULTILINE);
        }
        else {
            pattern = Pattern.compile(warningPattern);
        }
    }

    /**
     * Creates a new instance of {@link RegexpParser}.
     *
     * @param warningPattern
     *         pattern of compiler warnings.
     * @param useMultiLine
     *         Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match just after or
     *         just before, respectively, a line terminator or the end of the input sequence. By default these
     */
    protected RegexpParser(final String warningPattern, final boolean useMultiLine) {
        super();

        setPattern(warningPattern, useMultiLine);
    }

    /**
     * Parses the specified string content and creates annotations for each found warning.
     *
     * @param content
     *         the content to scan
     * @param issues
     *         the found annotations
     *
     * @throws ParsingCanceledException
     *         indicates that the user canceled the operation
     */
    protected void findAnnotations(final String content, final Issues<Issue> issues)
            throws ParsingCanceledException {
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            Issue warning = createIssue(matcher, new IssueBuilder());
            if (warning != FALSE_POSITIVE) { // NOPMD
                issues.add(warning);
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
     */
    protected abstract Issue createIssue(Matcher matcher, IssueBuilder builder);
}
