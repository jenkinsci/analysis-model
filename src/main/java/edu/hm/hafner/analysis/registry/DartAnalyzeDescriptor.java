package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.DartAnalyzeParserAdapter;

/**
 * A descriptor for the Dart analyze parser.
 *
 * @author Ullrich Hafner
 */
public class DartAnalyzeDescriptor extends ParserDescriptor {
    private static final String ID = "dart";
    private static final String NAME = "Dart Analyze";

    public DartAnalyzeDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DartAnalyzeParserAdapter();
    }

    @Override
    public String getUrl() {
        return "https://dart.dev/";
    }
}
