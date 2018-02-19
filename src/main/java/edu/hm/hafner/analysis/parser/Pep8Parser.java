package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for the Pep8 compiler warnings.
 *
 * @author Marvin Schütz
 */
public class Pep8Parser extends FastRegexpLineParser {
    private static final long serialVersionUID = -8444940209330966997L;

    private static final String PEP8_WARNING_PATTERN = "(.*):(\\d+):(\\d+): (\\D\\d*) (.*)";

    /**
     * Creates a new instance of {@link Pep8Parser}.
     */
    public Pep8Parser() {
        super(PEP8_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(5);
        String category = guessCategoryIfEmpty(matcher.group(4), message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setColumnStart(parseInt(matcher.group(3)))
                .setCategory(category)
                .setMessage(message)
                .setPriority(mapPriority(category))
                .build();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(":");
    }

    private Priority mapPriority(final String priority) {
        if (priority.contains("E")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }
}

