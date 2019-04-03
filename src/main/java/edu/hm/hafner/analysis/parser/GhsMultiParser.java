package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the GHS Multi compiler warnings.
 *
 * @author Joseph Boulos
 */
public class GhsMultiParser extends RegexpLineParser {
    private static final long serialVersionUID = 8149238560432255036L;

    private static final String GHS_MULTI_WARNING_PATTERN =
            //"(?:\\.|[A-Z]:)(.*)\"\\,\\s*line\\s*(\\d+):\\s*(warning|error)\\s*([^:]+):\\s*(?m)([^\\^]*)\\s*\\^";
            "(?:\\.|[A-Z]:)(.*)\"\\,\\s*line\\s*(\\d+):\\s*(warning|error)\\s*([^:]+):";

    /**
     * Creates a new instance of {@link GhsMultiParser}.
     */
    public GhsMultiParser() {
        super(GHS_MULTI_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(3));
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .setMessage("TODO: Gather all lines untill ^ Symbol")
                .setSeverity(mapPriority(type))
                .buildOptional();
    }

    private Severity mapPriority(final String type) {
        Severity priority;
        if ("warning".equalsIgnoreCase(type)) {
            priority = Severity.WARNING_NORMAL;
        }
        else {
            priority = Severity.WARNING_HIGH;
        }
        return priority;
    }
}

