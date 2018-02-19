package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the erlc compiler warnings.
 *
 * @author Stefan Brausch
 */
public class ErlcParser extends RegexpLineParser {
    private static final long serialVersionUID = 8986478184830773892L;

    private static final String ERLC_WARNING_PATTERN = "^(.+\\.(?:erl|yrl|mib|bin|rel|asn1|idl)):(\\d*): ([wW]arning:"
            + " )?(.+)$";

    /**
     * Creates a new instance of {@link ErlcParser}.
     */
    public ErlcParser() {
        super(ERLC_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        Priority priority;
        String category;
        String categoryMatch = matcher.group(3);

        if ("warning: ".equalsIgnoreCase(categoryMatch)) {
            priority = Priority.NORMAL;
            category = categoryMatch.substring(0, categoryMatch.length() - 2);
        }
        else {
            priority = Priority.HIGH;
            category = "Error";
        }
        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(matcher.group(4))
                .setPriority(priority)
                .build();
    }
}

