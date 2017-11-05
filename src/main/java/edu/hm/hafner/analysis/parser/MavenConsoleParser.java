package edu.hm.hafner.analysis.parser;

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for maven console warnings.
 *
 * @author Ullrich Hafner
 */
public class MavenConsoleParser extends FastRegexpLineParser {
    private static final String CONSOLE = "";
    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";
    private static final int MAX_MESSAGE_LENGTH = 4000;

    private static final long serialVersionUID = 1737791073711198075L;

    private static final String PATTERN = "^.*\\[(WARNING|ERROR)\\]\\s*(.*)$";

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super("maven", PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(WARNING) || line.contains(ERROR);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        Priority priority;
        String category;
        if (ERROR.equals(matcher.group(1))) {
            priority = Priority.HIGH;
            category = "Error";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return issueBuilder().setFileName(CONSOLE).setLineStart(getCurrentLine()).setCategory(category)
                             .setMessage(matcher.group(2)).setPriority(priority).build();
    }

    // TODO: post processing is quite slow for large number of warnings, see JENKINS-25278
    @Override
    protected Issues<Issue> postProcessWarnings(final Issues<Issue> warnings) {
        Deque<Issue> condensed = new LinkedList<>();
        int line = -1;
        for (Issue warning : warnings) {
            if (warning.getLineStart() == line + 1 && !condensed.isEmpty()) {
                Issue previous = condensed.getLast();
                if (previous.getPriority() == warning.getPriority()) {
                    condensed.removeLast();
                    if (previous.getMessage().length() + warning.getMessage().length() >= MAX_MESSAGE_LENGTH) {
                        condensed.add(issueBuilder().copy(previous).setLineStart(warning.getLineStart()).build());
                    }
                    else {
                        condensed.add(issueBuilder().copy(previous).setLineStart(warning.getLineStart())
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
        Issues noBlank = new Issues<>();
        for (Issue warning : condensed) {
            if (StringUtils.isNotBlank(warning.getMessage())) {
                noBlank.add(warning);
            }
        }
        return noBlank;
    }

}

