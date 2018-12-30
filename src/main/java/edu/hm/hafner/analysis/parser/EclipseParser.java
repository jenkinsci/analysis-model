package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 * @author Jason Faust
 */
public class EclipseParser extends LookaheadParser {
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
        super("(?:.*\\d+\\.\\s*(?<severity>WARNING|ERROR|INFO) in (?<file>.*)\\s*\\(at line (?<line>\\d+)\\))"
                + "|(?:\\s*\\[(?<severity2>WARNING|ERROR|INFO)\\]\\s*(?<file2>.*):\\[(?<line2>\\d+)(?:,\\d+)?\\]).*");
    }

    /**
     * Map Eclipse compiler types to {@link Severity} levels.
     *
     * @param type
     *         Non null type string.
     *
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
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        if (StringUtils.isNotBlank(matcher.group("file2"))) {
            builder.setSeverity(mapTypeToSeverity(matcher.group("severity2")))
                    .setFileName(matcher.group("file2"))
                    .setLineStart(matcher.group("line2"));
        }
        else {
            builder.setSeverity(mapTypeToSeverity(matcher.group("severity")))
                    .setFileName(matcher.group("file"))
                    .setLineStart(matcher.group("line"));
        }

        String code = lookahead.next();
        while (lookahead.hasNext("^[^\\t]*$")) {
            code += lookahead.next();
        }
        String columns = lookahead.next();

        Pattern columnPattern = Pattern.compile(".*\\t([^\\\\^]*)([\\\\^]+).*");
        Matcher columnMatcher = columnPattern.matcher(columns);
        if (columnMatcher.matches()) {
            // Columns are a closed range, 1 based index.
            int columnStart = StringUtils.defaultString(columnMatcher.group(1)).length() + 1;
            int columnEnd = columnStart + columnMatcher.group(2).length() - 1;
            builder.setColumnStart(columnStart).setColumnEnd(columnEnd);
        }
        String message = lookahead.next();

        Pattern ant = Pattern.compile("^(?:.*\\[.*\\])?\\s*(.*)");
        Matcher messageMatcher = ant.matcher(message);
        if (messageMatcher.matches()) {
            builder.setMessage(messageMatcher.group(1));
        }

        return builder.buildOptional();
    }
}

