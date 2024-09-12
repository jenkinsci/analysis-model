package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the CrossCoreEmbeddedStudio (CCES) log files.
 */
public class CrossCoreEmbeddedStudioParser extends LookaheadParser {
    private static final long serialVersionUID = 1L;

    /* Regex to match the CCES warnings.
     *
     * Details:
     *  - See below the MATCHER_* ids to ease their retrieval.
     *  - Potential columns are ignored
     *  - Assume warnings description are always spread over two lines (what was observed so far)
     *  - Errors are not parsed, only warnings
     */
    private static final String CCES_WARNING_PATTERN =
            "^\"(?<file>.+?)\", line (?<line>\\d+).*(?<category>cc\\d+).*warning:(?<messageBegin>.*)";

    /**
     * Creates a new instance of {@link CrossCoreEmbeddedStudioParser}.
     */
    public CrossCoreEmbeddedStudioParser() {
        super(CCES_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var message = new StringBuilder(matcher.group("messageBegin").trim());

        // always grab the second line
        if (lookahead.hasNext()) {
            message.append(" ");
            message.append(lookahead.next().trim());
        }

        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setSeverity(Severity.WARNING_NORMAL)
                .setCategory(matcher.group("category"))
                .setMessage(message.toString())
                .buildOptional();
    }
}
