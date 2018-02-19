package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for PHP runtime errors and warnings.
 *
 * @author Shimi Kiviti
 */
public class PhpParser extends FastRegexpLineParser {
    private static final long serialVersionUID = -5154327854315791181L;

    private static final String PHP_WARNING_PATTERN = "^.*(PHP Warning|PHP Notice|PHP Fatal error|PHP Parse error)"
            + ":\\s+(?:(.+ in (.+) on line (\\d+))|(SOAP-ERROR:\\s+.*))$";

    /**
     * Creates a new instance of {@link PhpParser}.
     */
    public PhpParser() {
        super(PHP_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("PHP");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = matcher.group(1);
        builder.setCategory(category).setPriority(mapPriority(category));

        if (matcher.group(5) == null) {
            return builder.setFileName(matcher.group(3))
                    .setLineStart(parseInt(matcher.group(4)))
                    .setMessage(matcher.group(2))
                    .build();
        }
        else {
            return builder.setFileName("-")
                    .setLineStart(0)
                    .setMessage(matcher.group(5))
                    .build();
        }
    }

    private Priority mapPriority(final String category) {
        if (category.contains("Fatal") || category.contains("Parse")) {
            return Priority.HIGH;
        }
        return Priority.NORMAL;
    }
}