package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for gcc 4.x compiler warnings.
 *
 * @author Frederic Chateau
 */
public class Gcc4CompilerParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 5490211629355204910L;

    private static final String ERROR = "error";
    private static final String GCC_WARNING_PATTERN = ANT_TASK + "(.+?):(\\d+):(?:(\\d+):)? (warning|.*error): (.*)$";
    private static final Pattern CLASS_PATTERN = Pattern.compile("\\[-W(.+)\\]$");

    /**
     * Creates a new instance of {@link Gcc4CompilerParser} .
     */
    public Gcc4CompilerParser() {
        super(GCC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(5);
        Priority priority;

        StringBuilder category = new StringBuilder();
        if (matcher.group(4).contains(ERROR)) {
            priority = Priority.HIGH;
            category.append("Error");
        }
        else {
            priority = Priority.NORMAL;
            category.append("Warning");

            Matcher classMatcher = CLASS_PATTERN.matcher(message);
            if (classMatcher.find() && classMatcher.group(1) != null) {
                category.append(':').append(classMatcher.group(1));
            }
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setColumnStart(parseInt(matcher.group(3)))
                .setCategory(category.toString())
                .setMessage(message)
                .setPriority(priority)
                .build();
    }
}

