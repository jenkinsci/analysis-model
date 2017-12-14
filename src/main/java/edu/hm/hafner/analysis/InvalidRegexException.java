package edu.hm.hafner.analysis;

import java.util.regex.PatternSyntaxException;

import edu.hm.hafner.analysis.IssueFilter.IssueProperty;
import static java.lang.String.*;

/**
 * Runtime exception thrown if an invalid regex is provided.
 *
 * @author Marcel Binder
 */
public class InvalidRegexException extends RuntimeException {
    private static final String MESSAGE = "Invalid regex for Issue property %s: %s";
    private final IssueProperty property;
    private final String invalidRegex;

    /**
     * Create a new {@link InvalidRegexException}.
     *
     * @param property the property for which an invalid regex has been provided
     * @param invalidRegex the invaid regex
     * @param cause the cause
     */
    InvalidRegexException(final IssueProperty property, final String invalidRegex, final PatternSyntaxException cause) {
        super(format(MESSAGE, property.name(), invalidRegex), cause);
        this.property = property;
        this.invalidRegex = invalidRegex;
    }

    /**
     * @return the property for which an invalid regex has been provided
     */
    public IssueProperty getProperty() {
        return property;
    }

    /**
     * @return the invalid regex
     */
    public String getInvalidRegex() {
        return invalidRegex;
    }
}
