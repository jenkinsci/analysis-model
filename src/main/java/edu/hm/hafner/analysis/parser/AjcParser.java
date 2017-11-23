package edu.hm.hafner.analysis.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

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

    static final String ADVICE = "Advice";

    /**
     * Creates a new instance of {@link AjcParser}.
     */
    public AjcParser() {
        super();
    }

    @Override
    public Issues<Issue> parse(final Reader reader, final IssueBuilder builder) throws ParsingException {
        try (BufferedReader br = new BufferedReader(reader)) {
            Issues<Issue> warnings = new Issues<>();

            String line;
            States state = States.START;
            String message = "";
            String file = "";
            String category = "";
            int lineNo = 0;

            while ((line = br.readLine()) != null) {
                //clean up any ESC characters (e.g. terminal colors)
                line = line.replaceAll((char)27 + "\\[.*" + (char)27 + "\\[0m", "");

                switch (state) {
                    case START:
                        if (line.startsWith("[INFO] Showing AJC message detail for messages of types")) {
                            state = States.PARSING;
                        }
                        break;
                    case PARSING:
                        if (line.startsWith("[WARNING] ")) {
                            state = States.PARSED_WARNING;
                            message = line.replaceAll("\\[WARNING\\] ", "");

                            if (message.contains("is deprecated") || message.contains("overrides a deprecated")) {
                                category = AbstractParser.DEPRECATION;
                            }
                            else if (message.contains("adviceDidNotMatch")) {
                                category = AjcParser.ADVICE;
                            }
                        }
                        break;
                    case PARSED_WARNING:
                        if (line.startsWith("\t")) {
                            state = States.PARSED_FILE;

                            int idx = line.lastIndexOf(":");
                            if (idx != -1) {
                                file = line.substring(0, idx);
                                try {
                                    lineNo = parseInt(line.substring(idx + 1));
                                }
                                catch (IndexOutOfBoundsException ignored) {
                                }
                            }
                        }

                        if ("".equals(line)) {
                            if (!"".equals(message.trim())) {
                                warnings.add(builder.setFileName(file).setLineStart(lineNo).setCategory(category)
                                                           .setMessage(message.trim()).build());
                            }
                            message = "";
                            file = "";
                            category = "";
                            lineNo = 0;
                            state = States.PARSING;
                        }

                        break;
                    case PARSED_FILE:
                    default:
                        if ("".equals(line)) {
                            if (!"".equals(message.trim())) {
                                warnings.add(builder.setFileName(file).setLineStart(lineNo).setCategory(category)
                                                           .setMessage(message.trim()).build());
                            }
                            message = "";
                            file = "";
                            category = "";
                            lineNo = 0;
                            state = States.PARSING;
                        }
                }
            }

            return warnings;
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    private enum States {
        START, PARSING, PARSED_WARNING, PARSED_FILE
    }
}
