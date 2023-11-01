package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.CodeNarcAdapter;

/**
 * A descriptor for CodeNarc.
 *
 * @author Lorenz Munsch
 */
public class CodeNarcDescriptor extends ParserDescriptor {
    private static final String ID = "codenarc";
    private static final String NAME = "CodeNarc";

    public CodeNarcDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CodeNarcAdapter();
    }
}
