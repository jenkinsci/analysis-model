package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CpdParser;

/**
 * A descriptor for the CPD parser.
 *
 * @author Lorenz Munsch
 */
class CpdDescriptor extends DryDescriptor {
    private static final String ID = "cpd";
    private static final String NAME = "CPD";

    CpdDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CpdParser(getHighThreshold(options), getNormalThreshold(options));
    }

    @Override
    public String getPattern() {
        return "**/cpd.xml";
    }

    @Override
    public String getUrl() {
        return "https://pmd.github.io/latest/pmd_userdocs_cpd.html";
    }
}
