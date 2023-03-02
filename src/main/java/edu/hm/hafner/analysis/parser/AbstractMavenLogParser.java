package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.LookaheadParser;

/**
 * A base class for parsers that will work on subsections of Maven build logs. These logs can be divided into sections
 * that are created by specific maven plugins or goals.
 *
 * @author Jagruti Tiwari
 */
public abstract class AbstractMavenLogParser extends LookaheadParser {
    private static final long serialVersionUID = -3768790896172545192L;

    /** Regular expression to parse the start of maven plugin in console. */
    private static final Pattern MAVEN_PLUGIN_START = Pattern.compile(
            "\\[INFO\\] --- (?<id>\\S+):(?<version>\\S+):(?<goal>\\S+)\\s.*");
    private static final Pattern MAVEN_MODULE_START = Pattern.compile(
            "-+< (?<id>\\S+) >-+"
    );
    static final String MAVEN_COMPILER_PLUGIN = "compiler";
    private String goal = StringUtils.EMPTY;
    private String module = StringUtils.EMPTY;

    /**
     * Creates a new instance of {@link AbstractMavenLogParser}.
     *
     * @param pattern
     *         pattern of compiler warnings.
     */
    protected AbstractMavenLogParser(final String pattern) {
        super(pattern);
    }

    @Override
    protected void preprocessLine(final String line) {
        Matcher goalMatcher = MAVEN_PLUGIN_START.matcher(line);

        if (goalMatcher.find()) {
            goal = String.format("%s:%s", goalMatcher.group("id"), goalMatcher.group("goal"));
        }

        Matcher moduleMatcher = MAVEN_MODULE_START.matcher(line);
        if (moduleMatcher.find()) {
            module = moduleMatcher.group("id");
        }
    }

    protected String getModule() {
        return module;
    }

    protected String getGoal() {
        return goal;
    }

    boolean hasGoalOrModule() {
        return StringUtils.isNotBlank(goal) || StringUtils.isNotBlank(module);
    }
}
