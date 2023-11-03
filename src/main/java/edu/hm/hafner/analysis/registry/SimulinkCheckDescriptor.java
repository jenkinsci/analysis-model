package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SimulinkCheckParser;

/**
 * A descriptor for Simulink Check tool by Mathworks.
 *
 * @author Eva Habeeb
 */

class SimulinkCheckDescriptor extends ParserDescriptor {

    private static final String ID = "simulink-check-parser";
    private static final String NAME = "Simulink Check Tool";

    SimulinkCheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new SimulinkCheckParser();
    }

    @Override
    public String getUrl() {
        return "https://www.mathworks.com/products/simulink-check.html";
    }

    @Override
    public String getHelp() {
        return "Reads and Parses HTML reports of Simulink Check Tool by MathWorks. "
                + "Report can be generated with command: "
                + "<code>ModelAdvisor.summaryReport(ModelAdvisor.run(<SYSTEMS>, <CONFIG>, <FILENAME>, <ARGS>))</code>";
    }
}
