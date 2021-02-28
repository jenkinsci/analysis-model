package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ScalacParser;

/**
 * A descriptor for the  Scalac parser.
 *
 * @author Lorenz Munsch
 */
class ScalaDescriptor extends ParserDescriptor {
    private static final String ID = "scala";
    private static final String NAME = "Scala Compiler";

    ScalaDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new ScalacParser();
    }
}
