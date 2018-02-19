package edu.hm.hafner.analysis.parser;

import java.util.Locale;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for the IAR C/C++ compiler warnings. Note, that since release 4.1 this parser requires that IAR compilers
 * are started with option '----no_wrap_diagnostics'. Then the IAR compilers will create single-line warnings.
 *
 * @author Claus Klein
 * @author Ullrich Hafner
 * @author Kay van der Zander
 */
public class IarParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 7695540852439013425L;

    private static final int GROUP_NUMBER = 5;

    // search for: Fatal Error[Pe1696]: cannot open source file "c:\filename.c"
    // search for: c:\filename.h(17) : Fatal Error[Pe1696]: cannot open source file "System/ProcDef_LPC17xx.h"
    private static final String IAR_WARNING_PATTERN = "((\\[exec\\] )?(.*)\\((\\d+)\\)?.*)?" + "(Fatal "
            + "[Ee]rror|Remark|Warning)\\[(\\w+)\\]: (.*(\\\".*(c|h)\\\")|.*)";

    /**
     * Creates a new instance of {@link IarParser}.
     */
    public IarParser() {
        super(IAR_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning") || line.contains("rror") || line.contains("Remark") || line.contains("[");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        builder.setPriority(determinePriority(matcher.group(GROUP_NUMBER)))
                .setMessage(matcher.group(7));

        if (matcher.group(3) == null) {
            return builder.setFileName(matcher.group(8))
                    .setLineStart(0)
                    .setCategory(matcher.group(6))
                    .build();
        }
        return builder.setFileName(matcher.group(3))
                .setLineStart(parseInt(matcher.group(4)))
                .setCategory(matcher.group(6))
                .build();
    }

    private Priority determinePriority(final String message) {
        String lowerCaseMessage = message.toLowerCase(Locale.ENGLISH);
        if (lowerCaseMessage.contains("error")) {  // for "Fatal error", "Fatal Error", "Error" and "error" and "warning"
            return Priority.HIGH;
        }
        else if (lowerCaseMessage.contains("warning")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }
}
