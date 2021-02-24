package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.dry.cpd.CpdParser;

/**
 * A Descriptor for the Cpfr parser.
 *
 * @author Lorenz Munsch
 */
class CpdDescriptor extends ParserDescriptor {

    private static final String ID = "cpd";
    private static final String NAME = "CPD";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    CpdDescriptor() {
        super(ID, NAME, new CpdParser());
    }
}
