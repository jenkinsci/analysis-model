package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
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
     * Matches the beginning of a Modelsim/Questa message "** [priority] : [The remainder of the message]".
     */
    private static final String MSG_REGEX = "\\*\\*\\s+(?<priority>\\w+):\\s+(?<message>.*)";

    /**
     * The first capture group captures the message type such as "vlog-###" or "vsim-###".
     * The second group matches an optional filename in the form of filename.sv(line-number).
     * The third group matches the rest of the message.
     */
    private static final Pattern VSIM_PATTERN = Pattern.compile(
            "\\((?<category>v\\w+-\\d+)\\)(?: (?<filename>\\S*)\\((?<line>\\d+)\\):)? (?<message>.*)");

    /**
     * The first capture group matches the filename and line number.
     * The second capture group matches the warning type such as "vlog-###" or "vsim-###".
     * The third group matches the rest of the message.
     */
    private static final Pattern VLOG_PATTERN = Pattern.compile(
            "at (?<filename>\\S*)\\((?<line>\\d+)\\): \\((?<category>v\\w+-\\d+)\\) (?<message>.*)");
    
    /**
     * The first capture group matches the timestamp for the message on the previous line.
     * The iteration is ignored.
     * \w* matches a word such as "module" or "protected".
     * The next capture group captures the path of the module.
     * The next capture groups capture the File name and Line number.
     */
    private static final Pattern TIME_FILE_PATTERN = Pattern.compile(
            "# {4}Time: (?<simtime>\\d* \\ws)(?: {2}Iteration: \\d+)? {2}\\w*: (?<module>.\\S*)(?: File: (?<filename>\\S+)(?: Line: (?<line>\\d+))?)?.*");

    
    /**
     * Construct a parser for MentorGraphics Modelsim/Questa logs.
     */
    public MentorParser() {
        super(MSG_REGEX);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {
        clearBuilder(builder);

        builder.guessSeverity(matcher.group("priority"));

        String message = matcher.group("message");
        if (message.contains("while parsing file")) {
            parseVlogMessage(lookahead, builder, message);
        } else {
            parseVsimMessage(lookahead, builder, message);
        }

        return builder.buildOptional();
    }

    private void parseVlogMessage(LookaheadStream lookahead, IssueBuilder builder, String message) {
        while (!lookahead.peekNext().startsWith("# ** at ")) {
            lookahead.next();
        }

        // If we can refine the message, the do.
        // Else just return what we got passed.
        Matcher vlog = VLOG_PATTERN.matcher(lookahead.next().substring(5));
        if (vlog.matches()) {
            builder.setFileName(vlog.group("filename"));
            builder.setLineStart(vlog.group("line"));
            builder.setCategory(vlog.group("category"));
            message = vlog.group("message");
        }
        builder.setDescription("");
        builder.setMessage(message);
    }

    private void parseVsimMessage(LookaheadStream lookahead, IssueBuilder builder, String message) {
        builder.setDescription(parseSimTime(lookahead, builder));

        // If we can refine the message, the do.
        // Else just return what we got passed.
        Matcher vsim = VSIM_PATTERN.matcher(message);
        if (vsim.matches()) {
            builder.setCategory(vsim.group("category"));
            builder.setFileName(vsim.group("filename"));
            builder.setLineStart(vsim.group("line"));
            message = vsim.group("message");
        }
        builder.setMessage(message);
    }

    private String parseSimTime(LookaheadStream lookahead, IssueBuilder builder) {
        StringBuilder description = new StringBuilder();
        String timeLine = "";
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
            builder.setModuleName(tf.group("module"));
            builder.setFileName(tf.group("filename"));
            builder.setLineStart(tf.group("line"));
        }
        return description.toString();
    }

    private void clearBuilder(IssueBuilder builder) {
        builder.setModuleName(null);
        builder.setFileName(null);
        builder.setLineStart(null);
        builder.setCategory(null);
    }

    @Override
    protected boolean isLineInteresting(String line) {
        return line.startsWith("# **");
    }

}
