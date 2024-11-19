package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.MyPyParser;

/**
 * Parses MyPy files.
 *
 * @author Ullrich Hafner
 */
public class MyPyAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -6091072858896474363L;

    @Override
    MyPyParser createParser() {
        return new MyPyParser();
    }
}
