package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.RegExUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends AbstractMavenLogParser {
    @Serial
    private static final long serialVersionUID = 7199325311690082782L;

    private static final String ERROR_PRONE_URL_PATTERN = "\\s+\\(see https?://\\S+\\s*\\)";

    private static final String JAVAC_WARNING_PATTERN
            = "^(?:\\S+\\s+)?"                          // optional preceding arbitrary number of characters that are not a
                                                        // whitespace followed by whitespace. This can be used for timestamps.
            + "(?:(?:\\[(WARNING|ERROR)\\]|w:|e:)\\s+)" // optional [WARNING] or [ERROR] or w: or e:
            // --- Matches filename/line ---
            + "(((\\/?[a-zA-Z]|file):)?[^\\[\\(:]*):"   // group 2: filename starting path with C:\ or /C:\ or file:/// or /
            + "("                                       // start group 5
            + "(\\s*[\\[\\(]?)?"                        // optional ( or [
            + "(\\d+)"                                  // group 7 line
            + "[.,;]?\\s?:?"                            // separator
            + "(\\d+)?"                                 // group 8 column
            + "[\\]\\)]?\\s*:?\\s?"                     // optional ) or ] or whitespace or :
            + ")"                                       // end group 5
            + "(?:\\[(\\w+)\\])?"                       // group 9: optional category
            + "\\s*(.*)";                               // group 10: message

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
                && !hasGoals(MAVEN_JAVADOC_PLUGIN, MAVEN_HPI_PLUGIN, MAVEN_SUREFIRE_PLUGIN, MAVEN_FAILSAFE_PLUGIN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        if (lookahead.hasNext(ERROR_PRONE_URL_PATTERN)) {
            return Optional.empty();
        }

        var type = matcher.group(1);
        if (SEVERITY_ERROR.equals(type) || SEVERITY_ERROR_SHORT.equals(type)) {
            builder.setSeverity(Severity.ERROR);
        }
        else {
            builder.setSeverity(Severity.WARNING_NORMAL);
        }

        var message = matcher.group(10);
        var category = guessCategoryIfEmpty(matcher.group(9), message);

        // get rid of leading / from windows compiler output JENKINS-66738
        return builder.setFileName(RegExUtils.replaceAll(matcher.group(2), "^/([a-zA-Z]):", "$1:"))
                .setLineStart(matcher.group(7))
                .setType(Objects.toString(getGoal(), DEFAULT_GOAL))
                .setColumnStart(matcher.group(8))
                .setCategory(category)
                .setMessage(message)
                .buildOptional();
    }
}
