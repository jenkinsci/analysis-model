package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClairParser;

/**
 * A descriptor for {@code clair-scanner} json report.
 *
 * @author Lorenz Munsch
 */
class ClairDescriptor extends ParserDescriptor {
    private static final String ID = "clair";
    private static final String NAME = "Clair Scanner";

    ClairDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ClairParser();
    }

    @Override
    public String getHelp() {
        return "Reads Clair json data. "
                + "Use commandline <code>clair-scanner --report=\"/target/clair.json\"</code> output.<br/>"
                + "See <a href='https://github.com/arminc/clair-scanner'>"
                + "clair-scanner on Github</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/arminc/clair-scanner";
    }

    @Override
    public Type getType() {
        return Type.VULNERABILITY;
    }
}
