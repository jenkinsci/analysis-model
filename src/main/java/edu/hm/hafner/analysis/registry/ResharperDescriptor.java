package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.ResharperInspectCodeAdapter;

/**
 * A descriptor for Resharper Inspections.
 *
 * @author Lorenz Munsch
 */
public class ResharperDescriptor extends ParserDescriptor {
    private static final String ID = "resharper";
    private static final String NAME = "Resharper Inspections";

    public ResharperDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ResharperInspectCodeAdapter();
    }
}
