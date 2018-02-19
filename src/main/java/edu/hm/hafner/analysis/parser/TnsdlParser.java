package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for the tnsdl translator warnings.
 *
 * @author Shaohua Wen
 */
public class TnsdlParser extends FastRegexpLineParser {
    private static final long serialVersionUID = -7740789998865369930L;

    static final String WARNING_CATEGORY = "Error";
    private static final String TNSDL_WARNING_PATTERN = "^tnsdl((.*)?):\\(.*\\) (.*) \\((.*)\\):(.*)$";

    /**
     * Creates a new instance of {@link TnsdlParser}.
     */
    public TnsdlParser() {
        super(TNSDL_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("tnsdl");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(3))
                .setLineStart(parseInt(matcher.group(4)))
                .setCategory(WARNING_CATEGORY)
                .setMessage(matcher.group(5))
                .setPriority(mapPriority(matcher))
                .build();
    }

    private Priority mapPriority(final Matcher matcher) {
        if (matcher.group().contains("(E)")) {
            return Priority.HIGH;
        }
        else {
            return Priority.NORMAL;
        }
    }
}

