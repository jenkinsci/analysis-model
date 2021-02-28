package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AntJavacParser;

/**
 * A descriptor for the AntJavac warnings.
 *
 * @author Lorenz Munsch
 */
class AntJavacDescriptor extends ParserDescriptor {
    private static final String ID = "javac-ant";
    private static final String NAME = "Ant javac";

    AntJavacDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new AntJavacParser();
    }
}
