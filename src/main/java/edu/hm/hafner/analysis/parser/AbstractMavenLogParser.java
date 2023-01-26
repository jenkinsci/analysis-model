package edu.hm.hafner;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A parser subclass for MavenConsoleParser, AntJavacParser, JavaDocParser, JavacParser
 *
 * @author Jagruti Tiwari
 */

public class AbstractMavenLogParser extends MavenConsoleParser {

    private String goal = StringUtils.EMPTY;

    public boolean isLineInteresting(final String line) {
        return super.isLineInteresting(line);
    }

    public boolean isValidGoal() {
        return !(goal.contains("maven-compiler-plugin")
                || goal.contains("maven-javadoc-plugin")); // will be captured by another parser already
    }



}
