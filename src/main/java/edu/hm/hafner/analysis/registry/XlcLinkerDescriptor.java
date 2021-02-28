package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.XlcLinkerParser;

/**
 * A descriptor for the IBM XLC Linker.
 *
 * @author Lorenz Munsch
 */
class XlcLinkerDescriptor extends ParserDescriptor {
    private static final String ID = "xlc-linker";
    private static final String NAME = "IBM XLC Linker";

    XlcLinkerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new XlcLinkerParser();
    }
}
