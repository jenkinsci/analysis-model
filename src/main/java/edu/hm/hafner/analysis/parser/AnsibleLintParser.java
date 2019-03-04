package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for Ansible Lint warnings.
 *
 * The parser expects the Ansible Lint output to be in a "parseable output in the format of pep8".
 * Pass the argument {@code -p} to Ansible Lint to get a compatible output.
 *
 * @author Ce Qi
 */
public class AnsibleLintParser extends RegexpLineParser {
    private static final long serialVersionUID = 8481090596321427484L;

    private static final String ANSIBLE_LINT_WARNING_PATTERN = "(.*)\\:([0-9]*)\\:\\s*\\[(.*)\\]\\s(.*)";

    /**
     * Creates a new instance of {@link AnsibleLintParser}.
     */
    public AnsibleLintParser() {
        super(ANSIBLE_LINT_WARNING_PATTERN);
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains("[")) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(3))
                .setMessage(matcher.group(4))
                .buildOptional();
    }
}

