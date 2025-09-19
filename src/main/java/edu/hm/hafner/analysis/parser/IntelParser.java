package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * A parser for messages from the Intel C and Fortran compilers.
 *
 * @author Vangelis Livadiotis
 */
public class IntelParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 8409744276858003050L;
    private static final String INTEL_PATTERN = "^(\\d+>)?(.*)\\((\\d*)\\)?:(?:\\s*\\(col\\. (\\d+)\\))?.*("
            + "(?:message|remark|warning|error)\\s*#*\\d*)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link IntelParser}.
     */
    public IntelParser() {
        super(INTEL_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error") || line.contains("remark") || line.contains("message");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var category = StringUtils.capitalize(matcher.group(5));

        Severity priority;
        if (Strings.CS.startsWith(category, "Remark") || Strings.CS.startsWith(category, "Message")) {
            priority = Severity.WARNING_LOW;
        }
        else if (Strings.CS.startsWith(category, "Error")) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }

        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setCategory(category)
                .setMessage(matcher.group(6))
                .setSeverity(priority)
                .buildOptional();
    }
}
