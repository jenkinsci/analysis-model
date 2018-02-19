package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for messages from the Intel C and Fortran compilers.
 *
 * @author Vangelis Livadiotis
 */
public class IntelParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 8409744276858003050L;
    private static final String INTEL_PATTERN = "^(.*)\\((\\d*)\\)?:(?:\\s*\\(col\\. (\\d+)\\))?.*("
            + "(?:remark|warning|error)\\s*#*\\d*)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link IntelParser}.
     */
    public IntelParser() {
        super(INTEL_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error") || line.contains("remark");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = StringUtils.capitalize(matcher.group(4));

        Priority priority;
        if (StringUtils.startsWith(category, "Remark")) {
            priority = Priority.LOW;
        }
        else if (StringUtils.startsWith(category, "Error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setColumnStart(parseInt(matcher.group(3)))
                .setCategory(category)
                .setMessage(matcher.group(5))
                .setPriority(priority)
                .build();
    }
}
