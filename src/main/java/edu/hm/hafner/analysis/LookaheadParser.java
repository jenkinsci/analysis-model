package edu.hm.hafner.analysis;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.LookaheadStream;

/**
 * Parses a report file line by line for issues using a pre-defined regular expression. If the regular expression
 * matches then the abstract method {@link #createIssue(Matcher, LookaheadStream, IssueBuilder)} will be called. Sub
 * classes need to provide an implementation that transforms the {@link Matcher} instance into a new issue. If required,
 * sub classes may consume additional lines from the report file before control is handed back to the template method of
 * this parser.
 *
 * @author Ullrich Hafner
 */
public abstract class LookaheadParser extends IssueParser {
    private static final long serialVersionUID = 3240719494150024894L;

    /** Pattern identifying an ant task debug output prefix. */
    protected static final String ANT_TASK = "^(?:.*\\[[^]]*\\])?\\s*";

    private static final String ENTERING_DIRECTORY = "Entering directory";
    private static final Pattern MAKE_PATH
            = Pattern.compile(".*make(?:\\[\\d+])?: " + ENTERING_DIRECTORY + " [`'](?<dir>.*)['`]");
    private static final String NINJA_PREFIX = "-- Build files have";
    private static final Pattern NINJA_PATH = Pattern.compile(NINJA_PREFIX + " been written to: (?<dir>.*)");

    private final Pattern pattern;

    /**
     * Creates a new instance of {@link LookaheadParser}.
     *
     * @param pattern
     *         pattern of compiler warnings.
     */
    protected LookaheadParser(final String pattern) {
        super();

        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        Report report = new Report();
        try (Stream<String> lines = readerFactory.readStream()) {
            LookaheadStream lookahead = new LookaheadStream(lines);
            IssueBuilder builder = new IssueBuilder();
            while (lookahead.hasNext()) {
                String line = lookahead.next();
                if (line.contains(ENTERING_DIRECTORY)) {
                    Matcher makeLineMatcher = MAKE_PATH.matcher(line);
                    if (makeLineMatcher.matches()) {
                        builder.setDirectory(makeLineMatcher.group("dir"));
                    }
                }
                else if (line.contains(NINJA_PREFIX)) {
                    Matcher ninja = NINJA_PATH.matcher(line);
                    if (ninja.matches()) {
                        builder.setDirectory(ninja.group("dir"));
                    }
                }
                else {
                    final String content = interestingLineContent(line);
                    if (StringUtils.isNotEmpty(content)) {
                        Matcher matcher = pattern.matcher(content);
                        if (matcher.find()) {
                            createIssue(matcher, lookahead, builder).ifPresent(report::add);
                        }
                    }
                }
                if (Thread.interrupted()) {
                    throw new ParsingCanceledException();
                }
            }
        }

        return postProcess(report);
    }

    /**
     * Creates a new issue for the specified pattern. This method is called for each matching line in the specified
     * file. If a match is a false positive, then return {@link Optional#empty()} to ignore this warning.
     *
     * @param matcher
     *         the regular expression matcher
     * @param lookahead
     *         the lookahead stream to read additional lines
     * @param builder
     *         the issue builder to use
     *
     * @return a new annotation for the specified pattern
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     */
    protected abstract Optional<Issue> createIssue(Matcher matcher, LookaheadStream lookahead, IssueBuilder builder)
            throws ParsingException;

    /**
     * Returns whether the specified line is interesting. Each interesting line will be matched by the defined regular
     * expression. Here a parser can implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met. This default implementation does always return {@code true}.
     *
     * @param line
     *         the line to inspect
     *
     * @return {@code true} if the line should be handed over to the regular expression scanner, {@code false} if the
     *         line does not contain a warning.
     * @deprecated use interestingLineContent(String) instead
     */
    @Deprecated
    protected boolean isLineInteresting(final String line) {
        return true;
    }

    /**
     * Returns a substring of the given line whether the specified line is interesting.
     * This substring will be matched by the defined regular expression.
     * Here a parser can implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met. This default implementation does always return the {@code line} itself
     *
     * @param line
     *         the line to inspect
     *
     * @return the {@code line} if the line should be handed over to the regular expression scanner,
     *         an substring of the {@code line} if the regular expression scanner should work on a smaller
     *         part (e.g. for performance reasons),
     *         or {@code null} if the line is line does not contain a warning.
     */
    protected String interestingLineContent(final String line) {
        // call isLineInteresting for backward compatibility
        if (isLineInteresting(line)) {
            return line;
        }
        return null;
    }

    /**
     * Post processes the issues. This default implementation does nothing.
     *
     * @param report
     *         the issues after the parsing process
     *
     * @return the post processed issues
     */
    protected Report postProcess(final Report report) {
        return report;
    }
}
