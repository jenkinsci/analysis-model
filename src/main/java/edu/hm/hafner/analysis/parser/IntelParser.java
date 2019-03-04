package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for messages from the Intel C and Fortran compilers.
 *
 * @author Vangelis Livadiotis
 */
public class IntelParser extends RegexpLineParser {
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
    protected String interestingLineContent(String line) {
        if (line.contains("warning")
                || line.contains("error")
                || line.contains("remark")
                || line.contains("message")) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = StringUtils.capitalize(matcher.group(5));

        Severity priority;
        if (StringUtils.startsWith(category, "Remark") || StringUtils.startsWith(category, "Message")) {
            priority = Severity.WARNING_LOW;
        }
        else if (StringUtils.startsWith(category, "Error")) {
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
