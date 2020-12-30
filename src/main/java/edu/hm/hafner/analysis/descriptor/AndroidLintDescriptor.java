package edu.hm.hafner.analysis.descriptor;


import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.AndroidLintParserAdapter;

/**
 * A Descriptor for the AndroidLint warnings.
 *
 * @author Lorenz Munsch
 */
public class AndroidLintDescriptor implements Descriptor {

    private static final String ID = "AndroidLint";

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
        return new AndroidLintParserAdapter();
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
