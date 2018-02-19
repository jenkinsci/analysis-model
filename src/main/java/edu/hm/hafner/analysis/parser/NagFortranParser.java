package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpDocumentParser;

/**
 * A parser for messages from the NAG Fortran Compiler.
 *
 * @author Mat Cross.
 */
public class NagFortranParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 2072414911276743946L;

    private static final String NAGFOR_MSG_PATTERN = "^(Info|Warning|Questionable|Extension|Obsolescent|Deleted "
            + "feature used|Error|Runtime Error|Fatal Error|Panic): (.+\\.[^,:\\n]+)(, line (\\d+))?: (.+($\\s+detected"
            + " at .+)?)";

    /**
     * Creates a new instance of {@link NagFortranParser}.
     */
    public NagFortranParser() {
        super(NAGFOR_MSG_PATTERN, true);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = matcher.group(1);

        return builder.setFileName(matcher.group(2))
                .setLineStart(getLineNumber(matcher))
                .setCategory(category)
                .setMessage(matcher.group(5))
                .setPriority(mapPriority(category))
                .build();
    }

    private Priority mapPriority(final String category) {
        switch (category) {
            case "Error":
            case "Runtime Error":
            case "Fatal Error":
            case "Panic":
                return Priority.HIGH;
            case "Info":
                return Priority.LOW;
            default:
                return Priority.NORMAL;
        }
    }

    private int getLineNumber(final Matcher matcher) {
        int lineNumber;
        if (StringUtils.isEmpty(matcher.group(4))) {
            lineNumber = 0;
        }
        else {
            lineNumber = Integer.parseInt(matcher.group(4));
        }
        return lineNumber;
    }
}
