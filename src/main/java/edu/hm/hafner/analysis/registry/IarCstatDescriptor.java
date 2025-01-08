package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.IarCstatParser;

/**
 * A descriptor for the IAR C-Stat static analysis tool.
 *
 * @author Lorenz Munsch
 */
class IarCstatDescriptor extends ParserDescriptor {
    private static final String ID = "iar-cstat";
    private static final String NAME = "IAR C-STAT";

    IarCstatDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new IarCstatParser();
    }

    @Override
    public String getHelp() {
        return """
                <p>The IAR C-STAT static analysis tool finds potential issues in code by doing an analysis \
                on the source code level. Use the following icstat command to generate the output on \
                stdout in the correct format: <pre><code>\
                icstat --db a.db --checks checks.ch commands commands.txt\
                </code></pre> where the commands.txt contains: <pre><code>\
                analyze - iccxxxxcompiler_opts cstat1.c
                analyze - iccxxxxcompiler_opts cstat2.c\
                </pre></code>\
                For details check the IAR C-STAT guide.</p>""";
    }
}
