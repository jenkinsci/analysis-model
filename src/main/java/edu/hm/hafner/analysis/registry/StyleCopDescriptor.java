package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.StyleCopParser;

/**
 * A Descriptor for the Style Cop parser.
 *
 * @author Lorenz Munsch
 */
class StyleCopDescriptor extends ParserDescriptor {
    private static final String ID = "stylecop";
    private static final String NAME = "StyleCop";

    StyleCopDescriptor() {
        super(ID, NAME, new StyleCopParser());
    }
}
