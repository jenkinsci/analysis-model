package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CadenceIncisiveParser;

/**
 * A descriptor for the Cadence Incisive Enterprise Simulator.
 *
 * @author Lorenz Munsch
 */
public class CadenceIncisiveDescriptor extends ParserDescriptor {
    private static final String ID = "cadence";
    private static final String NAME = "Cadence Incisive";

    public CadenceIncisiveDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CadenceIncisiveParser();
    }
}
