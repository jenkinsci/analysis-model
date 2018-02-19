package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the Texas Instruments Code Composer Studio compiler warnings.
 *
 * @author Jan Linnenkohl
 */
public class TiCcsParser extends RegexpLineParser {
    private static final long serialVersionUID = -8253481365175984661L;

    private static final String TI_CCS_WARNING_PATTERN = "^((\"(.*)\",\\s*)(line\\s*(\\d+)(\\s*\\(.*\\))?:)?\\s*)?"
            + "(WARNING|ERROR|remark|warning|(fatal\\s*)?error)(!\\s*at line\\s(\\d+))?\\s*([^:]*)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link TiCcsParser}.
     */
    public TiCcsParser() {
        super(TI_CCS_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String lineNumber = matcher.group(5);
        if (StringUtils.isBlank(lineNumber)) {
            lineNumber = matcher.group(10);
        }
        return builder.setFileName(matcher.group(3))
                .setLineStart(parseInt(lineNumber))
                .setCategory(matcher.group(11))
                .setMessage(matcher.group(12))
                .setPriority(mapPriority(matcher))
                .build();
    }

    private Priority mapPriority(final Matcher matcher) {
        if (isOfType(matcher, "remark")) {
            return Priority.LOW;
        }
        else if (isOfType(matcher, "warning")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.HIGH;
        }
    }

    /**
     * Returns whether the warning type is of the specified type.
     *
     * @param matcher
     *         the matcher
     * @param type
     *         the type to match with
     *
     * @return {@code true} if the warning type is of the specified type
     */
    private boolean isOfType(final Matcher matcher, final String type) {
        return StringUtils.containsIgnoreCase(matcher.group(7), type);
    }
}

