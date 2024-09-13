package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Parser for Mentor Graphics Modelsim/Questa Simulator.
 *
 * @author Derrick Gibelyou
 */
public class MentorParser extends LookaheadParser {
    public static final long serialVersionUID = 1L;

    /**
     * Matches the beginning of a Modelsim/Questa message "** [priority] : [The remainder of the message]".
     */
    private static final String MSG_REGEX = "\\*\\*\\s+(?<priority>[\\w \\(\\)]+):\\s+(?<message>.*)";

    /**
     * The first capture group captures the message type such as "vlog-###" or "vsim-###".
     * The second group matches an optional filename in the form of filename.sv(line-number).
     * The third group matches the rest of the message.
     */
    private static final Pattern VSIM_PATTERN = Pattern.compile(
            "\\((?<category>v\\w+-\\d+)\\)(?: (?<filename>\\S*)\\((?<line>\\d+)\\):)? (?<message>.*)");

    /**
     * The first capture group matches the filename
     * The second group matches the line number.
     */
    private static final Pattern VLOG_FILE_PATTERN = Pattern.compile(
            "(?<filename>\\S*)\\((?<line>\\d+)\\):");

    /**
     * The first capture group matches the warning type such as "vlog-###" or "vsim-###".
     * The second group matches the rest of the message.
     */
    private static final Pattern VLOG_TYPE_PATTERN = Pattern.compile(
            "\\((?<category>v\\w+-\\d+)\\) (?<message>.*)");

    /**
     * The first capture group matches the timestamp for the message on the previous line.
     * The iteration is ignored.
     * \w* matches a word such as "module" or "protected".
     * The next capture group captures the path of the module.
     * The next capture groups capture the File name and Line number.
     */
    private static final Pattern TIME_FILE_PATTERN = Pattern.compile(
            "# {4}Time: (?<simtime>\\d* \\ws)(?: {2}Iteration: \\d+)? {2}\\w*: (?<module>.\\S*)(?: File: (?<filename>\\S+))?(?: Line: (?<line>\\d+))?.*");

    /**
     * Creates a parser for MentorGraphics Modelsim/Questa logs.
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
            parseLongVlogMessage(lookahead, builder);
        }
        else if (message.contains("vlog-") || message.contains("vopt-")) {
            parseVlogMessage(builder, message);
        }
        else {
            parseVsimMessage(lookahead, builder, message);
        }

        return builder.buildOptional();
    }

    private void parseLongVlogMessage(final LookaheadStream lookahead, final IssueBuilder builder) {
        while (!lookahead.peekNext().startsWith("# ** at ")) {
            lookahead.next();
        }
        parseVlogMessage(builder, lookahead.next().substring(8));
    }

    private void parseVlogMessage(final IssueBuilder builder, final String message) {
        String parsedMessage = message;
        builder.setCategory("vlog");
        // If we can refine the message, the do.
        // Else just return what we got passed.
        Matcher vlog;
        vlog = VLOG_FILE_PATTERN.matcher(message);
        if (vlog.find()) {
            builder.setFileName(vlog.group("filename"));
            builder.setLineStart(vlog.group("line"));
        }
        vlog = VLOG_TYPE_PATTERN.matcher(message);
        if (vlog.find()) {
            builder.setCategory(vlog.group("category"));
            parsedMessage = vlog.group("message");
        }
        builder.setDescription("");
        builder.setMessage(parsedMessage);
    }

    private void parseVsimMessage(final LookaheadStream lookahead, final IssueBuilder builder, final String message) {
        builder.setDescription(parseSimTime(lookahead, builder));

        String parsedMessage = message;
        // If we can refine the message, the do.
        // Else just return what we got passed.
        Matcher vsim = VSIM_PATTERN.matcher(message);
        if (vsim.matches()) {
            builder.setCategory(vsim.group("category"));
            builder.setFileName(vsim.group("filename"));
            builder.setLineStart(vsim.group("line"));
            parsedMessage = vsim.group("message");
        }
        builder.setMessage(parsedMessage);
    }

    @SuppressFBWarnings(value = "POTENTIAL_XML_INJECTION", justification = "Message is cleaned in UI")
    private String parseSimTime(final LookaheadStream lookahead, final IssueBuilder builder) {
        var description = new StringBuilder();
        String timeLine = "";
        while (lookahead.hasNext()
                && lookahead.peekNext().startsWith("# ")
                && !lookahead.peekNext().startsWith("# **")) {
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

    private void clearBuilder(final IssueBuilder builder) {
        builder.setModuleName(null);
        builder.setFileName(null);
        builder.setLineStart(null);
        builder.setCategory("-");
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.startsWith("# ** ") || line.startsWith("** ");
    }
}
