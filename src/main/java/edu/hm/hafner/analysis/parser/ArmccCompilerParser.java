package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for armcc compiler warnings.
 *
 * @author Emanuele Zattin
 */
public class ArmccCompilerParser extends RegexpLineParser {
    private static final long serialVersionUID = -2677728927938443703L;

    private static final String ARMCC_WARNING_PATTERN = "^\"(.+)\", line (\\d+): ([A-Z][a-z]+):\\D*(\\d+)\\D*?:\\s+(.+)$";

    /**
     * Creates a new instance of {@link ArmccCompilerParser}.
     */
    public ArmccCompilerParser() {
        super(ARMCC_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String fileName = matcher.group(1);
        int lineNumber = parseInt(matcher.group(2));
        String type = matcher.group(3);
        int errorCode = parseInt(matcher.group(4));
        String message = matcher.group(5);
        Severity priority;

        if ("error".equalsIgnoreCase(type)) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }

        return builder.setFileName(fileName).setLineStart(lineNumber).setMessage(errorCode + " - " + message)
                      .setSeverity(priority).build();
    }
}

