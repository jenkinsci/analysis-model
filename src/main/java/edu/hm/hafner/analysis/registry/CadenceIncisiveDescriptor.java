package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.CadenceIncisiveParser;

/**
 * A Descriptor for the CadenceIncisive warnings.
 *
 * @author Lorenz Munsch
 */
class CadenceIncisiveDescriptor extends ParserDescriptor {
    private static final String ID = "cadence";
    private static final String NAME = "Cadence Incisive";

    CadenceIncisiveDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new CadenceIncisiveParser();
    }
}
