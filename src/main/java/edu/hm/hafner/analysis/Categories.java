package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Provides convenience methods to detect categories. 
 *
 * @author Ullrich Hafner
 */
public final class Categories {
    /** Category for warnings due to usage of deprecate API. */
    public static final String DEPRECATION = "Deprecation";
    /** Category for warnings due to the usage of proprietary API. */
    public static final String PROPRIETARY_API = "Proprietary API";
    /** Category for Javadoc warnings. */
    public static final String JAVADOC = "Javadoc";
    /** Category for Other warnings. */
    public static final String OTHER = "Other";

    /**
     * Classifies the warning message: tries to guess a category from the warning message.
     *
     * @param message
     *         the message to check
     *
     * @return warning category, empty string if unknown
     */
    public static String guessCategory(@CheckForNull final String message) {
        if (StringUtils.contains(message, "proprietary")) {
            return PROPRIETARY_API;
        }
        if (StringUtils.contains(message, "deprecated")) {
            return DEPRECATION;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Returns a category for the current warning. If the provided category is not empty, then a capitalized string is
     * returned. Otherwise the category is obtained from the specified message text.
     *
     * @param category
     *         the warning category (might be empty)
     * @param message
     *         the warning message
     *
     * @return the actual category
     */
    public static String guessCategoryIfEmpty(@CheckForNull final String category, @CheckForNull final String message) {
        String capitalized = StringUtils.capitalize(category);
        if (StringUtils.isEmpty(capitalized)) {
            capitalized = guessCategory(message);
        }
        return capitalized;
    }

    private Categories() {
        // prevents instantiation
    }
}
