package edu.hm.hafner.analysis;

import javax.annotation.Nullable;

/**
 * Returns the line to parse without any modifications.
 *
 * @author Ullrich Hafner
 */
public class NullPostProcessor implements PostProcessor {
    @Nullable
    @Override
    public String apply(@Nullable final String input) {
        return input;
    }
}
