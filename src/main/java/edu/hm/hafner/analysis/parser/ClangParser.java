package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the Clang compiler warnings.
 *
 * @author Neil Davis
 */
public class ClangParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -3015592762345283182L;

    private static final String CLANG_WARNING_PATTERN = "^\\s*(?:\\d+%)?(C\\/C\\+\\+: )?([^%]*?):(\\d+):(?:(\\d+):)?" + "(?:"
            + "(?:\\{\\d+:\\d+-\\d+:\\d+\\})+:)?\\s*(warning|[^\\[\\]]*error):" + "\\s*(.*?)(?:\\[([^\\[]*)\\])?$";
    private static final Pattern IGNORE_FORMAT = Pattern.compile("^-\\[.*\\].*$");

    /**
     * Creates a new instance of {@link ClangParser}.
     */
    public ClangParser() {
        super(CLANG_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var message = matcher.group(6);
        if (IGNORE_FORMAT.matcher(message).matches()) {
            return Optional.empty();
        }

        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setCategory(matcher.group(7))
                .setMessage(message)
                .setSeverity(mapPriority(matcher.group(5)))
                .buildOptional();
    }

    private Severity mapPriority(final String type) {
        if (type.contains("error")) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_NORMAL;
        }
    }
}
