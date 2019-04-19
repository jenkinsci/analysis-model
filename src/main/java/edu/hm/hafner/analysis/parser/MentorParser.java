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

    /**
     * Matches the beginning of a Modelsim questa message "** [priority] : [The remainder of the message]".
     * The first capture group is the priority, the second capture group is the rest of the message to be parsed later.
     */
    private static final String MSG_REGEX = "\\*\\*\\s+(\\w+):\\s+(.*)";

    /**
     * The first capture group captures the message type such as "vlog-###" or "vsim-###".
     * The second group matches an optional filename in the form of filename.v(line-number).
     * The third group matches the rest of the message.
     */
    private static final String VSIM_REGEX = "\\((v\\w+-\\d+)\\)(?: (\\S*)\\((\\d+)\\):)? (.*)";
    private static final Pattern VSIM_PATTERN = Pattern.compile(VSIM_REGEX);

    /**
     * The first capture group matches the filename and line number.
     * The second capture group matches the warning type such as "vlog-###" or "vsim-###".
     * The third group matches the rest of the message.
     */
    private static final String VLOG_REGEX = "at (\\S*)\\((\\d+)\\): \\((v\\w+-\\d+)\\) (.*)";
    private static final Pattern VLOG_PATTERN = Pattern.compile(VLOG_REGEX);
    
    /**
     * The first capture group matches the timestamp for the message on the previous line.
     * The iteration is ignored.
     * \w* matches a word such as "module" or "protected".
     * The next capture group captures the path of the module.
     * The next capture groups capture the File name and Line number.
     */
    private static final String TIME_FILE_REGEX = "# {4}Time: (\\d* \\ws)(?: {2}Iteration: \\d+)? {2}\\w*: (.\\S*)(?: File: (\\S+)(?: Line: (\\d+))?)?.*";
    private static final Pattern TIME_FILE_PATTERN = Pattern.compile(TIME_FILE_REGEX);

    
    /**
     * Construct a parser for MentorGraphics Modelsim/Questa logs.
     */
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
        String timeLine = "";

        if (message.contains("while parsing file")) {
            while (!lookahead.peekNext().startsWith("# ** at ")) {
                lookahead.next();
            }
            Matcher at = VLOG_PATTERN.matcher(lookahead.next().substring(5));
            if (at.matches()) {
                builder.setFileName(at.group(1));
                builder.setLineStart(at.group(2));
                builder.setCategory(at.group(3));
                message = at.group(4);
            }

        }
        else {
            while (lookahead.hasNext() && !lookahead.peekNext().contains("# **")) {
                timeLine = lookahead.next();
                if (timeLine.startsWith("#    Time:")) {
                    break;
                }
                description.append("<br>");
                description.append(timeLine);
            }
            Matcher tf = TIME_FILE_PATTERN.matcher(timeLine);
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

    @Override
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
