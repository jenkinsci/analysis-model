package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ErlcParser;
import edu.hm.hafner.analysis.parser.ErrorProneParser;

/**
 * A Descriptor for the Error Prone parser.
 *
 * @author Lorenz Munsch
 */
public class ErrorProneDescriptor extends ParserDescriptor {

    private static final String ID = "error_prone";
    private static final String NAME = "ErrorProne";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public ErrorProneDescriptor() {
        super(ID, NAME, new ErrorProneParser());
    }
}
