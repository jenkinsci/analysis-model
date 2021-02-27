package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.DiabCParser;

/**
 * A Descriptor for the Diab C parser.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new DiabCParser();
    }
}
