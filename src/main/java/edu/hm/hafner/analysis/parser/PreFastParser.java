package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Microsoft PREfast (aka Code Analysis for C/C++) XML files.
 *
 * @author Charles Chan
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms173498.aspx" ></a>
 */
public class PreFastParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1409381677034028504L;

    /*
     * Microsoft PREfast static code analyzer produces XML files with the
     * following schema.
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <DEFECTS>
     *   <DEFECT _seq="1">
     *     <SFA>
     *       <FILEPATH>d:\myproject\</FILEPATH>
     *       <FILENAME>filename.c</FILENAME>
     *       <LINE>102</LINE>
     *       <COLUMN>9</COLUMN>
     *     </SFA>
     *     <DEFECTCODE>28101</DEFECTCODE>
     *     <DESCRIPTION>A long message</DESCRIPTION>
     *     <FUNCTION>DriverEntry</FUNCTION>
     *     <DECORATED>DriverEntry@8</DECORATED>
     *     <FUNCLINE>102</FUNCLINE>
     *     <PATH/>
     *   </DEFECT>
     *   <DEFECT>
     *     ...
     *   </DEFECT>
     * </DEFECTS>
     *
     * The following regular expression performs the following matches:
     * <DEFECT.*?> ... </DEFECT>
     *     - the tag containing 1 violation (seq number ignored)
     * .*?
     *     - zero or more characters
     * <FILENAME>(.+?)</FILENAME>
     *     - capture group 1 to get the filename
     * <LINE>(.+?)</LINE>
     *     - capture group 2 to get the line number
     * <DEFECTCODE>(.+?)</DEFECTCODE>
     *     - capture group 3 to get the error code
     * <DESCRIPTION>(.+?)</DESCRIPTION>
     *     - capture group 4 to get the description
     */
    private static final String PREFAST_PATTERN_WARNING = "<DEFECT.*?>.*?<FILENAME>(.+?)</FILENAME>.*?<LINE>(.+?)"
            + "</LINE>.*?<DEFECTCODE>(.+?)</DEFECTCODE>.*?<DESCRIPTION>(.+?)</DESCRIPTION>.*?</DEFECT>";

    /**
     * Creates a new instance of {@link PreFastParser}.
     */
    public PreFastParser() {
        super(PREFAST_PATTERN_WARNING);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(3))
                .setMessage(matcher.group(4))
                .buildOptional();
    }
}
