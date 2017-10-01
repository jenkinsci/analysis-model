package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for the IAR C/C++ compiler warnings. Note, that since release 4.1 this parser requires that IAR compilers
 * are started with option '----no_wrap_diagnostics'. Then the IAR compilers will create single-line warnings.
 *
 * @author Claus Klein
 * @author Ulli Hafner
 * @author Kay van der Zander
 */
public class IarParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 7695540852439013425L;
    private static int GROUP_NUMBER = 5;

    // search for: Fatal Error[Pe1696]: cannot open source file "c:\filename.c"
    // search for: c:\filename.h(17) : Fatal Error[Pe1696]: cannot open source file "System/ProcDef_LPC17xx.h"
    private static final String IAR_WARNING_PATTERN = "((\\[exec\\] )?(.*)\\((\\d+)\\)?.*)?" + "(Fatal " +
            "[Ee]rror|Remark|Warning)\\[(\\w+)\\]: (.*(\\\".*(c|h)\\\")|.*)";

    /**
     * Creates a new instance of {@link IarParser}.
     */
    public IarParser() {
        super("iar", IAR_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning") || line.contains("rror") || line.contains("Remark") || line.contains("[");
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        Priority priority = determinePriority(matcher.group(GROUP_NUMBER));

        return composeWarning(matcher, priority);
    }

    private Issue composeWarning(final Matcher matcher, final Priority priority) {
        String message = matcher.group(7);

        if (matcher.group(3) == null) {
            return issueBuilder().setFileName(matcher.group(8)).setLineStart(0).setCategory(matcher.group(6))
                                 .setMessage(message).setPriority(priority).build();
        }
        return issueBuilder().setFileName(matcher.group(3)).setLineStart(parseInt(matcher.group(4)))
                             .setCategory(matcher.group(6)).setMessage(message).setPriority(priority).build();
    }

    private Priority determinePriority(final String message) {
        // for "Fatal error", "Fatal Error", "Error" and "error" and "warning"
        if (message.toLowerCase().contains("error")) {
            return Priority.HIGH;
        }
        else if (message.toLowerCase().contains("warning")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }
}
