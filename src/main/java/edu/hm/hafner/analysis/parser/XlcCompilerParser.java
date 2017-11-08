package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for IBM xlC compiler warnings.
 *
 * @author Andrew Gvozdev
 */
public class XlcCompilerParser extends RegexpLineParser {
    private static final long serialVersionUID = 5490211629355204910L;
    private static final String XLC_WARNING_PATTERN = ANT_TASK + ".*((?:[A-Z]+|[0-9]+-)[0-9]+)* ?\\([USEWI]\\)\\s*(" +
            ".*)$";

    private static final String XLC_WARNING_PATTERN_WITH_LINE = ANT_TASK + "\"?([^\"]*)\"?, line ([0-9]+)\\.[0-9]+:( " +
            "(?:[A-Z]+|[0-9]+-)[0-9]+)? \\(([USEWI])\\)\\s*(.*)$";
    private static final String XLC_WARNING_PATTERN_NO_LINE = ANT_TASK + "\\s*((?:[A-Z]+|[0-9]+-)[0-9]+)?:? ?\\(" +
            "([USEWI])\\)( INFORMATION:)?\\s*(.*)$";
    private static final Pattern PATTERN_1 = Pattern.compile(XLC_WARNING_PATTERN_WITH_LINE);
    private static final Pattern PATTERN_2 = Pattern.compile(XLC_WARNING_PATTERN_NO_LINE);

    /**
     * Creates a new instance of {@link XlcCompilerParser}.
     */
    public XlcCompilerParser() {
        super("xlc", XLC_WARNING_PATTERN);
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private Priority toPriority(final String severity) {
        switch (severity.charAt(0)) {
            case 'U':
            case 'S':
            case 'E':
                return Priority.HIGH;
            case 'W':
                return Priority.NORMAL;
            case 'I':
                return Priority.LOW;
            default:
                return Priority.HIGH;
        }
    }

    @Override
    protected Issue createWarning(final Matcher matcher0, final IssueBuilder builder) {
        String line = matcher0.group(0);
        Matcher matcher = PATTERN_1.matcher(line);
        if (matcher.find()) {
            String fileName = matcher.group(1);
            int lineNumber = parseInt(matcher.group(2));
            String category = matcher.group(3).trim();
            String severity = matcher.group(4);
            Priority priority = toPriority(severity);
            String message = matcher.group(5);
            return builder.setFileName(fileName).setLineStart(lineNumber).setCategory(category)
                                 .setMessage(message).setPriority(priority).build();
        }
        matcher = PATTERN_2.matcher(line);
        if (matcher.find()) {
            String fileName = "";
            int lineNumber = 0;
            String category = matcher.group(1).trim();
            String severity = matcher.group(2);
            Priority priority = toPriority(severity);
            String message = matcher.group(4);
            return builder.setFileName(fileName).setLineStart(lineNumber).setCategory(category)
                                 .setMessage(message).setPriority(priority).build();
        }
        return FALSE_POSITIVE;
    }
}

