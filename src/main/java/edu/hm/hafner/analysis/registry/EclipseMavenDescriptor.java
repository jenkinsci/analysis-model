package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.EclipseMavenParser;

/**
 * A Descriptor for the Eclipse Maven parser.
 *
 * @author Lorenz Munsch
 */
class EclipseMavenDescriptor extends ParserDescriptor {
    private static final String ID = "eclipse-maven";
    private static final String NAME = "Eclipse Maven";

    EclipseMavenDescriptor() {
        super(ID, NAME, new EclipseMavenParser());
    }
}
