package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for armcc5 compiler warnings.
 *
 * @author Dmytro Kutianskyi
 */
public class Armcc5CompilerParser extends FastRegexpLineParser {
    private static final long serialVersionUID = -2677728927938443701L;

    private static final String ARMCC5_WARNING_PATTERN = "^(.+)\\((\\d+)\\): (warning|error):  #(.+): (.+)$";

    /**
     * Creates a new instance of {@link Armcc5CompilerParser}.
     */
    public Armcc5CompilerParser() {
        super(ARMCC5_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("#");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String fileName = matcher.group(1);
        int lineNumber = parseInt(matcher.group(2));
        String type = matcher.group(3);
        String errorCode = matcher.group(4);
        String message = matcher.group(5);
        Priority priority;

        if ("error".equalsIgnoreCase(type)) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return builder.setFileName(fileName).setLineStart(lineNumber).setMessage(errorCode + " - " + message)
                             .setPriority(priority).build();
    }
}

