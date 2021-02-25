package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.jcreport.JcReportParser;

/**
 * A Descriptor for the Jcreport warnings.
 *
 * @author Lorenz Munsch
 */
class JcreportDescriptor extends ParserDescriptor {
    private static final String ID = "jc-report";
    private static final String NAME = "JCReport";

    JcreportDescriptor() {
        super(ID, NAME, new JcReportParser());
    }
}
