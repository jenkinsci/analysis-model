package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TnsdlParser;

/**
 * A descriptor for the TNSDL Translator.
 *
 * @author Lorenz Munsch
 */
public class TnsdlDescriptor extends ParserDescriptor {
    private static final String ID = "tnsdl";
    private static final String NAME = "TNSDL Translator";

    public TnsdlDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new TnsdlParser();
    }
}
