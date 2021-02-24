package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ClangParser;

/**
 * A Descriptor for the Clang parser.
 *
 * @author Lorenz Munsch
 */
class ClangDescriptor extends ParserDescriptor {

    private static final String ID = "clang";
    private static final String NAME = "Clang";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    ClangDescriptor() {
        super(ID, NAME, new ClangParser());
    }
}
