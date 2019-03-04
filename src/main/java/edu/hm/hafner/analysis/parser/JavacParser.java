package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends RegexpLineParser {
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
    protected String interestingLineContent(String line) {
        if (line.contains("[")
                || line.contains("w:")) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = matcher.group(1);
        if ("ERROR".equals(type)) {
            builder.setSeverity(Severity.ERROR);
        }
        else {
            builder.setSeverity(Severity.WARNING_NORMAL);
        }

        String message = matcher.group(6);
        String category = guessCategoryIfEmpty(matcher.group(5), message);

        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setCategory(category)
                .setMessage(message)
                .buildOptional();
    }
}

