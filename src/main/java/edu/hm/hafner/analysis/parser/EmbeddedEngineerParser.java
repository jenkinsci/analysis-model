package edu.hm.hafner.analysis.parser;

import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
    private static final String HEADER_PATTERN = "^Starting code generation for\\s(?<file>[^)]*)\\)";
    private static final String WARNING_PATTERN = "^(Warning:)\\s(?<description>[^\\']*)\\'(?<module>[^\\']*)\\'\\s"
            + "(?<details>\\(?[^\\(]*)\\((?<serial>[^)]*)\\)";

    @Override
    public Report parse(final ReaderFactory reader) throws ParsingException {
        try (Stream<String> lines = reader.readStream()) {
            return parse(lines);
        }
        catch (UncheckedIOException e) {
            throw new ParsingException(e);
        }
    }

    private Report parse(final Stream<String> lines) {
        try (IssueBuilder builder = new IssueBuilder()) {
            Report report = new Report();
            Iterator<String> lineIterator = lines.iterator();
            Pattern header_pattern = Pattern.compile(HEADER_PATTERN);
            Pattern warning_pattern = Pattern.compile(WARNING_PATTERN);
            String file = "";

            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                Matcher matcher = header_pattern.matcher(line);
                boolean matches = matcher.matches();
                if (matches) {
                    file = matcher.group("file") + ")";
                    break;
                }
            }

            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                Matcher matcher = warning_pattern.matcher(line);
                boolean matches = matcher.matches();

                if (matches) {
                    String category = setCategory(line);
                    Severity priority = mapPriority(line);
                    String description = matcher.group("description") + "'" + matcher.group("module")
                            + "' " + matcher.group("details") + "(" + matcher.group("serial") + ")";
                    builder.setDescription(description);
                    builder.setCategory(category);
                    builder.setFileName(file);
                    builder.setModuleName(matcher.group("module"));
                    builder.setSeverity(priority);
                    report.add(builder.build());
                }
            }
            return report;
        }
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

    private Severity mapPriority(final String line) {
        if (line.contains("Complex type")) {
            return Severity.WARNING_NORMAL;
        }
        else if (line.contains("skipped")) {
            return Severity.WARNING_NORMAL;
        }
        else if (line.contains("failed")) {
            return Severity.WARNING_HIGH;
        }
        return Severity.WARNING_NORMAL;
    }
}
