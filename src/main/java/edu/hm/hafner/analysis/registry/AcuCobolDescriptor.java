package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AcuCobolParser;

/**
 * A descriptor for the AcuCobol Compiler.
 *
 * @author Lorenz Munsch
 */
class AcuCobolDescriptor extends ParserDescriptor {
    private static final String ID = "acu-cobol";
    private static final String NAME = "AcuCobol";

    AcuCobolDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new AcuCobolParser();
    }
}
