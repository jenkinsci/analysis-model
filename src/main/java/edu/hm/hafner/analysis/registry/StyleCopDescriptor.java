package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.StyleCopParser;

/**
 * A descriptor for StyleCop.
 *
 * @author Lorenz Munsch
 */
public class StyleCopDescriptor extends ParserDescriptor {
    private static final String ID = "stylecop";
    private static final String NAME = "StyleCop";

    public StyleCopDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new StyleCopParser();
    }
}
