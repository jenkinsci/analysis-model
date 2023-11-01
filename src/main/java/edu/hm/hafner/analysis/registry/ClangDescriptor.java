package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClangParser;

/**
 * A descriptor for the Clang parser.
 *
 * @author Lorenz Munsch
 */
public class ClangDescriptor extends ParserDescriptor {
    private static final String ID = "clang";
    private static final String NAME = "Clang";

    public ClangDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ClangParser();
    }
}
