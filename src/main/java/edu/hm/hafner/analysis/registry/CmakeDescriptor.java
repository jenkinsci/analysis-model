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
    public IssueParser create(final Option... options) {
        return new CMakeParser();
    }

    @Override
    public String getUrl() {
        return "https://cmake.org/";
    }

    @Override
    public String getIconUrl() {
        return "https://cmake.org/wp-content/uploads/2023/08/CMake-Mark-1.svg";
    }
}
