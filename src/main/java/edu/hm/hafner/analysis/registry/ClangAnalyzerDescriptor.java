package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClangAnalyzerPlistParser;

/**
 * A descriptor for the Clang parser.
 *
 * @author Lorenz Munsch
 */
class ClangAnalyzerDescriptor extends ParserDescriptor {
    private static final String ID = "clang-analyzer";
    private static final String NAME = "Clang Analyzer";

    ClangAnalyzerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new ClangAnalyzerPlistParser();
    }

    @Override
    public String getHelp() {
        return "Use options --analyze --analyzer-output plist-multi-file";
    }

    @Override
    public String getUrl() {
        return "https://clang-analyzer.llvm.org";
    }
}
