package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.XmlLintAdapter;

/**
 * A descriptor for XML-Lint.
 *
 * @author Lorenz Munsch
 */
public class XmlLintDescriptor extends ParserDescriptor {
    private static final String ID = "xmllint";
    private static final String NAME = "XML-Lint";

    public XmlLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new XmlLintAdapter();
    }
}
