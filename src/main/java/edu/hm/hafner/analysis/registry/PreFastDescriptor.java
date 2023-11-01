package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PreFastParser;

/**
 * A descriptor for Microsoft PreFast.
 *
 * @author Lorenz Munsch
 */
public class PreFastDescriptor extends ParserDescriptor {
    private static final String ID = "prefast";
    private static final String NAME = "PREfast";

    public PreFastDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PreFastParser();
    }
}
