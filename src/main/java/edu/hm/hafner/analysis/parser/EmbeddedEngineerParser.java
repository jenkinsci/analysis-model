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
    private static String file;

    /**
     * Creates a new instance of {@link EmbeddedEngineerParser}.
     */

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

            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                Pattern pattern1 = Pattern.compile("^Starting code generation for\\s([^)]*)\\)");
                Matcher matcher1 = pattern1.matcher(line);
                boolean matches = matcher1.matches();

                if (matches) {
                    file = matcher1.group(1) + ")";
                }
                Pattern pattern2 = Pattern.compile("^(Warning:)\\s([^\\']*)\\'([^\\']*)\\'\\s(\\(?[^\\(]*)\\(([^)]*)\\)");
                Matcher matcher2 = pattern2.matcher(line);
                matches = matcher2.matches();

                String category = setCategory(line);
                Severity priority = mapPriority(line);

                if (matches) {
                    String description = matcher2.group(2) + "'" + matcher2.group(3) + "' " + matcher2.group(4) + "(" + matcher2.group(5) + ")";
                    builder.setDescription(description);
                    builder.setCategory(category);
                    builder.setFileName(file);
                    builder.setModuleName(matcher2.group(3));
                    builder.setSeverity(priority);
                    report.add(builder.build());
                }
            }
            return report;
        }
    }

    private String setCategory(final String line) {
        String cat = " ";
        if (line.contains("Complex type")) {
            cat = "Complex type definition without referenced element";
        }
        else if (line.contains("skipped")) {
            cat = "Code generation skipped";
        }
        else if (line.contains("failed")) {
            cat = "Code generation failed";
        }
        return cat;
    }

    private Severity mapPriority(final String line) {
        Severity priority = Severity.WARNING_NORMAL;
        if (line.contains("Complex type")) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (line.contains("skipped")) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (line.contains("failed")) {
            priority = Severity.WARNING_HIGH;
        }
        return priority;
    }
}
