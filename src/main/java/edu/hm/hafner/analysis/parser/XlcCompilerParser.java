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

    private static final String XLC_WARNING_PATTERN = ANT_TASK + ".*((?:[A-Z]+|[0-9]+-)[0-9]+)* ?\\([USEWI]\\)\\s*("
            + ".*)$";
    private static final String XLC_WARNING_PATTERN_WITH_LINE = ANT_TASK + "\"?([^\"]*)\"?, line ([0-9]+)\\.[0-9]+:( "
            + "(?:[A-Z]+|[0-9]+-)[0-9]+)? \\(([USEWI])\\)\\s*(.*)$";
    private static final String XLC_WARNING_PATTERN_NO_LINE = ANT_TASK + "\\s*((?:[A-Z]+|[0-9]+-)[0-9]+)?:? ?\\("
            + "([USEWI])\\)( INFORMATION:)?\\s*(.*)$";
    private static final Pattern PATTERN_WITH_LINE = Pattern.compile(XLC_WARNING_PATTERN_WITH_LINE);
    private static final Pattern PATTERN_WITHOUT_LINE = Pattern.compile(XLC_WARNING_PATTERN_NO_LINE);

    /**
     * Creates a new instance of {@link XlcCompilerParser}.
     */
    public XlcCompilerParser() {
        super(XLC_WARNING_PATTERN);
    }

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
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String line = matcher.group(0);

        Matcher lineMatcher = PATTERN_WITH_LINE.matcher(line);
        if (lineMatcher.find()) {
            return builder.setFileName(lineMatcher.group(1))
                    .setLineStart(parseInt(lineMatcher.group(2)))
                    .setCategory(lineMatcher.group(3).trim())
                    .setMessage(lineMatcher.group(5))
                    .setPriority(toPriority(lineMatcher.group(4)))
                    .build();
        }

        return createIssueWithoutLine(builder, line);
    }

    private Issue createIssueWithoutLine(final IssueBuilder builder, final String line) {
        Matcher matcher = PATTERN_WITHOUT_LINE.matcher(line);
        if (matcher.find()) {
            return builder.setFileName("")
                    .setLineStart(0)
                    .setCategory(matcher.group(1).trim())
                    .setMessage(matcher.group(4))
                    .setPriority(toPriority(matcher.group(2)))
                    .build();
        }
        return FALSE_POSITIVE;
    }
}

