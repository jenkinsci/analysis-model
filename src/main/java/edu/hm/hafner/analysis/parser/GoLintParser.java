package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the golint tool in the Go toolchain.
 *
 * @author Ryan Cox
 */
public class GoLintParser extends RegexpLineParser {
    private static final long serialVersionUID = -5895416507693444713L;

    // conn.go:360:3: should replace c.writeSeq += 1 with c.writeSeq++
    private static final String GOLINT_WARNING_PATTERN = "^(.*?):(\\d+?):(\\d*?):\\s*(.*)$";

    /**
     * Creates a new instance of {@link GoLintParser}.
     */
    public GoLintParser() {
        super(GOLINT_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(4);
        String category = guessCategory(message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setColumnStart(parseInt(matcher.group(3)))
                .setCategory(category)
                .setMessage(message)
                .build();
    }
}

