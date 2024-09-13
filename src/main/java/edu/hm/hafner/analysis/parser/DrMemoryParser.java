package edu.hm.hafner.analysis.parser;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static edu.hm.hafner.analysis.util.IntegerParser.*;

/**
 * A parser for the Dr. Memory Errors.
 *
 * @author Wade Penson
 */
public class DrMemoryParser extends LookaheadParser {
    private static final long serialVersionUID = 7195239138601238590L;
    private static final String DR_MEMORY_WARNING_PATTERN = "Error #\\d+: (.*)";

    /** Regex pattern to extract the file path from a line. */
    private static final Pattern FILE_PATH_PATTERN = Pattern.compile(
            "#\\s*\\d+.*?\\[(?<file>.*/?.*):(?<line>\\d+)]");

    /** Regex pattern to extract the jenkins path from file path. */
    private static final Pattern JENKINS_PATH_PATTERN = Pattern
            .compile(".*?(/jobs/.*?/workspace/|workspace/)");

    /**
     * Creates a new instance of {@link DrMemoryParser}.
     */
    public DrMemoryParser() {
        super(DR_MEMORY_WARNING_PATTERN);
    }

    @Override @SuppressFBWarnings(value = "POTENTIAL_XML_INJECTION", justification = "Message is cleaned in UI")
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder)
            throws ParsingException {
        String header = matcher.group(1);

        var messageBuilder = new StringBuilder(header);
        while (lookahead.hasNext("Elapsed time")) {
            messageBuilder.append("<br>");
            messageBuilder.append(lookahead.next());
        }

        var stacktraceBuilder = new StringBuilder();
        while (lookahead.hasNext("^#.*")) {
            String stackTrace = lookahead.next();
            stacktraceBuilder.append(stackTrace);
            stacktraceBuilder.append("<br>");

            messageBuilder.append("<br>");
            messageBuilder.append(stackTrace);
        }

        while (lookahead.hasNext("^Note:")) {
            messageBuilder.append("<br>");
            messageBuilder.append(lookahead.next());
        }

        if (StringUtils.isNotBlank(header)) {
            assignCategoryAndSeverity(builder, header.toLowerCase(Locale.ENGLISH));
        }
        findOriginatingErrorLocation(stacktraceBuilder.toString(), builder);

        if (messageBuilder.length() == 0) {
            messageBuilder.append("Unknown Dr. Memory Error");
        }
        return builder.setMessage(messageBuilder.toString()).buildOptional();
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private void assignCategoryAndSeverity(final IssueBuilder builder, final String header) {
        builder.setCategory("Unknown");
        builder.setSeverity(Severity.WARNING_NORMAL);

        if (header.startsWith("unaddressable access")) {
            builder.setCategory("Unaddressable Access");
            builder.setSeverity(Severity.WARNING_HIGH);
        }
        else if (header.startsWith("uninitialized read")) {
            builder.setCategory("Uninitialized Read");
            builder.setSeverity(Severity.WARNING_HIGH);
        }
        else if (header.startsWith("invalid heap argument")) {
            builder.setCategory("Invalid Heap Argument");
            builder.setSeverity(Severity.WARNING_HIGH);
        }
        else if (header.startsWith("reachable leak")) {
            builder.setCategory("Reachable Leak");
            builder.setSeverity(Severity.WARNING_HIGH);
        }
        else if (header.startsWith("leak")) {
            builder.setCategory("Leak");
            builder.setSeverity(Severity.WARNING_HIGH);
        }
        else if (header.startsWith("possible leak")) {
            builder.setCategory("Possible Leak");
        }
        else if (header.startsWith("gdi usage error")) {
            builder.setCategory("GDI Usage Error");
        }
        else if (header.startsWith("handle leak")) {
            builder.setCategory("Handle Leak");
        }
        else if (header.startsWith("warning")) {
            builder.setCategory("Warning");
        }
    }

    /**
     * Looks through each line of the stack trace to try and determine the file path and line number where the error
     * originates from within the user's code. This assumes that the user's code is within the Jenkins workspace folder.
     * Otherwise, the file path and line number is obtained from the top of the stack trace.
     *
     * @param stackTrace
     *         the stack trace in the correct order
     * @param builder
     *         the issue builder
     */
    private void findOriginatingErrorLocation(final String stackTrace, final IssueBuilder builder) {
        builder.setFileName("-");
        builder.setLineStart(0);
        for (String line : stackTrace.split("<br>", -1)) {
            Matcher pathMatcher = FILE_PATH_PATTERN.matcher(line);

            if (pathMatcher.find()) {
                String path = pathMatcher.group("file");
                builder.setFileName(path);
                builder.setLineStart(parseInt(pathMatcher.group("line")));

                Matcher jenkinsPathMatcher = JENKINS_PATH_PATTERN.matcher(path);
                if (jenkinsPathMatcher.find()) {
                    return;
                }
            }
        }
    }
}

