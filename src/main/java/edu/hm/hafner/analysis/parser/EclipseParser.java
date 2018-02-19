package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpDocumentParser;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class EclipseParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 425883472788422955L;

    /** Pattern for eclipse warnings. */
    @VisibleForTesting
    public static final String ANT_ECLIPSE_WARNING_PATTERN = "\\[?(WARNING|ERROR)\\]?" +      // group 1 'type':
            // WARNING or ERROR in optional []
            "\\s*(?:in)?" +                  // optional " in"
            "\\s*(.*)" +                     // group 2 'filename'
            "(?:\\(at line\\s*(\\d+)\\)|" +  // either group 3 'lineNumber': at line dd
            ":\\[(\\d+)).*" +                // or group 4 'rowNumber': eg :[row,col] - col ignored
            "(?:\\r?\\n[^\\^\\n]*){1,3}" +   // 1 or 3 ignored lines (no column pointer) eg source excerpt
            "\\r?\\n([^\\^]*)" +             // newline then group 5 (indent for column pointers)
            "([\\^]+).*" +                   // group 6 column pointers (^^^^^)
            "\\r?\\n(?:\\s*\\[.*\\]\\s*)?" + // newline then optional ignored text in [] (eg [javac])
            "(.*)";                          // group 7 'message'

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(ANT_ECLIPSE_WARNING_PATTERN, true);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(1));
        Priority priority;
        if ("warning".equalsIgnoreCase(type)) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }

        int columnStart = StringUtils.defaultString(matcher.group(5)).length() + 1;
        int columnEnd = columnStart + matcher.group(6).length();

        return builder
                .setFileName(matcher.group(2))
                .setLineStart(parseInt(getLine(matcher)))
                .setColumnStart(columnStart)
                .setColumnEnd(columnEnd)
                .setMessage(matcher.group(7))
                .setPriority(priority)
                .build();
    }

    private String getLine(final Matcher matcher) {
        String eclipse34 = matcher.group(3);
        String eclipse38 = matcher.group(4);

        return StringUtils.defaultIfEmpty(eclipse34, eclipse38);
    }
}

