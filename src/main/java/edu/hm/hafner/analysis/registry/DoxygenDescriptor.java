package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DoxygenParser;

/**
 * A descriptor for Doxygen.
 *
 * @author Lorenz Munsch
 */
class DoxygenDescriptor extends ParserDescriptor {
    private static final String ID = "doxygen";
    private static final String NAME = "Doxygen";

    DoxygenDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new DoxygenParser();
    }

    @Override
    public String getUrl() {
        return "https://www.doxygen.nl/";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/doxygen/doxygen/master/doc/doxygen_logo.svg";
    }

    @Override
    public String getHelp() {
        return "Execute doxygen:"
              + "As <b>shell</b> command <code>( cat Doxyfile; echo WARN_FORMAT='$file:$line: $text' ) | doxygen -</code>"
              + "As <b>batch</b> command <code>( type Doxyfile & echo WARN_FORMAT='$file:$line: $text' ) | doxygen -</code>";
    }
}
