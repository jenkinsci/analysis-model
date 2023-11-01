package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.IarParser;

/**
 * A descriptor for the IAR C/C++ compiler.
 *
 * @author Lorenz Munsch
 */
public class IarDescriptor extends ParserDescriptor {
    private static final String ID = "iar";
    private static final String NAME = "IAR Compiler (C/C++)";

    public IarDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new IarParser();
    }

    @Override
    public String getHelp() {
        return "The IAR compilers need to be started with option <strong>--no_wrap_diagnostics</strong>. "
                + "Then the IAR compilers will create single-line warnings.";
    }
}
