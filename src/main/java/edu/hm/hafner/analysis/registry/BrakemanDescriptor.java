package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.BrakemanParser;

/**
 * A descriptor for the Brakeman Scanner.
 *
 * @author Lorenz Munsch
 */
public class BrakemanDescriptor extends ParserDescriptor {
    private static final String ID = "brakeman";
    private static final String NAME = "Brakeman";

    public BrakemanDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new BrakemanParser();
    }

    @Override
    public String getHelp() {
        return "Reads Brakeman JSON reports. "
                + "Use commandline <code>brakeman -o brakeman-output.json</code> output.<br/>"
                + "See <a href='https://brakemanscanner.org/docs/jenkins/'>"
                + "Brakeman documentation</a> for usage details.";
    }

    @Override
    public String getPattern() {
        return "**/brakeman-output.json";
    }

    @Override
    public String getUrl() {
        return "https://brakemanscanner.org";
    }
}
