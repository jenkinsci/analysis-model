package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.Severity.*;

/**
 * A parser for gcc 4.x linker warnings.
 *
 * @author Frederic Chateau
 */
public class Gcc4LinkerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -2792019431810134790L;

    /** A GCC error. */
    static final String WARNING_CATEGORY = "GCC4 Linker Error";
    /** Pattern of gcc 4 linker warnings. */
    private static final String LINKER_WARNING_PATTERN = "^(?:(.+?)(?:(?::(?:(\\d+):)? (undefined reference to .*))|"
            + "(?::?\\(\\.\\w+\\+0x[0-9a-fA-F]+\\)): (?:(warning): )?(.*))|.*/ld(?:\\.exe)?: (?:(warning): )?(.*))$";

    /**
     * Creates a new instance of {@link Gcc4LinkerParser}.
     */
    public Gcc4LinkerParser() {
        super(LINKER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        if (StringUtils.isNotBlank(matcher.group(7))) {
            parseLdError(matcher, builder);
        }
        else {
            // link error
            if (StringUtils.isNotBlank(matcher.group(3))) {
                // error of type "undefined reference..."
                builder.setMessage(matcher.group(3));
                builder.setSeverity(WARNING_HIGH);
            }
            else {
                // generic linker error with reference to the binary section and
                // offset
                if (StringUtils.equalsIgnoreCase(matcher.group(4), "warning")) {
                    builder.setSeverity(WARNING_NORMAL);
                }
                else {
                    builder.setSeverity(WARNING_HIGH);
                }
                String message = matcher.group(5);
                builder.setMessage(message);
                if (StringUtils.endsWith(message, ":")) {
                    return Optional.empty();
                }
            }
        }

        return builder.setFileName(StringUtils.defaultString(matcher.group(1)))
                .setLineStart(matcher.group(2))
                .setCategory(WARNING_CATEGORY)
                .buildOptional();
    }

    private void parseLdError(final Matcher matcher, final IssueBuilder builder) {
        if (StringUtils.equalsIgnoreCase(matcher.group(6), "warning")) {
            builder.setSeverity(WARNING_NORMAL);
        }
        else {
            builder.setSeverity(WARNING_HIGH);
        }
        builder.setMessage(matcher.group(7));
    }
}
