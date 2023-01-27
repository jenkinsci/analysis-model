package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.parser.MavenConsoleParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser subclass for MavenConsoleParser, AntJavacParser, JavaDocParser, JavacParser
 *
 * @author Jagruti Tiwari
 */

public class AbstractMavenLogParser extends LookaheadParser {


    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";

    /**
     * Regular expression to parse the start of maven plugin in console.
     */
    protected static final Pattern MAVEN_PLUGIN_START = Pattern.compile(
            "\\[INFO\\] --- (?<id>\\S+):(?<version>\\S+):(?<goal>\\S+)\\s.*");

    private static final Pattern MAVEN_MODULE_START = Pattern.compile(
            "-+< (?<id>\\S+) >-+"
    );

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

    private String goal = StringUtils.EMPTY;
    private String module = StringUtils.EMPTY;

    /**
     * Creates a new instance of {@link LookaheadParser}.
     *
     * @param pattern
     *         pattern of compiler warnings.
     */
    protected AbstractMavenLogParser(final String pattern) {
        super(pattern);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder)
            throws ParsingException {
        return Optional.empty();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        Matcher goalMatcher = MAVEN_PLUGIN_START.matcher(line);

        if (goalMatcher.find()) {
            goal = String.format("%s:%s", goalMatcher.group("id"), goalMatcher.group("goal"));
        }

        if(!goal.isEmpty()){
            return false;
        }

        Matcher moduleMatcher = MAVEN_MODULE_START.matcher(line);
        if (moduleMatcher.find()) {
            module = moduleMatcher.group("id");
        }

        return isValidGoal() && (line.contains(WARNING) || line.contains(ERROR));
    }

    private boolean isValidGoal() {
        return !(goal.contains("maven-compiler-plugin")
                || goal.contains("maven-javadoc-plugin")); // will be captured by another parser already
    }





}
