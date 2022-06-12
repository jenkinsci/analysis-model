package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Ansible Lint warnings.
 *
 * <p>
 * The parser expects the Ansible Lint output to be in a "parseable output in the format of pep8".
 * Pass the argument {@code -p} to Ansible Lint to get a compatible output.
 * </p>
 *
 * @author Ce Qi
 */
public class AnsibleLintParser extends LookaheadParser {
    private static final long serialVersionUID = 8481090596321427484L;

    private static final String ANSIBLE_LINT_WARNING_PATTERN = "(.*)\\:([0-9]*)\\:\\s*\\[?([a-zA-Z0-9\\-]+)\\]?:?\\s(.*)";

    /**
     * Creates a new instance of {@link AnsibleLintParser}.
     */
    public AnsibleLintParser() {
        super(ANSIBLE_LINT_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(":");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(3))
                .setMessage(matcher.group(4))
                .buildOptional();
    }
}

