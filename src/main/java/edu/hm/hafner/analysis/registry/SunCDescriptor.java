package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SunCParser;

/**
 * A descriptor for the the SUN Studio C++ compiler.
 *
 * @author Lorenz Munsch
 */
class SunCDescriptor extends ParserDescriptor {
    private static final String ID = "sunc";
    private static final String NAME = "SUN C++ Compiler";

    SunCDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new SunCParser();
    }
}
