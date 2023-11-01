package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.jcreport.JcReportParser;

/**
 * A descriptor for the JcReport compiler.
 *
 * @author Lorenz Munsch
 */
public class JcreportDescriptor extends ParserDescriptor {
    private static final String ID = "jc-report";
    private static final String NAME = "JCReport";

    public JcreportDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new JcReportParser();
    }
}
