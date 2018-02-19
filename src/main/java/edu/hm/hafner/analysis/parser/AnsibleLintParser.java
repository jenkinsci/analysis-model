package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;


/**
 * A parser for the ansible lint warnings.
 *
 * @author Ce Qi
 */
public class AnsibleLintParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 8481090596321427484L;

    private static final String ANSIBLE_LINT_WARNING_PATTERN = "(.*)\\:([0-9]*)\\:\\s*\\[.*(ANSIBLE[0-9]*)\\]\\s(.*)";

    /**
     * Creates a new instance of {@link AnsibleLintParser}.
     */
    public AnsibleLintParser() {
        super(ANSIBLE_LINT_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder
                .setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(matcher.group(3))
                .setMessage(matcher.group(4))
                .build();
    }
}

