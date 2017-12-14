package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the sbt scala compiler warnings. You should use -feature & -deprecation compiler opts.
 *
 * @author <a href="mailto:hochak@gmail.com">Hochak Hung</a>
 */
public class SbtScalacParser extends RegexpLineParser {
    private static final String SBT_WARNING_PATTERN = "^(\\[warn\\]|\\[error\\])\\s*(.*?):(\\d+)(?::\\d+)?:\\s*(.*)$";

    /**
     * Creates a new instance of {@link SbtScalacParser}.
     */
    public SbtScalacParser() {
        super(SBT_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(2))
                .setLineStart(parseInt(matcher.group(3)))
                .setMessage(matcher.group(4))
                .setPriority(mapPriority(matcher))
                .build();
    }

    private Priority mapPriority(final Matcher matcher) {
        if ("[error]".equals(matcher.group(1))) {
            return Priority.HIGH;
        }
        else {
            return Priority.NORMAL;
        }
    }
}
