package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends AbstractMavenLogParser {
    private static final long serialVersionUID = 7199325311690082782L;

    private static final String ERROR_PRONE_URL_PATTERN = "\\s+\\(see https?://\\S+\\s*\\)";

    private static final String JAVAC_WARNING_PATTERN
            = "^(?:\\S+\\s+)?"                // optional preceding arbitrary number of characters that are not a
                                              // whitespace followed by whitespace. This can be used for timestamps.
            + "(?:(?:\\[(WARNING|ERROR)\\]|w:|e:)\\s+)" // optional [WARNING] or [ERROR] or w: or e:
            + "([^\\[\\(]*):\\s*"             // group 1: filename
            + "[\\[\\(]"                      // [ or (
            + "(\\d+)[.,;]*"                  // group 2: line number
            + "\\s?(\\d+)?"                   // group 3: optional column
            + "[\\]\\)]\\s*"                  // ] or )
            + ":?"                            // optional :
            + "(?:\\[(\\w+)\\])?"             // group 4: optional category
            + "\\s*(.*)$";                    // group 5: message

    private static final String SEVERITY_ERROR = "ERROR";
    private static final String SEVERITY_ERROR_SHORT = "e:";
    private static final String DEFAULT_GOAL = "javac";

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super(JAVAC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return (line.contains("[") || line.contains("w:") || line.contains("e:"))
                && !hasGoals(MAVEN_JAVADOC_PLUGIN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        if (lookahead.hasNext(ERROR_PRONE_URL_PATTERN)) {
            return Optional.empty();
        }

        String type = matcher.group(1);
        if (SEVERITY_ERROR.equals(type) || SEVERITY_ERROR_SHORT.equals(type)) {
            builder.setSeverity(Severity.ERROR);
        }
        else {
            builder.setSeverity(Severity.WARNING_NORMAL);
        }

        String message = matcher.group(6);
        String category = guessCategoryIfEmpty(matcher.group(5), message);

        // get rid of leading / from windows compiler output JENKINS-66738
        return builder.setFileName(RegExUtils.replaceAll(matcher.group(2), "^/([a-zA-Z]):", "$1:"))
                .setLineStart(matcher.group(3))
                .setType(StringUtils.defaultString(getGoal(), DEFAULT_GOAL))
                .setColumnStart(matcher.group(4))
                .setCategory(category)
                .setMessage(message)
                .buildOptional();
    }
}

