package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for the CheckStyle warnings.
 *
 * @author Lorenz Munsch
 */
class CheckstyleDescriptor extends ParserDescriptor {
    private static final String ID = "checkstyle";
    private static final String NAME = "CheckStyle";

    CheckstyleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new CheckStyleParser();
    }

    @Override
    public String getPattern() {
        return "**/checkstyle-result.xml";
    }

    @Override
    public String getUrl() {
        return "https://checkstyle.org";
    }
}