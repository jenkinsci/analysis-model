package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parser for Mentor Graphics Modelsim/Questa Simulator.
 *
 * @author Derrick Gibelyou
 */
public class MentorParser extends LookaheadParser {

    protected static final String MSG_REGEX = "\\*\\*\\s+(\\w+):\\s+(.*)";
    protected static final String VSIM_REGEX = "\\((v\\w+-\\d+)\\)(?: (\\S*)\\((\\d+)\\):)? (.*)";
    protected static final String VLOG_REGEX = "at (\\S*)\\((\\d+)\\): \\((v\\w+-\\d+)\\) (.*)";
    protected static final String TIME_FILE_REGEX = "#    Time: (\\d* \\ws)(?:  Iteration: \\d+)?  \\w*: (.\\S*)(?: File: (\\S+)(?: Line: (\\d+))?)?.*";
    protected static final Pattern TIME_FILE_PATTERN = Pattern.compile(TIME_FILE_REGEX);
    protected static final Pattern VSIM_PATTERN = Pattern.compile(VSIM_REGEX);
    protected static final Pattern VLOG_PATTERN = Pattern.compile(VLOG_REGEX);

    public MentorParser() {
        super(MSG_REGEX);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {

        builder.setModuleName(null);
        builder.setFileName(null);
        builder.setLineStart(null);
        builder.setCategory(null);

        String priority = matcher.group(1);
        String message = matcher.group(2);

        StringBuilder description = new StringBuilder();
        String time_line = "";

        if (message.contains("while parsing file")) {
            while (!lookahead.peekNext().startsWith("# ** at "))
                lookahead.next();
            Matcher at = VLOG_PATTERN.matcher(lookahead.next().substring(5));
            if (at.matches())
            {
                builder.setFileName(at.group(1));
                builder.setLineStart(at.group(2));
                builder.setCategory(at.group(3));
                message = at.group(4);
            }

        }
        else {
            while (lookahead.hasNext() && !lookahead.peekNext().contains("# **")) {
                time_line = lookahead.next();
                if (time_line.startsWith("#    Time:"))
                    break;
                description.append("<br>");
                description.append(time_line);
            }
            Matcher tf = TIME_FILE_PATTERN.matcher(time_line);
            if (tf.matches()) {
                builder.setModuleName(tf.group(2));
                builder.setFileName(tf.group(3));
                builder.setLineStart(tf.group(4));
            }
            Matcher vsim = VSIM_PATTERN.matcher(message);
            if (vsim.matches()) {
                builder.setCategory(vsim.group(1));
                builder.setFileName(vsim.group(2));
                builder.setLineStart(vsim.group(3));
                message = vsim.group(4);
            }
        }

        builder.setMessage(message);
        builder.setDescription(description.toString());
        builder.setOrigin("Modelsim");
        builder.setSeverity(mapPriority(priority));
        return builder.buildOptional();
    }

    protected boolean isLineInteresting(String line) {
        return line.startsWith("# **");
    }

    private Severity mapPriority(final String xilPriority) {
        Severity severity;
        switch (xilPriority) {
            case "Error":
                severity = Severity.ERROR;
                break;
            case "Warning":
                severity = Severity.WARNING_NORMAL;
                break;
            case "Note":
                severity = Severity.WARNING_LOW;
                break;
            default:
                severity = Severity.WARNING_HIGH;
                break;
        }
        return severity;
    }
}
