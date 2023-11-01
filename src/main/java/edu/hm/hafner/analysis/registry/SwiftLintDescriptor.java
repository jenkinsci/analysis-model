package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for SwiftLint. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
public class SwiftLintDescriptor extends ParserDescriptor {
    private static final String ID = "swiftlint";
    private static final String NAME = "SwiftLint";

    public SwiftLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
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
