package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.PitAdapter;

/**
 * A descriptor for PIT (mutation testing).
 *
 * @author Lorenz Munsch
 */
class PitDescriptor extends ParserDescriptor {
    private static final String ID = "pit";
    private static final String NAME = "PIT";

    PitDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PitAdapter();
    }

    @Override
    public String getPattern() {
        return "**/mutations.xml";
    }

    @Override
    public String getUrl() {
        return "https://pitest.org";
    }

    @Override
    public String getIconUrl() {
        return "https://pitest.org/images/pit-black-150x152.png";
    }
}
