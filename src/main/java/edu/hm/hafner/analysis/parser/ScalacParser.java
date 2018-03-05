package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the scalac compiler warnings. You should use -feature and -deprecation compiler opts.
 *
 * @author Alexey Kislin
 */
public class ScalacParser extends RegexpLineParser {
    private static final long serialVersionUID = -4034552404001800574L;

    private static final String SCALAC_WARNING_PATTERN = "^(\\[WARNING\\]|\\[ERROR\\])\\s*(.*):(\\d+):\\s*([a-z]*)"
            + ":\\s*(.*)$";

    /**
     * Creates a new instance of {@link ScalacParser}.
     */
    public ScalacParser() {
        super(SCALAC_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(2))
                .setLineStart(parseInt(matcher.group(3)))
                .setCategory(matcher.group(4))
                .setMessage(matcher.group(5))
                .setPriority(mapPriority(matcher))
                .build();
    }

    private Priority mapPriority(final Matcher matcher) {
        return "[ERROR]".equals(matcher.group(1)) ? Priority.HIGH : Priority.NORMAL;
    }
}
