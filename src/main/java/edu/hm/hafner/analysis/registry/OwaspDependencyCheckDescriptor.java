package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.OwaspDependencyCheckParser;

/**
 * Descriptor for OWASP dependency check report parser.
 */
public class OwaspDependencyCheckDescriptor extends ParserDescriptor {
    private static final String ID = "owasp-dependency-check";
    private static final String NAME = "OWASP Dependency Check";

    OwaspDependencyCheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new OwaspDependencyCheckParser();
    }

    @Override
    public String getPattern() {
        return "**/dependency-check-report.json";
    }
}
