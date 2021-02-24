package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.TnsdlParser;

/**
 * A Descriptor for the Tnsdl parser.
 *
 * @author Lorenz Munsch
 */
class TnsdlDescriptor extends ParserDescriptor {

    private static final String ID = "tnsdl";
    private static final String NAME = "TNSDL Translator";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    TnsdlDescriptor() {
        super(ID, NAME, new TnsdlParser());
    }
}
