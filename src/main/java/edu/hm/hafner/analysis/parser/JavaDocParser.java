package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for the ant JavaDoc compiler warnings.
 *
 * @author Ulli Hafner
 */
public class JavaDocParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 7127568148333474921L;
    private static final String JAVA_DOC_WARNING_PATTERN = "(?:\\s*\\[(?:javadoc|WARNING|ERROR)\\]\\s*)?(?:(?:(?:Exit" +
            " code: \\d* - )?(.*):(\\d+))|(?:\\s*javadoc\\s*)):\\s*(warning|error)\\s*[-:]\\s*(.*)";

    /**
     * Creates a new instance of {@link JavaDocParser}.
     */
    public JavaDocParser() {
        super("javadoc", JAVA_DOC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("javadoc") || line.contains("@") || (line.contains("error") && line.contains("ERROR"));
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String message = matcher.group(4);
        String type = matcher.group(3);
        Priority priority;
        if ("warning".equals(type)) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }
        String fileName = StringUtils.defaultIfEmpty(matcher.group(1), " - ");

        return issueBuilder().setFileName(fileName).setLineStart(parseInt(matcher.group(2))).setMessage(message)
                             .setPriority(priority).build();
    }
}

