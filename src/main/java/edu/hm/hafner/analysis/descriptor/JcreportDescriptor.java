package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.jcreport.JcReportParser;

/**
 * A Descriptor for the Jcreport warnings.
 *
 * @author Lorenz Munsch
 */
public class JcreportDescriptor extends ParserDescriptor {

    private static final String ID = "jc-report";
    private static final String NAME = "JCReport";

    public JcreportDescriptor() {
        super(ID, NAME, new JcReportParser());
    }
}
