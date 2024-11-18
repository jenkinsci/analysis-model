package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.io.UncheckedIOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the EmbeddedEngineer EA Code Generator tool log files.
 *
 * @author Eva Habeeb
 */
public class EmbeddedEngineerParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -1251248150731418714L;

    private static final String LOG_BEGINNING_PATTERN = "^\\[.*?\\].*";
    private static final Pattern HEADER_PATTERN = Pattern.compile(
            "^([^\\(Start)]*)(Starting code generation for)\\s(?<file>.*\\})");
    private static final Pattern SPECIAL_WARNING_PATTERN = Pattern.compile(
            "^\\[([^\\]]*)\\]\\s(?<severity>Warn)\\s-\\s(?<description>[^']*)'(?<module>[^']*)"
                    + "'\\s(?<details>\\(?[^{]*)(?<serial>[^)]*\\})");

    private static final Pattern WARNING_PATTERN = Pattern.compile(
            "^\\[([^\\]]*)\\]\\s(?<severity>Error|Warn)\\s-\\s(?<category>[^:]*)" + "(:\\s|\\s\\()(?<description>.+)");

    @Override
    public Report parse(final ReaderFactory reader) throws ParsingException {
        try (Stream<String> lines = reader.readStream()) {
            try (LookaheadStream lookahead = new LookaheadStream(lines, reader.getFileName())) {
                return parse(lookahead);
            }
        }
        catch (UncheckedIOException e) {
            throw new ParsingException(e);
        }
    }

    private Report parse(final LookaheadStream lookahead) {
        try (IssueBuilder builder = new IssueBuilder()) {
            String file = parseFileName(lookahead);
            var report = new Report();
            while (lookahead.hasNext() && lookahead.hasNext(LOG_BEGINNING_PATTERN)) {
                String lines = this.readMultipleLines(lookahead);
                Matcher matcher = SPECIAL_WARNING_PATTERN.matcher(lines);
                Matcher warningMatcher = WARNING_PATTERN.matcher(lines);

                if (matcher.matches() || warningMatcher.matches()) {
                    String group;
                    String description;
                    if (matcher.matches()) {
                        group = matcher.group("severity");

                        description = String.format("%s'%s' %s%s",
                                matcher.group("description"),
                                matcher.group("module"),
                                matcher.group("details"),
                                matcher.group("serial"));
                        builder.setCategory(setCategory(lines));
                        builder.setModuleName(matcher.group("module"));
                    }
                    else {
                        group = warningMatcher.group("severity");
                        builder.setCategory(warningMatcher.group("category"));
                        description = String.format("%s %s",
                                warningMatcher.group("category"),
                                warningMatcher.group("description"));
                    }
                    Severity priority = mapPriority(lines, group);
                    builder.setDescription(description);
                    builder.setFileName(file);
                    builder.setSeverity(priority);
                    report.add(builder.build());
                }
            }
            return report;
        }
    }

    private String readMultipleLines(final LookaheadStream lookahead) {
        var multipleLines = new StringBuilder(lookahead.next());
        while (lookahead.hasNext() && !lookahead.hasNext(LOG_BEGINNING_PATTERN)) {
            multipleLines.append(" ").append(lookahead.next());
        }
        return multipleLines.toString();
    }

    private String parseFileName(final LookaheadStream lineIterator) {
        while (lineIterator.hasNext()) {
            String line = lineIterator.next();
            Matcher matcher = HEADER_PATTERN.matcher(line);
            if (matcher.matches()) {
                return matcher.group("file");
            }
        }
        return StringUtils.EMPTY;
    }

    private String setCategory(final String line) {
        if (line.contains("Complex type")) {
            return "Complex type definition without referenced element";
        }
        else if (line.contains("skipped")) {
            return "Code generation skipped";
        }
        else if (line.contains("failed")) {
            return "Code generation failed";
        }
        return "No Category";
    }

    private Severity mapPriority(final String line, final String group) {
        if (line.contains("Complex type") && group.contains("Warn")) {
            return Severity.WARNING_NORMAL;
        }
        else if (line.contains("skipped") && group.contains("Warn")) {
            return Severity.WARNING_NORMAL;
        }
        else if (line.contains("failed") && group.contains("Warn")) {
            return Severity.WARNING_HIGH;
        }
        else if (group.contains("Error")) {
            return Severity.ERROR;
        }
        return Severity.WARNING_NORMAL;
    }
}
