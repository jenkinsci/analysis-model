package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for gcc 4.x compiler warnings.
 *
 * @author Frederic Chateau
 */
public class Gcc4CompilerParser extends RegexpLineParser {
    private static final long serialVersionUID = 5490211629355204910L;
    private static final String ERROR = "error";
    private static final String GCC_WARNING_PATTERN = ANT_TASK + "(.+?):(\\d+):(?:(\\d+):)? (warning|.*error): (.*)$";
    private static final Pattern CLASS_PATTERN = Pattern.compile("\\[-W(.+)\\]$");

    /**
     * Creates a new instance of <code>Gcc4CompilerParser</code>.
     */
    public Gcc4CompilerParser() {
        super("gcc4", GCC_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String fileName = matcher.group(1);
        int lineNumber = parseInt(matcher.group(2));
        int column = parseInt(matcher.group(3));
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

        return issueBuilder().setFileName(fileName).setLineStart(lineNumber).setColumnStart(column)
                             .setCategory(category.toString()).setMessage(message).setPriority(priority).build();
    }
}

