package edu.hm.hafner.analysis.parser;

import static edu.hm.hafner.analysis.Categories.guessCategoryIfEmpty;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.RegExUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends LookaheadParser {
    private static final long serialVersionUID = 7199325311690082782L;

    private static final String ERRORPRONE_URL_PATTERN = "\\s+\\(see https?://\\S+\\s*\\)";

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

    private String goal = "javac";

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super(JAVAC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        Matcher goalMatcher = MavenConsoleParser.MAVEN_PLUGIN_START.matcher(line);
        if (goalMatcher.find() && "maven-compiler-plugin".equals(goalMatcher.group("id"))) {
            // remember the maven-compiler-plugin and goal to use as issue type
            goal = String.format("%s:%s", goalMatcher.group("id"), goalMatcher.group("goal"));
        }

        return line.contains("[") || line.contains("w:") || line.contains("e:");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {        
        if (lookahead.hasNext(ERRORPRONE_URL_PATTERN)) {
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
                .setType(goal)
                .setColumnStart(matcher.group(4))
                .setCategory(category)
                .setMessage(message)
                .buildOptional();
    }
}

