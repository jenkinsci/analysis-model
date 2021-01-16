package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ClangParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A Descriptor for the Clang parser.
 *
 * @author Lorenz Munsch
 */
public class ClangDescriptor extends ParserDescriptor {

    private static final String ID = "clang";
    private static final String NAME = "Clang";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public ClangDescriptor() {
        super(ID, NAME, new ClangParser());
    }
}
