package edu.hm.hafner.util;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * A simple helper class in the style of {@link StringUtils} that provides methods to check if strings contain
 * search strings.
 *
 * @author Ullrich Hafner
 */
public final class StringContainsUtils {
    /**
     * Checks if the provided string contains irrespective of case any of the strings in the given array,
     * handling {@code null} strings. Case-insensitivity is defined as by {@link String#equalsIgnoreCase(String)}.
     *
     * <p>
     * A {@code null} {@code cs} CharSequence will return {@code false}. A {@code null} or zero length search array will
     * return {@code false}.
     * </p>
     *
     * <pre>
     * StringUtils.containsAny(null, *)            = false
     * StringUtils.containsAny("", *)              = false
     * StringUtils.containsAny(*, null)            = false
     * StringUtils.containsAny(*, [])              = false
     * StringUtils.containsAny("abcd", "ab", null) = true
     * StringUtils.containsAny("abcd", "ab", "cd") = true
     * StringUtils.containsAny("abc", "d", "abc")  = true
     * StringUtils.containsAny("ABC", "d", "abc")  = true
     * </pre>
     *
     * @param input
     *         The string to check, may be {@code null}
     * @param searchTexts
     *         The strings to search for, may be empty. Individual CharSequences may be null as well.
     *
     * @return {@code true} if any of the search CharSequences are found, {@code false} otherwise
     * @since 3.4
     */
    public static boolean containsAnyIgnoreCase(@Nullable final CharSequence input, @Nullable final String... searchTexts) {
        if (StringUtils.isEmpty(input)) {
            return false;
        }
        if (searchTexts == null || searchTexts.length == 0) {
            return false;
        }

        for (String searchText : searchTexts) {
            if (StringUtils.containsIgnoreCase(input, searchText)) {
                return true;
            }
        }
        return false;
    }

    private StringContainsUtils() {
        // prevents instantiation
    }
}
