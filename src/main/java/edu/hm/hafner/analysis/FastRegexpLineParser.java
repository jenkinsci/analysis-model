package edu.hm.hafner.analysis;

/**
 * Parses an input stream line by line for compiler warnings using the provided regular expression. Multi-line regular
 * expressions are not supported, each warning has to be one a single line. A parser must implement some fast checks
 * (i.e. string or character comparisons) before the line parsing is started, see {@link #isLineInteresting(String)}.
 *
 * @author Ullrich Hafner
 */
public abstract class FastRegexpLineParser extends RegexpLineParser {
    /**
     * Creates a new instance of {@link FastRegexpLineParser}.
     *
     * @param warningPattern
     *         pattern of compiler warnings.
     */
    protected FastRegexpLineParser(final String warningPattern) {
        super(warningPattern);
    }

    @Override
    protected void findAnnotations(final String content, final Issues<Issue> issues)
            throws ParsingCanceledException {
        if (isLineInteresting(content)) {
            super.findAnnotations(content, issues);
        }
    }

    /**
     * Returns whether the specified line is interesting. Each interesting line will be handled by the defined regular
     * expression. Here a parser must implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met.
     *
     * @param line
     *         the line to inspect
     *
     * @return {@code true} if the line should be handed over to the regular expression scanner, {@code false} if the
     *         line does not contain a warning.
     */
    protected abstract boolean isLineInteresting(String line);
}
