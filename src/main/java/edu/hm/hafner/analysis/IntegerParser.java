package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;

import org.apache.commons.lang3.StringUtils;

/**
 * Parses integers from string values.
 *
 * @author Ullrich Hafner
 */
public class IntegerParser {
    /**
     * Converts a string line number to an integer value. If the string is not a valid line number, then 0 is returned
     * which indicates an Issue at the top of the file.
     *
     * @param lineNumber the line number (as a string)
     * @return the line number
     */
    public int parseInt(@CheckForNull final String lineNumber) {
        if (StringUtils.isNotBlank(lineNumber)) {
            try {
                return Integer.parseInt(lineNumber);
            }
            catch (NumberFormatException ignored) {
                // ignore and return 0
            }
        }
        return 0;
    }
}
