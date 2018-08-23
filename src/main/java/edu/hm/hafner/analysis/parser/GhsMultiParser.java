package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpDocumentParser;

/**
 * A parser for the GHS Multi compiler warnings.
 *
 * @author Joseph Boulos
 */
public class GhsMultiParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 8149238560432255036L;

    private static final String GHS_MULTI_WARNING_PATTERN =
            "(?:\\.|[A-Z]:)(.*)\"\\,\\s*line\\s*(\\d+):\\s*(warning|error)\\s*([^:]+):\\s*(?m)([^\\^]*)\\s*\\^";

    /**
     * Creates a new instance of {@link GhsMultiParser}.
     */
    public GhsMultiParser() {
        super(GHS_MULTI_WARNING_PATTERN, true);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(3));
        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(matcher.group(4))
                .setMessage(matcher.group(5))
                .setPriority(mapPriority(type))
                .build();
    }

    private Priority mapPriority(final String type) {
        Priority priority;
        if ("warning".equalsIgnoreCase(type)) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }
        return priority;
    }
}

