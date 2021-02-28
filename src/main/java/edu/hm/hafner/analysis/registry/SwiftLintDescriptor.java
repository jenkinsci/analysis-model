package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SunCParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

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
    public IssueParser createParser() {
        return new CheckStyleParser();
    }
}
