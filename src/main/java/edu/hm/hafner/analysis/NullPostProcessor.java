package edu.hm.hafner.analysis;

/**
 * Returns the line to parse without any modifications.
 *
 * @author Ullrich Hafner
 */
public class NullPostProcessor implements PostProcessor {
    @Override
    public String processLine(final String input) {
        return input;
    }
}
