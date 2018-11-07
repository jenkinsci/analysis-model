package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the Perl::Critic warnings.
 *
 * @author Mihail Menev, menev@hm.edu
 */
public class PerlCriticParser extends RegexpLineParser {
    private static final long serialVersionUID = -6481203155449490873L;

    private static final String PERLCRITIC_WARNING_PATTERN = "(?:(.*?):)?(.*)\\s+at\\s+line\\s+(\\d+),\\s+column\\s+"
            + "(\\d+)\\.\\s*(?:See page[s]?\\s+)?(.*)\\.\\s*\\(?Severity:\\s*(\\d)\\)?";
    private static final int SEVERITY_LOW_LIMIT = 2;
    private static final int SEVERITY_NORMAL_LIMIT = 4;

    /**
     * Creates a new instance of {@link PerlCriticParser}.
     */
    public PerlCriticParser() {
        super(PERLCRITIC_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String filename;
        if (matcher.group(1) == null) {
            filename = "-";
        }
        else {
            filename = matcher.group(1);
        }

        String message = matcher.group(2);
        int line = parseInt(matcher.group(3));
        int column = parseInt(matcher.group(4));
        String category = matcher.group(5);
        Severity priority = checkPriority(Integer.parseInt(matcher.group(6)));

        return builder.setFileName(filename).setLineStart(line).setColumnStart(column).setCategory(category)
                .setMessage(message).setSeverity(priority).build();
    }

    /**
     * Checks the severity level, parsed from the warning and return the priority level.
     *
     * @param priority
     *         the severity level of the warning.
     *
     * @return the priority level.
     */
    private Severity checkPriority(final int priority) {
        if (priority < SEVERITY_LOW_LIMIT) {
            return Severity.WARNING_LOW;
        }
        else if (priority < SEVERITY_NORMAL_LIMIT) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }
}