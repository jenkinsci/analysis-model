package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpDocumentParser;

/**
 * A parser for messages from the NAG Fortran Compiler.
 *
 * @author Mat Cross.
 */
public class NagFortranParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 0L;
    private static final String NAGFOR_MSG_PATTERN = "^(Info|Warning|Questionable|Extension|Obsolescent|Deleted " +
            "feature used|Error|Runtime Error|Fatal Error|Panic): (.+\\.[^,:\\n]+)(, line (\\d+))?: (.+($\\s+detected" +
            " at .+)?)";

    /**
     * Creates a new instance of {@link NagFortranParser}.
     */
    public NagFortranParser() {
        super("nag-fortran", NAGFOR_MSG_PATTERN, true);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String category = matcher.group(1);
        int lineNumber;
        Priority priority;

        if (StringUtils.isEmpty(matcher.group(4))) {
            lineNumber = 0;
        }
        else {
            lineNumber = Integer.parseInt(matcher.group(4));
        }

        if ("Error".equals(category) || "Runtime Error".equals(category) || "Fatal Error".equals(category) || "Panic"
                .equals(category)) {
            priority = Priority.HIGH;
        }
        else if ("Info".equals(category)) {
            priority = Priority.LOW;
        }
        else {
            priority = Priority.NORMAL;
        }

        return issueBuilder().setFileName(matcher.group(2)).setLineStart(lineNumber).setCategory(category)
                             .setMessage(matcher.group(5)).setPriority(priority).build();
    }
}
