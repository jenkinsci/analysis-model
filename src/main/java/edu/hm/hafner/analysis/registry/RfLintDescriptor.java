package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RfLintParser;

/**
 * A descriptor for RfLint.
 *
 * @author Lorenz Munsch
 */
public class RfLintDescriptor extends ParserDescriptor {
    private static final String ID = "rflint";
    private static final String NAME = "Robot Framework Lint";

    public RfLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new RfLintParser();
    }
}
