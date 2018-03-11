package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpDocumentParser;

/**
 * A parser for (compile-time) messages from the GNU Fortran Compiler.
 *
 * @author Mat Cross.
 */
public class GnuFortranParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 0L;
    /**
     * The gfortran regex string that follows has been reverse engineered from the show_locus function in
     * gcc/fortran/error.c at r204295. By inspection of the GCC release branches this regex should be compatible with
     * GCC 4.2 and newer.
     */
    private static final String GFORTRAN_MSG_PATTERN = "(?s)^([^\\n]+\\.[^:\\n]+):(\\d+)" // file:line.
            + "(?:\\.(\\d+)(?:-(\\d+))?)?" // Optional column (with optional range).
            + ":\\n" + "(?:    Included at [^\\n]+\\n)*" // Optional "    Included at file:line:", any number of times.
            + "\\n" + "[^\\n]+\\n" // The offending line itself. NOCHECKSTYLE
            + "[^\\n]+\\n" // The '1' and/or '2' corresponding to the column of the error locus.
            + "(Warning|Error|Fatal Error|Internal Error at \\(1\\)):[\\s\\n]([^\\n]+)\\n";
    private static final Pattern LINE_PATTERN = Pattern.compile(" at \\(\\d\\)");

    /**
     * Creates a new instance of {@link GnuFortranParser}.
     */
    public GnuFortranParser() {
        super(GFORTRAN_MSG_PATTERN, true);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = LINE_PATTERN.matcher(matcher.group(5)).replaceAll("");
        return builder.setFileName(matcher.group(1))
                .setColumnStart(parseInt(matcher.group(3)))
                .setColumnEnd(parseInt(matcher.group(4)))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(LINE_PATTERN.matcher(matcher.group(6)).replaceAll(""))
                .setPriority("Warning".equals(category) ? Priority.NORMAL : Priority.HIGH)
                .build();
    }
}
