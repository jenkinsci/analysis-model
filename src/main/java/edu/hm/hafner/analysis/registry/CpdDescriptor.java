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

    CpdDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new CpdParser();
    }
}
