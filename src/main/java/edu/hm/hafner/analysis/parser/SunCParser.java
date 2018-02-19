package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the SUN Studio C++ compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class SunCParser extends RegexpLineParser {
    private static final long serialVersionUID = -1251248150596418456L;

    private static final String SUN_CPP_WARNING_PATTERN = "^\\s*\"(.*)\"\\s*,\\s*line\\s*(\\d+)\\s*:\\s*"
            + "(Warning|Error)(?:| .Anachronism.)\\s*(?:, \\s*([^:]*))?\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link SunCParser}.
     */
    public SunCParser() {
        super(SUN_CPP_WARNING_PATTERN);
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
        if ("warning".equalsIgnoreCase(matcher.group(3))) {
            return Priority.NORMAL;
        }
        else {
            return Priority.HIGH;
        }
    }
}

