package edu.hm.hafner.analysis;

import java.util.Optional;
import java.util.regex.Matcher;

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
public abstract class RegexpLineParser extends LookaheadParser {
    public RegexpLineParser(final String pattern) {
        super(pattern);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        return createIssue(matcher, builder);
    }

    protected abstract Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder);
}
