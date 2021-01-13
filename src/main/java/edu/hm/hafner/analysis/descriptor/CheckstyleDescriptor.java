package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A Descriptor for the Checkstyle warnings.
 *
 * @author Lorenz Munsch
 */
public class CheckstyleDescriptor extends ParserDescriptor {

    private static final String ID = "checkstyle";
    private static final String NAME = "Checkstyle";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CheckstyleDescriptor() {
        super(ID, NAME, new CheckStyleParser());
    }

    /**
     *
     * Defines the default resultfile name and extension.
     *
     * @return the name of the resultfile
     */
    @Override
    public String getPattern() {
        return "**/checkstyle-result.xml";
    }

    /**
     *
     * Determines the checker URL.
     *
     * @return the checker URL or empty String
     */
    @Override
    public String getUrl() {
        return "https://checkstyle.org";
    }

}
