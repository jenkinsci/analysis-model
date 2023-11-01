package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.DocFxAdapter;

/**
 * A descriptor for DocFX.
 *
 * @author Lorenz Munsch
 */
public class DocFxDescriptor extends ParserDescriptor {
    private static final String ID = "docfx";
    private static final String NAME = "DocFX";

    public DocFxDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DocFxAdapter();
    }
}
