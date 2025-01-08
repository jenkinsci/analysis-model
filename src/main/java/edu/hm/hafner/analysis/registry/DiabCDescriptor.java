package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DiabCParser;

/**
 * A descriptor for the Wind River Diab Compiler (C/C++).
 *
 * @author Lorenz Munsch
 */
class DiabCDescriptor extends ParserDescriptor {
    private static final String ID = "diabc";
    private static final String NAME = "Wind River Diab Compiler (C/C++)";

    DiabCDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new DiabCParser();
    }
}
