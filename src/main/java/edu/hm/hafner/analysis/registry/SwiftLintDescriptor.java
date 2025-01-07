package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CheckStyleParser;

/**
 * A descriptor for SwiftLint. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class SwiftLintDescriptor extends ParserDescriptor {
    private static final String ID = "swiftlint";
    private static final String NAME = "SwiftLint";

    SwiftLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Use configuration reporter: \\”checkstyle\\”.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/realm/SwiftLint";
    }
}
