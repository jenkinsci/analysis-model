package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PolyspaceParser;

/**
 * A descriptor for Polyspace tool.
 *
 * @author Eva Habeeb
 */

public class PolyspaceDescriptor extends ParserDescriptor {
    private static final String ID = "PolyspaceParse";
    private static final String NAME = "Polyspace Tool";

    PolyspaceDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PolyspaceParser();
    }

    @Override
    public String getUrl() {
        return "https://www.mathworks.com/products/polyspace.html";
    }

    @Override
    public String getHelp() {
        return "Reads reports of Polyspace Static Analysis Tool by MathWorks. "
                + "Used for <a href='https://www.mathworks.com/help/bugfinder/ref/polyspaceresultsexport.html?s_tid=srchtitle_polyspace-results-export_1'>BugFinder</a> and  <a href='https://www.mathworks.com/help/codeprover/ref/polyspaceresultsexport.html?s_tid=srchtitle_polyspace-results-export_2'>CodeProver</a> result files.<br/>"
                + "Report can be generated with command: "
                + "polyspace-results-export -format csv -results-dir <RESULTS> -output-name <CSVFILE> -key <KEY>";
    }
}
