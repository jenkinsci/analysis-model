package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DiabCParser;

/**
 * A descriptor for the Wind River Diab Compiler (C/C++).
 *
 * @author Lorenz Munsch
 */
public class DiabCDescriptor extends ParserDescriptor {
    private static final String ID = "diabc";
    private static final String NAME = "Wind River Diab Compiler (C/C++)";

    public DiabCDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DiabCParser();
    }
}
