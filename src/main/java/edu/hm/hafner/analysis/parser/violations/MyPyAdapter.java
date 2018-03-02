package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.MyPyParser;

/**
 * Parses MyPy files.
 *
 * @author Ullrich Hafner
 */
public class MyPyAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -6091072858896474363L;

    /**
     * Creates a new instance of {@link MyPyAdapter}.
     */
    public MyPyAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected MyPyParser createParser() {
        return new MyPyParser();
    }
}
