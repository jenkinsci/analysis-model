package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the ant JavaDoc compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavaDocParser extends RegexpLineParser {
    private static final long serialVersionUID = 7127568148333474921L;
    private static final String JAVA_DOC_WARNING_PATTERN = "(?:\\s*\\[(?:javadoc|WARNING|ERROR)\\]\\s*)?(?:(?:(?:Exit"
            + " code: \\d* - )?(.*):(\\d+))|(?:\\s*javadoc\\s*)):\\s*(warning|error)\\s*[-:]\\s*(.*)";
    static final String CATEGORY_JAVADOC = "JavaDoc";
    static final Pattern TAG_PATTERN = Pattern.compile(".*(@\\w+).*");

    /**
     * Creates a new instance of {@link JavaDocParser}.
     */
    public JavaDocParser() {
        super(JAVA_DOC_WARNING_PATTERN);
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains("javadoc")
                || line.contains("@")
                || (line.contains("error") && line.contains("ERROR"))) {
            if (line.length() > 4000) {
                return line.substring(0, 4000);
            }
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = matcher.group(3);

        String message = matcher.group(4);
        Matcher tagMatcher = TAG_PATTERN.matcher(message);
        if (tagMatcher.matches()) {
            builder.setCategory(String.format("%s %s", CATEGORY_JAVADOC, tagMatcher.group(1)));
        }
        else {
            builder.setCategory(CATEGORY_JAVADOC);
        }
        return builder.setFileName(StringUtils.defaultIfEmpty(matcher.group(1), " - "))
                .setLineStart(matcher.group(2))
                .setMessage(message)
                .setSeverity(mapPriority(type))
                .buildOptional();
    }

    private Severity mapPriority(final String type) {
        Severity priority;
        if ("warning".equals(type)) {
            priority = Severity.WARNING_NORMAL;
        }
        else {
            priority = Severity.WARNING_HIGH;
        }
        return priority;
    }
}

