package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClangTidyParser;

/**
 * A descriptor for the Clang-Tidy compiler.
 *
 * @author Lorenz Munsch
 */
public class ClangTidyDescriptor extends ParserDescriptor {
    private static final String ID = "clang-tidy";
    private static final String NAME = "Clang-Tidy";

    public ClangTidyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ClangTidyParser();
    }
}
