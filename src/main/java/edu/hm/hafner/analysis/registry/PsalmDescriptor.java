package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PsalmParser;

/**
 * A descriptor for Psalm JSON reports.
 *
 * @author Akash Manna
 */
class PsalmDescriptor extends ParserDescriptor {
    private static final String ID = "psalm";
    private static final String NAME = "Psalm";

    PsalmDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PsalmParser();
    }

    @Override
    public String getPattern() {
        return "**/psalm-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>psalm --output-format=json > psalm-report.json</code> to generate JSON output."
                + " See <a href='https://github.com/vimeo/psalm'>Psalm on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://psalm.dev/";
    }
}