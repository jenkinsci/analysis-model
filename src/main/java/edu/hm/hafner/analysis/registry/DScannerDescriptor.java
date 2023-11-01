package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DScannerParser;

/**
 * A descriptor for DScanner.
 *
 * @author Lorenz Munsch
 */
public class DScannerDescriptor extends ParserDescriptor {
    private static final String ID = "dscanner";
    private static final String NAME = "DScanner";

    public DScannerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DScannerParser();
    }

    @Override
    public String getPattern() {
        return "**/dscanner-report.json";
    }

    @Override
    public String getUrl() {
        return "https://github.com/dlang-community/D-Scanner";
    }
}
