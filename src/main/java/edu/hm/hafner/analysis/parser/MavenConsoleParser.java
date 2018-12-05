package edu.hm.hafner.analysis.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for maven console warnings.
 *
 * @author Ullrich Hafner
 */
public class MavenConsoleParser extends FastRegexpLineParser {
    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";
    private static final int MAX_MESSAGE_LENGTH = 4000;

    private static final long serialVersionUID = 1737791073711198075L;

    /**
     * Pattern for identifying warning or error maven logs.
     * <pre>
     * Pattern:
     * (.*\s\s|)           -> Capture group 1 matches either empty string (e.g. [WARNING] some log) or some text followed by exactly two
     *                        spaces (e.g. 22:07:27  [WARNING] some log)
     * \[(WARNING|ERROR)\] -> Capture group 2 matches either [WARNING] or [ERROR]
     * \s*                 -> matches zero or more spaces
     * (.*)                -> Capture group 3 matches zero or more characters except line breaks, represents the actual error message
     * </pre>
     * <p>
     * Typical maven logs: 1) 22:07:27  [WARNING] For this reason, future Maven versions might no longer support
     * building such malformed projects. 2) [ERROR] The POM for org.codehaus.groovy.maven:gmaven-plugin:jar:1.1 is
     * missing
     */
    private static final String PATTERN = "^(.*\\s\\s|)\\[(WARNING|ERROR)\\]\\s*(.*)$";

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super(PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(WARNING) || line.contains(ERROR);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String errorOrWarningGroup = matcher.group(2);
        String errorOrWarningMessage = matcher.group(3);

        return builder.setLineStart(getCurrentLine())
                .setMessage(errorOrWarningMessage)
                .setSeverity(extractSeverity(errorOrWarningGroup))
                .build();
    }

    private Severity extractSeverity(final String errorOrWarningGroup) {
        if (ERROR.equals(errorOrWarningGroup)) {
            return Severity.ERROR;
        }
        return Severity.WARNING_NORMAL;
    }

    // TODO: post processing is quite slow for large number of warnings, see JENKINS-25278
    @Override
    protected Report postProcess(final Report warnings) {
        IssueBuilder builder = new IssueBuilder();
        Deque<Issue> condensed = new ArrayDeque<>();
        int line = -1;
        for (Issue warning : warnings) {
            if (warning.getLineStart() == line + 1 && !condensed.isEmpty()) {
                Issue previous = condensed.getLast();
                if (previous.getSeverity().equals(warning.getSeverity())) {
                    condensed.removeLast();
                    if (previous.getMessage().length() + warning.getMessage().length() >= MAX_MESSAGE_LENGTH) {
                        condensed.add(builder.copy(previous).setLineStart(warning.getLineStart()).build());
                    }
                    else {
                        condensed.add(builder.copy(previous).setLineStart(warning.getLineStart())
                                .setMessage(previous.getMessage() + "\n" + warning.getMessage())
                                .build());
                    }
                }
                else {
                    condensed.add(warning);
                }
            }
            else {
                condensed.add(warning);
            }
            line = warning.getLineStart();
        }
        Report noBlank = new Report();
        for (Issue warning : condensed) {
            if (StringUtils.isNotBlank(warning.getMessage())) {
                noBlank.add(warning);
            }
        }
        return noBlank;
    }
}

