package edu.hm.hafner.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses an input stream for compiler warnings using the provided regular expression.
 *
 * @author Ullrich Hafner
 */
public abstract class RegexpParser extends AbstractWarningsParser {
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
     * @param id             ID of the parser
     * @param warningPattern pattern of compiler warnings.
     * @param useMultiLine   Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match
     *                       just after or just before, respectively, a line terminator or the end of the input
     *                       sequence. By default these expressions only match at the beginning and the end of the
     *                       entire input sequence.
     */
    protected RegexpParser(final String id, final String warningPattern, final boolean useMultiLine) {
        super(id);

        setPattern(warningPattern, useMultiLine);
    }

    /**
     * Parses the specified string content and creates annotations for each found warning.
     *
     * @param content the content to scan
     * @param issues  the found annotations
     * @throws ParsingCanceledException indicates that the user canceled the operation
     */
    protected void findAnnotations(final String content, final Issues issues) throws ParsingCanceledException {
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            Issue warning = createWarning(matcher);
            if (warning != FALSE_POSITIVE) { // NOPMD
                // detectPackageName(warning); // TODO: package detection should be done on all warnings afterwards
                issues.add(warning);
            }
            if (Thread.interrupted()) {
                throw new ParsingCanceledException();
            }
        }
    }

    /**
     * Creates a new annotation for the specified pattern. This method is called for each matching line in the specified
     * file. If a match is a false positive, then you can return the constant {@link #FALSE_POSITIVE} to ignore this
     * warning.
     *
     * @param matcher the regular expression matcher
     * @return a new annotation for the specified pattern
     */
    protected abstract Issue createWarning(Matcher matcher);
}
