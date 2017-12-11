package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for TASKING VX compiler warnings.
 *
 * @author Sven Lübke
 */
public class TaskingVXCompilerParser extends RegexpLineParser {
    /** Pattern of TASKING VX compiler warnings. */
    private static final String TASKING_VX_COMPILER_WARNING_PATTERN = "^.*? (I|W|E|F)(\\d+): (?:\\[\"(.*?)\" (\\d+)" +
            "\\/(\\d+)\\] )?(.*)$";


    /**
     * Creates a new instance of <code>TaskingVXCompilerParser</code>.
     */
    public TaskingVXCompilerParser() {
        super(TASKING_VX_COMPILER_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        String fileName;
        String msgType = matcher.group(1);
        int lineNumber;
        String message = matcher.group(6);
        Priority priority;
        String category;

        if (matcher.group(3) != null) {
            fileName = matcher.group(3);
        }
        else {
            fileName = "";
        }

        if (matcher.group(4) != null) {
            lineNumber = parseInt(matcher.group(4));
        }
        else {
            lineNumber = 0;
        }

        if ("E".equals(msgType)) {
            priority = Priority.HIGH;
            category = "ERROR";
        }
        else if ("F".equals(msgType)) {
            priority = Priority.HIGH;
            category = "License issue";
        }
        else if ("I".equals(msgType)) {
            priority = Priority.LOW;
            category = "Info";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return builder.setFileName(fileName).setLineStart(lineNumber).setCategory(category).setMessage(message)
                      .setPriority(priority).build();
    }
}

