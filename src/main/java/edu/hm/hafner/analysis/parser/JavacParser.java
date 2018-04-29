package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 7199325311690082782L;

    private static final String JAVAC_WARNING_PATTERN
            = "^(?:\\[\\p{Alnum}*\\]\\s+)?"
            + "(?:(?:\\[(WARNING|ERROR)\\]|w:)\\s+)" // optional [WARNING] or [ERROR] or w:
            + "([^\\[\\(]*):\\s*" +             // group 1: filename
            "[\\[\\(]" +                      // [ or (
            "(\\d+)[.,;]*" +                  // group 2: line number
            "\\s?(\\d+)?" +                   // group 3: optional column
            "[\\]\\)]\\s*" +                  // ] or )
            ":?" +                            // optional :
            "(?:\\[(\\w+)\\])?" +             // group 4: optional category
            "\\s*(.*)$";                      // group 5: message

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super(JAVAC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[") || line.contains("w:");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = matcher.group(1);
        if ("WARNING".equals(type)) {
            builder.setPriority(Priority.NORMAL);
        }
        else if ("ERROR".equals(type)) {
            builder.setSeverity(Severity.ERROR);
        }

        String message = matcher.group(6);
        String category = guessCategoryIfEmpty(matcher.group(5), message);

        return builder.setFileName(matcher.group(2))
                .setLineStart(parseInt(matcher.group(3)))
                .setColumnStart(parseInt(matcher.group(4)))
                .setCategory(category)
                .setMessage(message)
                .build();
    }
}

