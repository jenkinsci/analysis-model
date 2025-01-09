package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.DartAnalyzeParserAdapter;

/**
 * A descriptor for the Dart analyze parser.
 *
 * @author Ullrich Hafner
 */
class DartAnalyzeDescriptor extends ParserDescriptor {
    private static final String ID = "dart";
    private static final String NAME = "Dart Analyze";

    DartAnalyzeDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new DartAnalyzeParserAdapter();
    }

    @Override
    public String getUrl() {
        return "https://dart.dev/";
    }
}
