package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
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
        super("tnsdl", TNSDL_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("tnsdl");
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String fileName = matcher.group(3);
        int lineNumber = parseInt(matcher.group(4));
        String message = matcher.group(5);
        Priority priority;

        if (matcher.group().contains("(E)")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return issueBuilder().setFileName(fileName).setLineStart(lineNumber).setCategory(WARNING_CATEGORY)
                             .setMessage(message).setPriority(priority).build();
    }
}

