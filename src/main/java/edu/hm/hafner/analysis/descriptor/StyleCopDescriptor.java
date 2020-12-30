package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.StyleCopParser;

/**
 * A Descriptor for the Style Cop parser.
 *
 * @author Lorenz Munsch
 */
class StyleCopDescriptor extends ParserDescriptor {

    private static final String ID = "stylecop";
    private static final String NAME = "StyleCop";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    StyleCopDescriptor() {
        super(ID, NAME, new StyleCopParser());
    }
}
