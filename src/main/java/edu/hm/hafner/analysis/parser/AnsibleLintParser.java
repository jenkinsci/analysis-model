package edu.hm.hafner.analysis.parser;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 8481090596321427484L;

    private static final String ANSIBLE_LINT_WARNING_PATTERN = "(?<file>.*)\\:(?<lineno>[0-9]*)\\:\\s*(\\[(?<cat>[a-zA-Z0-9\\-\\[\\]]+)\\]|(?<newcat>[^\\[][a-zA-Z0-9\\[\\]\\-]+)):?\\s(?<msg>.*)";

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
        final String cat;

        /* Ansible-lint has changed the style of parseable output. This requires
         * distinguishing between rule names in square brackets and rule names
         * containing square brackets. */
        if (matcher.group("cat") != null) {
            cat = matcher.group("cat");
        }
        else {
            cat = matcher.group("newcat");
        }

        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("lineno"))
                .setCategory(cat)
                .setMessage(matcher.group("msg"))
                .buildOptional();
    }
}
