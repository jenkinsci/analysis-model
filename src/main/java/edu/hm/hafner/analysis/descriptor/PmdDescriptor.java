package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * A Descriptor for the Pmd warnings.
 *
 * @author Lorenz Munsch
 */
public class PmdDescriptor implements Descriptor {

    private static final String ID = "Pmd";

    /**
     *
     * Name to identify the warning.
     *
     * @return the identification string
     */
    @Override
    public String getName() {
        return ID;
    }

    /**
     *
     * Creates a new Parser.
     *
     * @return the parser
     */
    @Override
    public IssueParser createParser() {
        return new PmdParser();
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
