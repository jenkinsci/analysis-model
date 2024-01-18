package edu.hm.hafner.analysis.parser;

/**
 * A parser for Doxygen.
 *
 * @author Ladislav Moravek
 */
public class DoxygenParser extends Gcc4CompilerParser {
    private static final long serialVersionUID = 8760302999081711502L;

    private static final String DOXYGEN_WARNING_PATTERN =
            ANT_TASK + "(?:(.+?):(\\d+):(?:(\\d+):)?)? ?([wW]arning|.*[Ee]rror): (.*)$";

    /**
     * Creates a new instance of {@link DoxygenParser}.
     */
    public DoxygenParser() {
        super(DOXYGEN_WARNING_PATTERN);
    }
}
