package edu.hm.hafner.analysis.parser;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

/**
 * A parser for the EmbeddedEngineer EA Code Generator tool log files.
 *
 * @author Eva Habeeb
 */
public class EmbeddedEngineerParser extends IssueParser {
    private static final long serialVersionUID = -1251248150731418714L;

    private static final Pattern LOG_BEGINNING_PATTERN = Pattern.compile("\\[.*?\\].*");
    private static final Pattern HEADER_PATTERN = Pattern.compile(
            "^([^\\(Start)]*)(Starting code generation for)\\s(?<file>.*\\})");
    private static final Pattern SPECIAL_WARNING_PATTERN = Pattern.compile(
            "^\\[([^\\]]*)\\]\\s(?<severity>Warn)\\s-\\s(?<description>[^']*)'(?<module>[^']*)"
                    + "'\\s(?<details>\\(?[^{]*)(?<serial>[^)]*\\})");

    private static final Pattern WARNING_PATTERN = Pattern.compile(
            "^\\[([^\\]]*)\\]\\s(?<severity>Error|Warn)\\s-\\s(?<category>.+):\\s(?<description>.+)");

    @Override
    public Report parse(final ReaderFactory reader) throws ParsingException {
        try (Stream<String> lines = reader.readStream()) {
            return parse(lines);
        }
        catch (UncheckedIOException e) {
            throw new ParsingException(e);
        }
    }

    private List<String> ReadLogInfo(final Iterator<String> lineIterator)
    {
        List<String> logInfo = new ArrayList<>();

        while (lineIterator.hasNext())
        {
            String currentLine = lineIterator.next();
            Matcher logBeginningMatcher = LOG_BEGINNING_PATTERN.matcher(currentLine);
            if (logBeginningMatcher.matches())
            {
                logInfo.add(currentLine);
            } else {
                int lastIdx = logInfo.size() - 1;
                StringBuilder multiLineInfo = new StringBuilder(logInfo.get(lastIdx));
                multiLineInfo.append(" ").append(currentLine);
                logInfo.set(lastIdx, multiLineInfo.toString());
            }

        }
        return logInfo;
    }

    private Report parse(final Stream<String> lines) {
        try (IssueBuilder builder = new IssueBuilder()) {
            Iterator<String> lineIterator = lines.iterator();
            String file = parseFileName(lineIterator);
            Report report = new Report();
            List<String> logLines = this.ReadLogInfo(lineIterator);
            for (String logLine:logLines) {

                Matcher matcher = SPECIAL_WARNING_PATTERN.matcher(logLine);
                Matcher warningMatcher = WARNING_PATTERN.matcher(logLine);

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
                        builder.setCategory(setCategory(logLine));
                        builder.setModuleName(matcher.group("module"));
                    }
                    else {
                        group = warningMatcher.group("severity");
                        builder.setCategory(warningMatcher.group("category"));
                        description = String.format("%s %s",
                                warningMatcher.group("category"),
                                warningMatcher.group("description"));
                    }
                    Severity priority = mapPriority(logLine, group);
                    builder.setDescription(description);
                    builder.setFileName(file);
                    builder.setSeverity(priority);
                    report.add(builder.build());
                }
            }
            return report;
        }
    }

    private String parseFileName(final Iterator<String> lineIterator) {
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