package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the flawfinder warnings.
 *
 * @author Dom Postorivo
 */
public class FlawfinderParser extends RegexpLineParser {
    private static final long serialVersionUID = 8088991846076174837L;

    private static final String FLAWFINDER_WARNING_PATTERN =
            "^(?<file>.*):(?<line>\\d+): .\\[(?<severity>[012345])\\] \\((?<category>[a-z0-9]*)\\) (?<message>.*)$";

    private static final int FLAWFINDER_HIGH_THRESHOLD = 4;
    private static final int FLAWFINDER_NORMAL_THRESHOLD = 2;

    /**
     * Creates a new instance of {@link FlawfinderParser}.
     */
    public FlawfinderParser() {
        super(FLAWFINDER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group("message");
        String category = matcher.group("category");
        int severity = Integer.parseInt(matcher.group("severity"));
        Severity priority = Severity.WARNING_LOW;

        if (severity >= FLAWFINDER_HIGH_THRESHOLD) {
            priority = Severity.WARNING_HIGH;
        }
        else if (severity >= FLAWFINDER_NORMAL_THRESHOLD) {
            priority = Severity.WARNING_NORMAL;
        }

        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(priority)
                .buildOptional();
    }
}
