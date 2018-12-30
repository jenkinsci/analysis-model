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
 * Parses a report file line by line for issues using a pre-defined regular expression. If the regular expression
 * matches then the abstract method {@link #createIssue(Matcher, LookaheadStream, IssueBuilder)} will be called.
 * Sub classes need to provide an implementation that transforms the {@link Matcher} instance into a new issue.
 * If required, sub classes may consume additional lines from the report file before control is handed back to the
 * template method of this parser.
 *
 * @author Ullrich Hafner
 */
public abstract class LookaheadParser extends IssueParser {
    private static final long serialVersionUID = 3240719494150024894L;

    /** Pattern of compiler warnings. */
    private final Pattern pattern;

    private int currentLine = 0;

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
     * Optionally configures the issue builder instance. This default implementation does nothing.
     *
     * @param builder
     *         the build to configure
     *
     * @return the builder
     */
    protected IssueBuilder configureIssueBuilder(final IssueBuilder builder) {
        return builder;
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

    /**
     * Returns the number of the current line in the parsed file.
     *
     * @return the current line
     */
    protected int getCurrentLine() {
        return currentLine;
    }
}
