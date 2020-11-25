package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for armcc5 compiler warnings.
 *
 * @author Dmytro Kutianskyi
 */
public class Armcc5CompilerParser extends RegexpLineParser {
    private static final long serialVersionUID = -2677728927938443701L;

    private static final String ARMCC5_WARNING_PATTERN = "^(.+)\\((\\d+)\\): (warning|error):  #(.+): (.+)$";

    /**
     * Creates a new instance of {@link Armcc5CompilerParser}.
     */
    public Armcc5CompilerParser() {
        super(ARMCC5_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("#");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = matcher.group(3);
        Severity priority;

        if (equalsIgnoreCase(type, "error")) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }

        String errorCode = matcher.group(4);
        String message = matcher.group(5);
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setMessage(errorCode + " - " + message)
                .setSeverity(priority).buildOptional();
    }
}

