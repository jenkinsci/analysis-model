package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DoxygenParser;

/**
 * A descriptor for Doxygen.
 *
 * @author Lorenz Munsch
 */
public class DoxygenDescriptor extends ParserDescriptor {
    private static final String ID = "doxygen";
    private static final String NAME = "Doxygen";

    public DoxygenDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DoxygenParser();
    }

    @Override
    public String getHelp() {
        return "Execute doxygen:"
              + "As <b>shell</b> command <code>( cat Doxyfile; echo WARN_FORMAT='$file:$line: $text' ) | doxygen -</code>"
              + "As <b>batch</b> command <code>( type Doxyfile & echo WARN_FORMAT='$file:$line: $text' ) | doxygen -</code>";
    }
}
