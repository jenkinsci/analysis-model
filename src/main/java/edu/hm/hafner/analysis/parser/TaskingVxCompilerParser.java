package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for TASKING VX compiler warnings.
 *
 * @author Sven Lübke
 */
public class TaskingVxCompilerParser extends RegexpLineParser {
    private static final long serialVersionUID = -5225265084645449716L;

    /** Pattern of TASKING VX compiler warnings. */
    private static final String TASKING_VX_COMPILER_WARNING_PATTERN = "^.*? (I|W|E|F)(\\d+): (?:\\[\"(.*?)\" (\\d+)"
            + "\\/(\\d+)\\] )?(.*)$";

    /**
     * Creates a new instance of {@code TaskingVXCompilerParser}.
     */
    public TaskingVxCompilerParser() {
        super(TASKING_VX_COMPILER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = matcher.group(1);
        Severity priority;
        String category;

        switch (type) {
            case "E":
                priority = Severity.WARNING_HIGH;
                category = "ERROR";
                break;
            case "F":
                priority = Severity.WARNING_HIGH;
                category = "License issue";
                break;
            case "I":
                priority = Severity.WARNING_LOW;
                category = "Info";
                break;
            default:
                priority = Severity.WARNING_NORMAL;
                category = "Warning";
                break;
        }

        return builder.setFileName(matcher.group(3))
                .setLineStart(matcher.group(4))
                .setCategory(category)
                .setMessage(matcher.group(6))
                .setSeverity(priority)
                .buildOptional();
    }
}
