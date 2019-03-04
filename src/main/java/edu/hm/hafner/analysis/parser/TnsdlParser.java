package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the tnsdl translator warnings.
 *
 * @author Shaohua Wen
 */
public class TnsdlParser extends RegexpLineParser {
    private static final long serialVersionUID = -7740789998865369930L;

    private static final String TNSDL_WARNING_PATTERN = "^tnsdl((.*)?):\\(.*\\) (.*) \\((.*)\\):(.*)$";

    /**
     * Creates a new instance of {@link TnsdlParser}.
     */
    public TnsdlParser() {
        super(TNSDL_WARNING_PATTERN);
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains("tnsdl")) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(3))
                .setLineStart(matcher.group(4))
                .setMessage(matcher.group(5))
                .setSeverity(mapPriority(matcher))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {
        if (matcher.group().contains("(E)")) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_NORMAL;
        }
    }
}

