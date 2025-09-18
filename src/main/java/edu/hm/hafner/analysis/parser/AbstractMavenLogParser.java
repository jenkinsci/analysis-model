package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import edu.hm.hafner.analysis.LookaheadParser;

import java.io.Serial;
import java.util.regex.Pattern;

/**
 * A base class for parsers that will work on subsections of Maven build logs. These logs can be divided into sections
 * that are created by specific maven plugins or goals.
 *
 * @author Jagruti Tiwari
 */
public abstract class AbstractMavenLogParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -3768790896172545192L;

    /** Regular expression to parse the start of maven plugin in console. */
    private static final Pattern MAVEN_PLUGIN_START = Pattern.compile(
            "\\[INFO\\] --- (?<id>\\S+):(?<version>\\S+):(?<goal>\\S+)\\s.*");
    private static final Pattern MAVEN_MODULE_START = Pattern.compile(
            "-+< (?<id>\\S+) >-+"
    );
    private static final String MAVEN_PLUGIN_PREFIX = "maven-";
    private static final String MAVEN_PLUGIN_SUFFIX = "-plugin";
    static final String MAVEN_COMPILER_PLUGIN = MAVEN_PLUGIN_PREFIX + "compiler" + MAVEN_PLUGIN_SUFFIX;
    static final String MAVEN_SUREFIRE_PLUGIN = MAVEN_PLUGIN_PREFIX + "surefire" + MAVEN_PLUGIN_SUFFIX;
    static final String MAVEN_FAILSAFE_PLUGIN = MAVEN_PLUGIN_PREFIX + "failsafe" + MAVEN_PLUGIN_SUFFIX;
    static final String MAVEN_JAVADOC_PLUGIN = MAVEN_PLUGIN_PREFIX + "javadoc" + MAVEN_PLUGIN_SUFFIX;
    static final String MAVEN_HPI_PLUGIN = MAVEN_PLUGIN_PREFIX + "hpi" + MAVEN_PLUGIN_SUFFIX;
    static final String MAVEN_ENFORCER_PLUGIN = MAVEN_PLUGIN_PREFIX + "enforcer" + MAVEN_PLUGIN_SUFFIX;
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
        var goalMatcher = MAVEN_PLUGIN_START.matcher(line);

        if (goalMatcher.find()) {
            goal = "%s:%s".formatted(goalMatcher.group("id"), goalMatcher.group("goal"));
        }
        else if (line.contains("[INFO] BUILD ")) {
            goal = StringUtils.EMPTY; // reset goal after build
        }

        var moduleMatcher = MAVEN_MODULE_START.matcher(line);
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

    protected boolean hasGoals(final String... goals) {
        for (String searchGoal : goals) {
            if (goal.contains(searchGoal)) {
                return true;
            }
            if (goal.contains(Strings.CS.removeEnd(
                    Strings.CS.removeStart(searchGoal, MAVEN_PLUGIN_PREFIX), MAVEN_PLUGIN_SUFFIX))) {
                return true;
            }
        }
        return false;
    }
}
