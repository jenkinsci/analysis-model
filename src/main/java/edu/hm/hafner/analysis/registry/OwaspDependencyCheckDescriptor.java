package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.OwaspDependencyCheckParser;

/**
 * Descriptor for OWASP dependency check report parser.
 */
class OwaspDependencyCheckDescriptor extends ParserDescriptor {
    private static final String ID = "owasp-dependency-check";
    private static final String NAME = "OWASP Dependency Check";

    OwaspDependencyCheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new OwaspDependencyCheckParser();
    }

    @Override
    public String getPattern() {
        return "**/dependency-check-report.json";
    }

    @Override
    public String getUrl() {
        return "https://github.com/jeremylong/DependencyCheck";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/jeremylong/DependencyCheck/main/src/site/resources/images/logo.svg";
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }
}
