package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * Parser for ProtoLint.
 */
public class ProtoLintParser extends RegexpLineParser {

    private static final long serialVersionUID = -8347619672754062010L;

    private static final String PROTOLINT_PATTERN = "^\\[([^:]+):(\\d+):(\\d+)\\] (.+)$";
    
    /**
     * Creates a new instance.
     */
    public ProtoLintParser() {
        super(PROTOLINT_PATTERN);
    }
    
    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setColumnStart(matcher.group(3))
                .setMessage(matcher.group(4))
                .setSeverity(Severity.WARNING_NORMAL)
                .buildOptional();    
        }

}
