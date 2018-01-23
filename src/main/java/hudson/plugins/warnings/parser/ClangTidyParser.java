package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.Extension;

import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for the clang-tidy static analysis warnings.
 *
 * @author Ryan Schaefer
 */
@Extension
public class ClangTidyParser extends RegexpLineParser {
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_TIDY_WARNING_PATTERN =
        "([^\\s]+):(\\d+):(\\d+): (warning|error): (.*?) \\[(.*?)\\]$";

    /**
     * Creates a new instance of {@link ClangTidyParser}.
     */
    public ClangTidyParser() {
        super(Messages._Warnings_ClangTidy_ParserName(),
                Messages._Warnings_ClangTidy_LinkName(),
                Messages._Warnings_ClangTidy_TrendName(),
                CLANG_TIDY_WARNING_PATTERN,
                false);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {

        String filename = matcher.group(1);
        int lineNumber = getLineNumber(matcher.group(2));
        int column = getLineNumber(matcher.group(3));
        String type = matcher.group(4);
        String category = matcher.group(6);
        String message = matcher.group(5);

        Priority priority;
        if (type.contains("error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        Warning warning;
        if (category == null) {
            warning = createWarning(filename, lineNumber, message, priority);
        }
        else {
            warning = createWarning(filename, lineNumber, category, message, priority);
        }
        warning.setColumnPosition(column);
        return warning;
    }
}
