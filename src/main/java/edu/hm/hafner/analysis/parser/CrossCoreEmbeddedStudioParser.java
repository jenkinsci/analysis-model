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
    private static final long serialVersionUID = 5490211629355204910L;

    /* Regex to match the CCES warnings.
     *
     * Details:
     *  - See below the MATCHER_* ids to ease their retrieval.
     *  - Potential columns are ignored
     *  - Assume warnings description are always spread over two lines (what was observed so far)
     *  - Errors are not parsed, only warnings
     */
    private static final String CCES_WARNING_PATTERN =
            "^\"(.+?)\", line (\\d+).*(cc\\d+).*warning:(.*)";

    private static final Integer MATCHER_FILE = 1;
    private static final Integer MATCHER_LINE = 2;
    private static final Integer MATCHER_CATEGORY = 3;
    private static final Integer MATCHER_MESSAGE_BEGIN = 4;

    /**
     * Creates a new instance of {@link CrossCoreEmbeddedStudioParser}.
     */
    public CrossCoreEmbeddedStudioParser() {
        super(CCES_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {

        StringBuilder message = new StringBuilder(matcher.group(MATCHER_MESSAGE_BEGIN).trim());

        // always grab the second line
        if (lookahead.hasNext()) {
            message.append(" ");
            message.append(lookahead.next().trim());
        }

        return builder.setFileName(matcher.group(MATCHER_FILE))
                .setLineStart(matcher.group(MATCHER_LINE))
                .setSeverity(Severity.WARNING_NORMAL)
                .setCategory(matcher.group(MATCHER_CATEGORY))
                .setMessage(message.toString())
                .buildOptional();
    }
}
