package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.BuckminsterParser;

/**
 * A Descriptor for the Buckminster warnings.
 *
 * @author Lorenz Munsch
 */
public class BuckminsterDescriptor implements Descriptor {

    private static final String ID = "Buckminster";

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
        return new BuckminsterParser();
    }

    /**
     *
     * Defines the default resultfile name and extension.
     *
     * @return the name of the resultfile
     */
    @Override
    public String getPattern() {
        return "";
    }

    /**
     *
     * Determines the checker URL.
     *
     * @return the checker URL or empty String
     */
    @Override
    public String getUrl() {
        return "";
    }
}
