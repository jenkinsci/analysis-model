package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends FastRegexpLineParser {

    private static final long serialVersionUID = 7199325311690082782L;
    private static final String JAVAC_WARNING_PATTERN = "^(?:\\[\\p{Alnum}*\\]\\s+)?" + // optional alphanumerics
            "(?:\\[WARNING\\]\\s+)?" +      // optional [WARNING]
            "([^\\[\\(]*):\\s*" +           // group 1: filename
            "[\\[\\(]" +                    // [ or (
            "(\\d+)[.,;]*" +                // group 2: line number
            "\\s?(\\d+)?" +                 // group 3: optional column
            "[\\]\\)]\\s*" +                // ] or )
            "(?:\\[(\\w+)\\])?" +           // group 4: optional category
            "\\s*(.*)$";                    // group 5: message

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super("javac", JAVAC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(5);
        String category = guessCategoryIfEmpty(matcher.group(4), message);

        return builder.setFileName(matcher.group(1)).setLineStart(parseInt(matcher.group(2)))
                             .setColumnStart(parseInt(matcher.group(3))).setCategory(category).setMessage(message)
                             .build();
    }
}

