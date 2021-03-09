package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CMakeParser;

/**
 * A descriptor for CMake.
 *
 * @author Lorenz Munsch
 */
class CmakeDescriptor extends ParserDescriptor {
    private static final String ID = "cmake";
    private static final String NAME = "CMake";

    CmakeDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new CMakeParser();
    }
}
