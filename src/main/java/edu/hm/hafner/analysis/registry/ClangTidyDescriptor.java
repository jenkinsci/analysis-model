package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ClangTidyParser;

/**
 * A Descriptor for the Clang tidy parser.
 *
 * @author Lorenz Munsch
 */
class ClangTidyDescriptor extends ParserDescriptor {
    private static final String ID = "clang-tidy";
    private static final String NAME = "Clang-Tidy";

    ClangTidyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new ClangTidyParser();
    }
}
