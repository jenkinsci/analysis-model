package edu.hm.hafner.analysis;

import java.util.function.Function;

/**
 * Removes Jenkins console notes.
 *
 * @author Ullrich Hafner
 */
// TODO: remove this class and move the using unit test to the Jenkins plugin
public class ConsolePostProcessor implements Function<String, String> {
    private static final String PREAMBLE_STR = "\u001B[8mha:";
    private static final String POSTAMBLE_STR = "\u001B[0m";

    @Override
    public String apply(final String input) {
        String line = input;
        while (true) {
            int idx = line.indexOf(PREAMBLE_STR);
            if (idx < 0) {
                return line;
            }
            int e = line.indexOf(POSTAMBLE_STR, idx);
            if (e < 0) {
                return line;
            }
            line = line.substring(0, idx) + line.substring(e + POSTAMBLE_STR.length());
        }
    }
}
