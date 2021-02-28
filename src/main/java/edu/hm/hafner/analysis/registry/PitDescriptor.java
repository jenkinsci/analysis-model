package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.analysis.parser.violations.PitAdapter;

/**
 * A descriptor for PIT (mutation testing)
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
    public IssueParser createParser() {
        return new PitAdapter();
    }

    @Override
    public String getUrl() {
        return "http://pitest.org";
    }
}
