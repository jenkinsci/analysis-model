package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

import static edu.hm.hafner.analysis.Categories.*;


/**
 * A parser for the Pep8 compiler warnings.
 *
 * @author Marvin Schütz
 */
public class Pep8Parser extends RegexpLineParser {
    private static final long serialVersionUID = -8444940209330966997L;

    private static final String PEP8_WARNING_PATTERN = "(.*):(\\d+):(\\d+): (\\D\\d*) (.*)";

    /**
     * Creates a new instance of {@link Pep8Parser}.
     */
    public Pep8Parser() {
        super(PEP8_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(5);
        String category = guessCategoryIfEmpty(matcher.group(4), message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setColumnStart(matcher.group(3))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(mapPriority(category))
                .buildOptional();
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains(":")) {
            return line;
        }

        return null;
    }

    private Severity mapPriority(final String priority) {
        if (priority.contains("E")) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.WARNING_LOW;
        }
    }
}

