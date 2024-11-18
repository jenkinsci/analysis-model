package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static j2html.TagCreator.*;

/**
 * A parser for maven console warnings.
 *
 * @author Ullrich Hafner
 */
public class MavenConsoleParser extends AbstractMavenLogParser {
    @Serial
    private static final long serialVersionUID = 1737791073711198075L;

    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";

    /**
     * Pattern for identifying warning or error maven logs.
     *
     * <pre>{@code
     * Pattern:
     * (.*\s\s|)           -> Capture group 1 matches either empty string (e.g. [WARNING] some log) or some text
     *                        followed by exactly two spaces (e.g. 22:07:27  [WARNING] some log)
     * \[(WARNING|ERROR)\] -> Capture group 2 matches either [WARNING] or [ERROR]
     * \s*                 -> matches zero or more spaces
     * (.*)                -> Capture group 3 matches zero or more characters except line breaks, represents the actual error message
     * }</pre>
     *
     * <p>
     * Typical maven logs:
     * </p>
     * <pre>{@code
     * 1) 22:07:27  [WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
     * 2) [ERROR] The POM for org.codehaus.groovy.maven:gmaven-plugin:jar:1.1 is missing
     * }</pre>
     */
    private static final String PATTERN = "^(?<timestamp>.*\\s|)\\[(?<severity>WARNING|ERROR)\\]\\s*(?<message>.*)$";

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super(PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return isValidGoal() && (line.contains(WARNING) || line.contains(ERROR));
    }

    private boolean isValidGoal() {
        return !hasGoals(MAVEN_COMPILER_PLUGIN, MAVEN_JAVADOC_PLUGIN); // will be captured by another parser already
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        String severity = matcher.group("severity");
        builder.setLineStart(lookahead.getLine()).guessSeverity(severity);

        var message = new StringBuilder(matcher.group("message"));

        if (hasGoals(MAVEN_ENFORCER_PLUGIN)) {
            String timestamp = matcher.group("timestamp");
            int length = StringUtils.length(timestamp);

            String continuation = "^(?:.*\\s|)\\[(INFO|WARNING|ERROR)";
            while (lookahead.hasNext() && !lookahead.hasNext(continuation)) {
                message.append('\n');
                message.append(StringUtils.substring(lookahead.next(), length));
            }
        }
        else {
            String continuation = "^(?:.*\\s\\s|)\\[" + severity + "\\] ";
            while (lookahead.hasNext(continuation)) {
                message.append('\n');
                message.append(RegExUtils.removeFirst(lookahead.next(), continuation));
            }
            if (message.lastIndexOf("Unable to locate Source XRef to link to") >= 0) {
                builder.setSeverity(Severity.WARNING_LOW);
            }
            if (StringUtils.isBlank(message.toString())) {
                return Optional.empty();
            }
        }
        return builder.setDescription(pre().with(code().withText(message.toString())).render())
                .setType(getGoal())
                .setModuleName(getModule())
                .setLineEnd(lookahead.getLine())
                .setFileName(lookahead.getFileName())
                .buildOptional();
    }
}
