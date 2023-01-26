package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A parser subclass for MavenConsoleParser, AntJavacParser, JavaDocParser, JavacParser
 *
 * @author Jagruti Tiwari
 */

public class AbstractMavenLogParser extends MavenConsoleParser {



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

    private String goal = StringUtils.EMPTY;
    private String module = StringUtils.EMPTY;


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
