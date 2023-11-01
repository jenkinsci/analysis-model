package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ProtoLintParser;

/**
 * A descriptor for ProtoLint.
 *
 * @author Lorenz Munsch
 */
public class ProtoLintDescriptor extends ParserDescriptor {
    private static final String ID = "protolint";
    private static final String NAME = "ProtoLint";

    public ProtoLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ProtoLintParser();
    }

    @Override
    public String getUrl() {
        return "https://github.com/yoheimuta/protolint";
    }
}
