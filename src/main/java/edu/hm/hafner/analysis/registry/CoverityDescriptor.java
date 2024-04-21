package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.CoverityAdapter;

/**
 * A descriptor for Coverity.
 *
 * @author Ullrich Hafner
 */
class CoverityDescriptor extends ParserDescriptor {
    private static final String ID = "coverity";
    private static final String NAME = "Coverity Scan";

    CoverityDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CoverityAdapter();
    }

    @Override
    public String getUrl() {
        return "https://scan.coverity.com/";
    }
}
