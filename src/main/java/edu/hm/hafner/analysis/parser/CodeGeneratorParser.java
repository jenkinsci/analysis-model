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
 * A parser for Simulink Code Generator tool log files.
 *
 * @author Eva Habeeb
 */
public class CodeGeneratorParser extends IssueParser {
    private static final long serialVersionUID = -1251248150731418714L;
    private static final Pattern WARNING_PATTERN = Pattern.compile("^(Warning:)(.*)");

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
                Matcher matcher = WARNING_PATTERN.matcher(line);

                if (matcher.matches()) {
                    String category = setCategory(line);
                    builder.setCategory(category);
                    builder.setDescription(matcher.group(2));
                    builder.setSeverity(Severity.WARNING_NORMAL);
                    report.add(builder.build());
                }
            }
            return report;
        }
    }

    private String setCategory(final String line) {
        if (line.contains("no longer available in the Configuration Parameters")) {
            return "Configuration Parameters Unavailable";
        }
        else if (line.contains("does not support multiword aliases")) {
            return "Multiword Aliases not Supported by Code Generation";
        }
        else if (line.contains("Unnecessary Data Type Conversion")) {
            return "Unnecessary Data Type Conversion";
        }
        else if (line.contains("Cannot close the model")) {
            return "Model Cannot be Closed";
        }
        else if (line.contains("Cannot find library called")) {
            return "Library Not Found";
        }
        else if (line.contains("File not found or permission denied")) {
            return "File not found or permission denied";
        }
        return "Other";
    }
}
