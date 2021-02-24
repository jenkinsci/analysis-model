package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.EclipseParser;

/**
 * A Descriptor for the Eclipse  parser.
 *
 * @author Lorenz Munsch
 */
class EclipseDescriptor extends ParserDescriptor {

    private static final String ID = "eclipse";
    private static final String NAME = "Eclipse ECJ";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    EclipseDescriptor() {
        super(ID, NAME, new EclipseParser());
    }
}
