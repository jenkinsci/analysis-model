package edu.hm.hafner.analysis.registry;

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
        super(ID, NAME, new PmdParser());
    }

    /**
     *
     * Defines the default resultfile name and extension.
     *
     * @return the name of the resultfile
     */
    @Override
    public String getPattern() {
        return "**/pmd.xml";
    }

    /**
     *
     * Determines the checker URL.
     *
     * @return the checker URL or empty String
     */
    @Override
    public String getUrl() {
        return "https://pmd.github.io";
    }
}
