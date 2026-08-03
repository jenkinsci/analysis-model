package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.IntelephenseParser;

/**
 * A descriptor for Intelephense diagnostics.
 *
 * @author Akash Manna
 */
class IntelephenseDescriptor extends ParserDescriptor {
    private static final String ID = "intelephense";
    private static final String NAME = "Intelephense";

    IntelephenseDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new IntelephenseParser();
    }

    @Override
    public String getPattern() {
        return "**/intelephense-report.json";
    }

    @Override
    public String getHelp() {
        return "Export Intelephense diagnostics as JSON with the publishDiagnostics payload.";
    }

    @Override
    public String getUrl() {
        return "https://intelephense.com/";
    }
}
