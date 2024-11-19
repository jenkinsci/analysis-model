package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

/**
 * A parser for AspectJ (ajc) compiler warnings.
 *
 * @author Tom Diamond
 */
public class AjcParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -9123765511497052454L;

    private static final Pattern ESCAPE_CHARACTERS = Pattern.compile((char) 27 + "\\[.*" + (char) 27 + "\\[0m");
    private static final String WARNING_TAG = "[WARNING] ";

    static final String ADVICE = "Advice";

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
        try (var builder = new IssueBuilder()) {
            var report = new Report();

            var state = States.START;

            Iterator<String> lineIterator = lines.iterator();
            while (lineIterator.hasNext()) {
                var line = lineIterator.next();
                // clean up any ESC characters (e.g. terminal colors)
                line = ESCAPE_CHARACTERS.matcher(line).replaceAll("");

                switch (state) {
                    case START:
                        if (line.startsWith("[INFO] Showing AJC message detail for messages of types")) {
                            state = States.PARSING;
                        }
                        break;
                    case PARSING:
                        if (line.startsWith(WARNING_TAG)) {
                            line = line.substring(WARNING_TAG.length());
                            state = States.WAITING_FOR_END;

                            fillMessageAndCategory(builder, line);
                        }
                        break;
                    case WAITING_FOR_END:
                        if (line.startsWith("\t")) {
                            fillFileName(builder, line);
                        }
                        else if (line.isEmpty()) {
                            state = States.PARSING;

                            report.add(builder.buildAndClean());
                        }
                        break;
                }
            }

            return report;
        }
    }

    private void fillFileName(final IssueBuilder builder, final String line) {
        int indexOfColon = line.lastIndexOf(':');
        if (indexOfColon != -1) {
            builder.setFileName(line.substring(0, indexOfColon));
            if (line.length() > indexOfColon + 1) {
                builder.setLineStart(line.substring(indexOfColon + 1));
            }
        }
    }

    private void fillMessageAndCategory(final IssueBuilder builder, final String line) {
        String category;
        if (line.contains("is deprecated") || line.contains("overrides a deprecated")) {
            category = Categories.DEPRECATION;
        }
        else if (line.contains("adviceDidNotMatch")) {
            category = ADVICE;
        }
        else {
            category = "";
        }
        builder.setMessage(line);
        builder.setCategory(category);
    }

    /** Available states for the parser. */
    private enum States {
        START, PARSING, WAITING_FOR_END
    }
}
