package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.LintParser;
import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A Descriptor for the Maven Console parser.
 *
 * @author Lorenz Munsch
 */
public class MavenConsoleDescriptor extends ParserDescriptor {

    private static final String ID = "maven_console";
    private static final String NAME = "MavenConsole";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public MavenConsoleDescriptor() {
        super(ID, NAME, new MavenConsoleParser());
    }
}
