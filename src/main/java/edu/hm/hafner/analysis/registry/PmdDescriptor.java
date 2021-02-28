package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * A descriptor for the Pmd warnings.
 *
 * @author Lorenz Munsch
 */
class PmdDescriptor extends ParserDescriptor {
    private static final String ID = "pmd";
    private static final String NAME = "PMD";

    PmdDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new PmdParser();
    }

    @Override
    public String getPattern() {
        return "**/pmd.xml";
    }

    @Override
    public String getUrl() {
        return "https://pmd.github.io";
    }
}
