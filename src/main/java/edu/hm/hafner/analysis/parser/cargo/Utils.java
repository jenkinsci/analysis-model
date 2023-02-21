package edu.hm.hafner.analysis.parser.cargo;

/**
 * Utility functions to use within the Cargo Clippy parser.
 *
 * @author Mike Delaney
 */
public final class Utils {
    private Utils() {
        throw new UnsupportedOperationException("Utility class and cannot be instantiated");
    }

    /**
     * Calculate the number of times a character occurs in a string.
     *
     * @param str
     *     The string to count the character in.
     * @param c
     *     The character to search for.
     * @return The occurrence count, or zero.
     */
    public static int countChar(final String str, final char c) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }

        return count;
    }
}
