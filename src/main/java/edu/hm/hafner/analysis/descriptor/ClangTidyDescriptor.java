package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ClangParser;
import edu.hm.hafner.analysis.parser.ClangTidyParser;

/**
 * A Descriptor for the Clang tidy parser.
 *
 * @author Lorenz Munsch
 */
public class ClangTidyDescriptor extends ParserDescriptor {

    private static final String ID = "clang_tidy";
    private static final String NAME = "ClangTidy";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public ClangTidyDescriptor() {
        super(ID, NAME, new ClangTidyParser());
    }
}
