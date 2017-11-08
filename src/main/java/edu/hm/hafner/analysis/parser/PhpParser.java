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

    private static final String PHP_WARNING_PATTERN = "^.*(PHP Warning|PHP Notice|PHP Fatal error|PHP Parse error)" +
            ":\\s+(?:(.+ in (.+) on line (\\d+))|(SOAP-ERROR:\\s+.*))$";

    /**
     * Creates a new instance of {@link PhpParser}.
     */
    public PhpParser() {
        super("php", PHP_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("PHP");
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        String category = matcher.group(1);

        Priority priority = Priority.NORMAL;

        if (category.contains("Fatal") || category.contains("Parse")) {
            priority = Priority.HIGH;
        }

        if (matcher.group(5) != null) {
            return builder.setFileName("-").setLineStart(0).setCategory(category).setMessage(matcher.group(5))
                                 .setPriority(priority).build();
        }
        else {
            String message = matcher.group(2);
            String fileName = matcher.group(3);
            String start = matcher.group(4);
            return builder.setFileName(fileName).setLineStart(Integer.parseInt(start)).setCategory(category)
                                 .setMessage(message).setPriority(priority).build();
        }
    }
}