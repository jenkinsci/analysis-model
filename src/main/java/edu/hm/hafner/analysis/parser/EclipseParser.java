package edu.hm.hafner.analysis.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 * @author Jason Faust
 */
public class EclipseParser extends LookaheadParser {
    private static final long serialVersionUID = 425883472788422955L;

    private static final String ECLIPSE_FIRST_LINE_REGEXP =
            ".*\\d+\\.\\s*(?<severity>WARNING|ERROR|INFO) in (?<file>.*)\\s*\\(at line (?<line>\\d+)\\)";
    private static final Pattern ANT_PREFIX = Pattern.compile("\\[.*\\] ");

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return !isXmlFile(readerFactory);
    }

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(ECLIPSE_FIRST_LINE_REGEXP);
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
        builder.setSeverity(mapTypeToSeverity(matcher.group("severity")))
                .setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"));

        List<String> context = new ArrayList<>();
        while (!lookahead.hasNext("^.*----------.*$") && lookahead.hasNext()) {
            context.add(lookahead.next());
        }

        int size = context.size();
        if (size > 1) {
            extractMessage(builder, context.get(size - 1));
            extractColumn(builder, context.get(size - 2));
        }

        return builder.buildOptional();
    }

    static void extractColumn(final IssueBuilder builder, final String columns) {
        if (columns.contains("\t")) {
            Pattern columnPattern = Pattern.compile(".*\\t([^\\\\^]*)([\\\\^]+).*");
            Matcher columnMatcher = columnPattern.matcher(columns);
            if (columnMatcher.matches()) {
                // Columns are a closed range, 1 based index.
                int columnStart = StringUtils.defaultString(columnMatcher.group(1)).length() + 1;
                int columnEnd = columnStart + columnMatcher.group(2).length() - 1;
                builder.setColumnStart(columnStart).setColumnEnd(columnEnd);
            }
        }
        else {
            String stripped = RegExUtils.removeFirst(columns, ANT_PREFIX);
            int pos = 0;
            while (pos < stripped.length() && stripped.charAt(pos) == ' ') {
                pos++;
            }
            int columnStart = pos + 1;
            while (pos < stripped.length() && stripped.charAt(pos) == '^') {
                pos++;
            }
            int columnEnd = pos;
            builder.setColumnStart(columnStart).setColumnEnd(columnEnd);
        }
    }

    static void extractMessage(final IssueBuilder builder, final String message) {
        Pattern ant = Pattern.compile("^(?:.*\\[.*\\])?\\s*(.*)");
        Matcher messageMatcher = ant.matcher(message);
        if (messageMatcher.matches()) {
            builder.setMessage(messageMatcher.group(1));
        }
    }
}

