package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for IbLinter. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class IbLinterDescriptor extends ParserDescriptor {
    private static final String ID = "iblinter";
    private static final String NAME = "IbLinter";

    IbLinterDescriptor() {
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
        return "https://github.com/IBDecodable/IBLinter";
    }
}
