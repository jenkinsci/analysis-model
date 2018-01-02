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
        String type = matcher.group(1);
        Priority priority;
        String category;

        if ("E".equals(type)) {
            priority = Priority.HIGH;
            category = "ERROR";
        }
        else if ("F".equals(type)) {
            priority = Priority.HIGH;
            category = "License issue";
        }
        else if ("I".equals(type)) {
            priority = Priority.LOW;
            category = "Info";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }

        return builder.setFileName(matcher.group(3))
                .setLineStart(parseInt(matcher.group(4)))
                .setCategory(category)
                .setMessage(matcher.group(6))
                .setPriority(priority)
                .build();
    }
}
