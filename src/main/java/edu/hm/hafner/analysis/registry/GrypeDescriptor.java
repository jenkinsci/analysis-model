package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GrypeParser;

/**
 * Descriptor for Grype report parser.
 */
public class GrypeDescriptor extends ParserDescriptor {
    private static final String ID = "grype";
    private static final String NAME = "Grype";

    GrypeDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new GrypeParser();
    }

    @Override
    public String getPattern() {
        return "**/grype-report.json";
    }

    @Override
    public String getUrl() {
        return "https://github.com/anchore/grype";
    }

    @Override
    public String getIconUrl() {
        return "https://user-images.githubusercontent.com/5199289/136855393-d0a9eef9-ccf1-4e2b-9d7c-7aad16a567e5.png";
    }

}
