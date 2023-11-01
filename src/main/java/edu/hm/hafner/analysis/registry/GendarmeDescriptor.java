package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;

/**
 * A descriptor for Gendarme violations.
 *
 * @author Lorenz Munsch
 */
public class GendarmeDescriptor extends ParserDescriptor {
    private static final String ID = "gendarme";
    private static final String NAME = "Gendarme";

    public GendarmeDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new GendarmeParser();
    }
}
