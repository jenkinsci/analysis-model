package edu.hm.hafner.analysis.ast;

/**
 * Converts SHA1 hash codes to long values.
 *
 * @author Ullrich Hafner
 */
public class Sha1ToLongConverter {
    /**
     * Picks 15 hexadecimal characters from a SHA1 hash code and converts these to a long value.
     *
     * @param sha1
     *         the SHA1 hash code with 40 characters
     *
     * @return a long value from the hash code
     */
    public long toLong(final String sha1) {
        StringBuilder stripped = new StringBuilder();
        stripped.append("0x");
        for (int i = 10; i < 40; i += 2) {
            stripped.append(sha1.charAt(i));
        }
        return Long.decode(stripped.toString());
    }
}
