package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ScalacParser;

/**
 * A Descriptor for the  Scalac parser.
 *
 * @author Lorenz Munsch
 */
class ScalacDescriptor extends ParserDescriptor {

    private static final String ID = "scalac";
    private static final String NAME = "Scala Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    ScalacDescriptor() {
        super(ID, NAME, new ScalacParser());
    }
}
