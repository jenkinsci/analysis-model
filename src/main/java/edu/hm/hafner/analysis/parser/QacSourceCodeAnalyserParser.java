package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for PRQA QA-C Sourcecode Analyser warnings.
 *
 * @author Sven Lübke
 */
public class QacSourceCodeAnalyserParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -8104046102312005968L;

    /** Pattern of QA-C Sourcecode Analyser warnings. */
    private static final String QAC_WARNING_PATTERN = "^(.+?)\\((\\d+),(\\d+)\\): (Err|Msg)\\((\\d+):(\\d+)\\) (.+?)$";

    /**
     * Creates a new instance of {@code QACSourceCodeAnalyserParser}.
     */
    public QacSourceCodeAnalyserParser() {
        super(QAC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority;
        String category;
        if (equalsIgnoreCase(matcher.group(4), "err")) {
            priority = Severity.WARNING_HIGH;
            category = "ERROR";
        }
        else {
            priority = Severity.WARNING_NORMAL;
            category = "Warning";
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(matcher.group(7))
                .setSeverity(priority)
                .buildOptional();
    }
}
