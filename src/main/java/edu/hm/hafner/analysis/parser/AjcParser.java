package edu.hm.hafner.analysis.parser;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingException;


/**
 * A parser for AspectJ (ajc) compiler warnings.
 *
 * @author Tom Diamond
 */
public class AjcParser extends AbstractParser {
    private static final long serialVersionUID = -9123765511497052454L;

    private static final Pattern ESCAPE_CHARACTERS = Pattern.compile((char) 27 + "\\[.*" + (char) 27 + "\\[0m");
    private static final Pattern WARNING_TAG = Pattern.compile("\\[WARNING\\] ");

    static final String ADVICE = "Advice";

    @Override
    public Issues<Issue> parse(@Nonnull final Reader reader, @Nonnull final IssueBuilder builder) throws ParsingException {
        try (BufferedReader br = new BufferedReader(reader)) {
            Issues<Issue> warnings = new Issues<>();

            String line;
            States state = States.START;

            while ((line = br.readLine()) != null) {
                // clean up any ESC characters (e.g. terminal colors)
                line = ESCAPE_CHARACTERS.matcher(line).replaceAll("");

                switch (state) {
                    case START:
                        if (line.startsWith("[INFO] Showing AJC message detail for messages of types")) {
                            state = States.PARSING;
                        }
                        break;
                    case PARSING:
                        if (line.startsWith("[WARNING] ")) {
                            state = States.WAITING_FOR_END;

                            fillMessageAndCategory(builder, line);
                        }
                        break;
                    case WAITING_FOR_END:
                        if (line.startsWith("\t")) {
                            fillFileName(builder, line);
                        }
                        else if ("".equals(line)) {
                            state = States.PARSING;

                            warnings.add(builder.build());
                        }
                        break;
                    default:
                        // not possible
                }
            }

            return warnings;
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    private void fillFileName(@Nonnull final IssueBuilder builder, final String line) {
        int indexOfColon = line.lastIndexOf(':');
        if (indexOfColon != -1) {
            builder.setFileName(line.substring(0, indexOfColon));
            if (line.length() > indexOfColon + 1) {
                builder.setLineStart(parseInt(line.substring(indexOfColon + 1)));
            }
        }
    }

    private void fillMessageAndCategory(@Nonnull final IssueBuilder builder, final String line) {
        String message = WARNING_TAG.matcher(line).replaceAll("");
        String category;
        if (message.contains("is deprecated") || message.contains("overrides a deprecated")) {
            category = AbstractParser.DEPRECATION;
        }
        else if (message.contains("adviceDidNotMatch")) {
            category = AjcParser.ADVICE;
        }
        else {
            category = "";
        }
        builder.setMessage(message);
        builder.setCategory(category);
    }

    /** Available states for the parser. */
    private enum States {
        START, PARSING, WAITING_FOR_END
    }
}
