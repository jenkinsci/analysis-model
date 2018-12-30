package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.LookaheadStream;

/**
 * TODO
 *
 * @author Ullrich Hafner
 */
public abstract class LookaheadParser extends IssueParser {
    /** Pattern of compiler warnings. */
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
        IssueBuilder builder = configureIssueBuilder(new IssueBuilder());
        try (Stream<String> lines = readerFactory.readStream()) {
            LookaheadStream lookahead = new LookaheadStream(lines);
            while (lookahead.hasNext()) {
                String line = lookahead.next();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    createIssue(matcher, lookahead, builder).ifPresent(report::add);

                }
                if (Thread.interrupted()) {
                    throw new ParsingCanceledException();
                }
            }
        }

        return report;
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
     * Optionally configures the issue builder instance.
     *
     * @param builder
     *         the build to configure
     *
     * @return the builder
     */
    protected IssueBuilder configureIssueBuilder(final IssueBuilder builder) {
        return builder;
    }
}
