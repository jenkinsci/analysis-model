package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the Diab C++ compiler warnings.
 *
 * @author Yuta Namiki
 */
public class DiabCParser extends RegexpLineParser {
    private static final long serialVersionUID = -1251248150596418456L;

    private static final String DIAB_CPP_WARNING_PATTERN = "^\\s*\"(.*)\"\\s*,\\s*line\\s*(\\d+)\\s*:\\s*"
            + "(info|warning|error|fatal\\serror)\\s*\\((?:dcc|etoa):(\\d+)\\)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link DiabCParser}.
     */
    public DiabCParser() {
        super(DIAB_CPP_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(matcher.group(4))
                .setMessage(matcher.group(5))
                .setPriority(mapPriority(matcher))
                .build();
    }

    private Priority mapPriority(final Matcher matcher) {
        if ("info".equalsIgnoreCase(matcher.group(3))) {
            return Priority.LOW;
        }
        else if ("warning".equalsIgnoreCase(matcher.group(3))) {
            return Priority.NORMAL;
        }
        else {
            return Priority.HIGH;
        }
    }
}

