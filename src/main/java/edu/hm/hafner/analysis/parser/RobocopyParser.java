package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for Robocopy.
 *
 * @author Adrian Deccico
 * <p>
 * captured samples: *EXTRA File                  0        b           Unknown Task 100%        New File 0        a.log
 * same                 0        a.log
 */
public class RobocopyParser extends FastRegexpLineParser {
    private static final long serialVersionUID = -671744745118772873L;
    /** Pattern of perforce compiler warnings. */
    private static final String ROBOCOPY_WARNING_PATTERN = "^(.*)(EXTRA File|New File|same)\\s*(\\d*)\\s*(.*)$";

    /**
     * Creates a new instance of {@link RobocopyParser}.
     */
    public RobocopyParser() {
        super(ROBOCOPY_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        String file = matcher.group(4).split("\\s{11}")[0];
        String message = file;
        String category = matcher.group(2);
        return builder.setFileName(file).setLineStart(0).setCategory(category).setMessage(message)
                             .setPriority(Priority.NORMAL).build();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("        ");
    }
}

