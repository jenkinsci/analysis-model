package edu.hm.hafner;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A parser subclass for MavenConsoleParser, AntJavacParser, JavaDocParser, JavacParser
 *
 * @author Jagruti Tiwari
 */

public class AbstractMavenLogParser extends MavenConsoleParser {

    @Override
    protected boolean isLineInteresting(final String line) {
        return super.isLineInteresting(line);
    }

}
