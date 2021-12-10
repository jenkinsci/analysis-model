package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.util.IntegerParser.*;

/**
 * A parser for Cadence Incisive Enterprise Simulator.
 *
 * @author Andrew 'Necromant' Andrianov
 */
public class CadenceIncisiveParser extends LookaheadParser {
    private static final long serialVersionUID = -3251791089328958452L;

    private static final String CADENCE_MESSAGE_PATTERN = "(" + "(^[a-zA-Z]+): \\*([a-zA-Z]),([a-zA-Z]+): (.*) "
            + "\\[File:(.*), Line:(.*)\\]." //ncelab vhdl warning
            + ")|(" + "(^[a-zA-Z]+): \\*([a-zA-Z]),([a-zA-Z]+) \\((.*),([0-9]+)\\|([0-9]+)\\): (.*)$" //Warning/error with filename
            + ")|(" + "(^g?make\\[.*\\]: Entering directory)\\s*(['`]((.*))\\')" // make: entering directory
            + ")|(" + "(^[a-zA-Z]+): \\*([a-zA-Z]),([a-zA-Z]+): (.*)$" //Single generic warning
            + ")";

    /**
     * Creates a new instance of {@link CadenceIncisiveParser}.
     */
    public CadenceIncisiveParser() {
        super(CADENCE_MESSAGE_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String tool;
        String type;
        String category;
        String message;
        String fileName;
        int lineNumber = 0;
        Severity priority;

        if (matcher.group(1) != null) {
            /* vhdl warning from ncelab */
            tool = matcher.group(2);
            type = matcher.group(3);
            category = matcher.group(4);
            fileName = matcher.group(6);
            lineNumber = parseInt(matcher.group(7));
            message = matcher.group(5);
            priority = Severity.WARNING_NORMAL;
        }
        else if (matcher.group(16) != null) {
            return Optional.empty();
        }
        else if (matcher.group(8) != null) {
            tool = matcher.group(9);
            type = matcher.group(10);
            category = matcher.group(11);
            fileName = matcher.group(12);
            lineNumber = parseInt(matcher.group(13));
            message = matcher.group(15);
            priority = Severity.WARNING_NORMAL;
        }
        else if (matcher.group(21) != null) {
            tool = matcher.group(22);
            type = matcher.group(23);
            category = matcher.group(24);
            fileName = StringUtils.EMPTY;
            message = matcher.group(25);
            priority = Severity.WARNING_LOW;
        }
        else {
            return Optional.empty(); /* Should never happen! */
        }

        if (equalsIgnoreCase(type, "E")) {
            priority = Severity.WARNING_HIGH;
            category = "Error (" + tool + "): " + category;
        }
        else {
            category = "Warning (" + tool + "): " + category;
        }

        // Filename should never be null here, unless someone updates from the code above fail
        if (fileName == null) {
            return Optional.empty();
        }
        return builder.setFileName(fileName).setLineStart(lineNumber).setCategory(category)
                .setMessage(message).setSeverity(priority).buildOptional();
    }
}
