package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SunCParser;

/**
 * A descriptor for the the SUN Studio C++ compiler.
 *
 * @author Lorenz Munsch
 */
public class SunCDescriptor extends ParserDescriptor {
    private static final String ID = "sunc";
    private static final String NAME = "SUN C++ Compiler";

    public SunCDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new SunCParser();
    }
}
