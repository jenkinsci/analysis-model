package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SpectralParser;

/**
 * A descriptor for Spectral JSON reports.
 *
 * @author Akash Manna
 */
class SpectralDescriptor extends ParserDescriptor {
    private static final String ID = "spectral";
    private static final String NAME = "Spectral";

    SpectralDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new SpectralParser();
    }

    @Override
    public String getPattern() {
        return "**/spectral-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>spectral lint api.yaml --format json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/stoplightio/spectral'>Spectral on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/stoplightio/spectral";
    }
}