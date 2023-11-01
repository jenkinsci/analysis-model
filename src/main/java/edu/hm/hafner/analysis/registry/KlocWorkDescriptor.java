package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.KlocWorkAdapter;

/**
 * A descriptor for Klocwork.
 *
 * @author Lorenz Munsch
 */
public class KlocWorkDescriptor extends ParserDescriptor {
    private static final String ID = "klocwork";
    private static final String NAME = "Klocwork";

    public KlocWorkDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new KlocWorkAdapter();
    }
}
