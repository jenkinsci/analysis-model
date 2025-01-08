package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.XmlLintAdapter;

/**
 * A descriptor for XML-Lint.
 *
 * @author Lorenz Munsch
 */
class XmlLintDescriptor extends ParserDescriptor {
    private static final String ID = "xmllint";
    private static final String NAME = "XML-Lint";

    XmlLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new XmlLintAdapter();
    }
}
