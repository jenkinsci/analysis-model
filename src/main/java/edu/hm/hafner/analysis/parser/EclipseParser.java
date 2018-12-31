package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.RegexpDocumentParser;
import edu.hm.hafner.analysis.Severity;
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

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return !isXmlFile(readerFactory);
    }

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(ANT_ECLIPSE_WARNING_PATTERN, true);
    }

    /**
     * Map Eclipse compiler types to {@link Severity} levels.
     * 
     * @param type Non null type string.
     * @return mapped level.
     */
    static Severity mapTypeToSeverity(final String type) {
        switch (type) {
            case "ERROR":
                return Severity.ERROR;
            case "INFO":
                return Severity.WARNING_LOW;
            default:
                return Severity.WARNING_NORMAL;
        }
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(1));
        Severity priority = mapTypeToSeverity(type);

        // Columns are a closed range, 1 based index.
        int columnStart = StringUtils.defaultString(matcher.group(5)).length() + 1;
        int columnEnd = columnStart + matcher.group(6).length() - 1;

        // Use columns to make issue 'unique', range isn't useful for counting in the physical source.
        String range = columnStart + "-" + columnEnd;

        return builder
                .setFileName(matcher.group(2))
                .setLineStart(getLine(matcher))
                .setAdditionalProperties(range)
                .setMessage(matcher.group(7))
                .setSeverity(priority)
                .buildOptional();
    }

    private String getLine(final Matcher matcher) {
        String eclipse34 = matcher.group(3);
        String eclipse38 = matcher.group(4);

        return StringUtils.defaultIfEmpty(eclipse34, eclipse38);
    }
}

