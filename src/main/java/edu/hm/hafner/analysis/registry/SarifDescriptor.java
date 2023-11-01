package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.SarifAdapter;

/**
 * A descriptor for the SARIF parser.
 *
 * @author Ullrich Hafner
 */
public class SarifDescriptor extends ParserDescriptor {
    private static final String ID = "sarif";
    private static final String NAME = "SARIF";

    public SarifDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new SarifAdapter();
    }

    @Override
    public String getUrl() {
        return "https://github.com/oasis-tcs/sarif-spec";
    }
}
