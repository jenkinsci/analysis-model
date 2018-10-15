package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpDocumentParser;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 * @author Jason Faust
 */
public class EclipseParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 425883472788422955L;

    /** Pattern for eclipse warnings. */
    @VisibleForTesting
    public static final String ANT_ECLIPSE_WARNING_PATTERN =
            "(?:\\[?(?:INFO|WARNING|ERROR)\\]?.*)?" + // Ignore leading type (output embedded in output)
            "\\[?(INFO|WARNING|ERROR)\\]?" +          // group 1 'type': INFO, WARNING or ERROR in optional []
            "\\s*(?:in)?" +                           // optional " in"
            "\\s*(.*)" +                              // group 2 'filename'
            "(?:\\(at line\\s*(\\d+)\\)|" +           // either group 3 'lineNumber': at line dd
            ":\\[(\\d+)).*" +                         // or group 4 'rowNumber': eg :[row,col] - col ignored
            "(?:\\r?\\n[^\\^\\n]*)+?" +               // 1+ ignored lines (no column pointer) eg source excerpt
            "\\r?\\n.*\\t([^\\^]*)" +                 // newline then group 5 (indent for column pointers)
            "([\\^]+).*" +                            // group 6 column pointers (^^^^^)
            "\\r?\\n(?:\\s*\\[.*\\]\\s*)?" +          // newline then optional ignored text in [] (eg [javac])
            "(.*)";                                   // group 7 'message'

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(ANT_ECLIPSE_WARNING_PATTERN, true);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(1));
        Severity priority;
        switch (type) {
            case "ERROR":
                priority = Severity.ERROR;
                break;
            case "INFO":
                priority = Severity.WARNING_LOW;
                break;
            default:
                priority = Severity.WARNING_NORMAL;
                break;
        }

        // Columns are start index to after last index, 1 based index.
        int columnStart = StringUtils.defaultString(matcher.group(5)).length() + 1;
        int columnEnd = columnStart + matcher.group(6).length();

        return builder
                .setFileName(matcher.group(2))
                .setLineStart(parseInt(getLine(matcher)))
                .setColumnStart(columnStart)
                .setColumnEnd(columnEnd)
                .setMessage(matcher.group(7))
                .setSeverity(priority)
                .build();
    }

    private String getLine(final Matcher matcher) {
        String eclipse34 = matcher.group(3);
        String eclipse38 = matcher.group(4);

        return StringUtils.defaultIfEmpty(eclipse34, eclipse38);
    }
}

