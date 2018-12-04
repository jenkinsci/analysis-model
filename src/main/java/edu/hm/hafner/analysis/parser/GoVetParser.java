package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import static edu.hm.hafner.analysis.Categories.guessCategory;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the go vet tool in the Go toolchain.
 *
 * @author Ryan Cox
 */
public class GoVetParser extends RegexpLineParser {
    private static final long serialVersionUID = 1451787851164850844L;

    // ui_colored_test.go:59: missing argument for Fatalf("%#v"): format reads arg 2, have only 1 args
    private static final String GOVET_WARNING_PATTERN = "^(.+?):(\\d+?):\\s*(.*)$";

    /**
     * Creates a new instance of {@link GoVetParser}.
     */
    public GoVetParser() {
        super(GOVET_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(guessCategory(matcher.group(3)))
                .setMessage(matcher.group(3))
                .build();
    }
}
