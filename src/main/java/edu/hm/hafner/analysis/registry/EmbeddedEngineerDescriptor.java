package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.EmbeddedEngineerParser;

/**
 * A descriptor for the EmbeddedEngineer EA Code Generator tool.
 *
 * @author Eva Habeeb
 */
public class EmbeddedEngineerDescriptor extends ParserDescriptor {
    private static final String ID = "embedded-engineer";
    private static final String NAME = "Embedded Engineer Tool";

    public EmbeddedEngineerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new EmbeddedEngineerParser();
    }

    @Override
    public String getUrl() {
        return "https://www.lieberlieber.com/embedded-engineer/";
    }
}
