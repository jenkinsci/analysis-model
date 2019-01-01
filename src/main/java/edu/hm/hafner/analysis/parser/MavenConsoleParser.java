package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for maven console warnings.
 *
 * @author Ullrich Hafner
 */
// FIXME: [INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ analysis-model ---
//         skip error messages that are part of the following plugin
//         use plugin as category
public class MavenConsoleParser extends LookaheadParser {
    private static final long serialVersionUID = 1737791073711198075L;
    
    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";

    private static final Pattern MAVEN_PLUGIN_START = Pattern.compile(
            "\\[INFO\\] --- (?<id>\\S+):(?<version>\\S+):(?<goal>\\S+)\\s.*");

    /**
     * Pattern for identifying warning or error maven logs.
     * <pre>
     * Pattern:
     * (.*\s\s|)           -> Capture group 1 matches either empty string (e.g. [WARNING] some log) or some text
     *                        followed by exactly two spaces (e.g. 22:07:27  [WARNING] some log)
     * \[(WARNING|ERROR)\] -> Capture group 2 matches either [WARNING] or [ERROR]
     * \s*                 -> matches zero or more spaces
     * (.*)                -> Capture group 3 matches zero or more characters except line breaks, represents the actual error message
     * </pre>
     * <p>
     * Typical maven logs: 1) 22:07:27  [WARNING] For this reason, future Maven versions might no longer support
     * building such malformed projects. 2) [ERROR] The POM for org.codehaus.groovy.maven:gmaven-plugin:jar:1.1 is
     * missing
     */
    private static final String PATTERN = "^(?:.*\\s\\s|)\\[(?<severity>WARNING|ERROR)\\]\\s*(?<message>.*)$";

    private String goal = StringUtils.EMPTY;

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super(PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        Matcher matcher = MAVEN_PLUGIN_START.matcher(line);
        if (matcher.find()) {
            goal = String.format("%s:%s", matcher.group("id"), matcher.group("goal"));
        }
        return line.contains(WARNING) || line.contains(ERROR);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        String severity = matcher.group("severity");
        builder.setLineStart(lookahead.getLine()).guessSeverity(severity);

        StringBuilder message = new StringBuilder(matcher.group("message"));
        String continuation = "^(?:.*\\s\\s|)\\[" + severity + "\\] ";
        while (lookahead.hasNext(continuation)) {
            message.append("\n");
            message.append(RegExUtils.removeFirst(lookahead.next(), continuation));
        }
        if (message.lastIndexOf("Unable to locate Source XRef to link to") >= 0) {
            builder.setSeverity(Severity.WARNING_LOW);
        }
        if (StringUtils.isBlank(message.toString())) {
            return Optional.empty();
        }
        return builder.setMessage(message.toString())
                .setType(goal)
                .setLineEnd(lookahead.getLine())
                .buildOptional();
    }
}

