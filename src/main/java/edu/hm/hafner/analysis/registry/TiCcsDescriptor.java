package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TiCcsParser;

/**
 * A descriptor for the Texas Instruments Code Composer Studio.
 *
 * @author Lorenz Munsch
 */
public class TiCcsDescriptor extends ParserDescriptor {
    private static final String ID = "code-composer";
    private static final String NAME = "Texas Instruments Code Composer Studio";

    public TiCcsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new TiCcsParser();
    }
}
