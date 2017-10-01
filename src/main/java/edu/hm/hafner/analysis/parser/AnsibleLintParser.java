package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;


/**
 * A parser for the ansible lint warnings.
 *
 * @author Ce Qi
 */
public class AnsibleLintParser extends FastRegexpLineParser {

    private static final long serialVersionUID = 8481090596321427484L;
    private static final String ANSIBLE_LINT_WARNING_PATTERN = "(.*)\\:([0-9]*)\\:\\s*\\[.*(ANSIBLE[0-9]*)\\]\\s(.*)";

    /**
     * Creates a new instance of {@link AnsibleLintParser}
     */
    public AnsibleLintParser() {
        super("ansible-lint", ANSIBLE_LINT_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String fileName = matcher.group(1);
        String lineNumber = matcher.group(2);
        String category = matcher.group(3);
        String message = matcher.group(4);
        return issueBuilder()
                .setFileName(fileName)
                .setLineStart(parseInt(lineNumber))
                .setCategory(category)
                .setMessage(message)
                .build();
    }
}

