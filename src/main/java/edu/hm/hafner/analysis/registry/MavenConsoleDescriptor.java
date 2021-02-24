package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A Descriptor for the Maven Console parser.
 *
 * @author Lorenz Munsch
 */
class MavenConsoleDescriptor extends ParserDescriptor {

    private static final String ID = "maven-warnings";
    private static final String NAME = "Maven";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    MavenConsoleDescriptor() {
        super(ID, NAME, new MavenConsoleParser());
    }
}
