package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.MyPyAdapter;

/**
 * A descriptor for MyPy.
 *
 * @author Lorenz Munsch
 */
public class MyPyDescriptor extends ParserDescriptor {
    private static final String ID = "mypy";
    private static final String NAME = "MyPy";

    public MyPyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new MyPyAdapter();
    }
}
