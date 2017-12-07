package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link IntelParserTest}.
 */
public class IntelParserTest extends ParserTester {
    private static final String TYPE = new IntelParser().getId();

    /**
     * Parses a file of messages from the Intel C and Fortran compilers.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new IntelParser().parse(openFile());

        assertEquals(7, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                1460, 20,
                "LOOP WAS VECTORIZED.",
                "D:/Hudson/workspace/foo/busdates.cpp",
                TYPE, "Remark", Priority.LOW);
        annotation = iterator.next();
        // remark
        checkWarning(annotation,
                2630, 15,
                "FUSED LOOP WAS VECTORIZED.",
                "D:/Hudson/workspace/foo/hols.cpp",
                TYPE, "Remark", Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                721,
                "last line of file ends without a newline",
                "D:/Hudson/workspace/zoo/oppdend2d_slv_strip_utils.cpp",
                TYPE, "Remark #1", Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                17,
                "external function definition with no prior declaration",
                "D:/Hudson/workspace/boo/serviceif.cpp",
                TYPE, "Remark #1418", Priority.LOW);
        annotation = iterator.next();
        // Messages from the Fortran compiler:
        checkWarning(annotation,
                1,
                "A dummy argument with an explicit INTENT(OUT) declaration is not given an explicit value.   [X]",
                "/path/to/file1.f90",
                TYPE, "Warning #6843", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                806,
                "The scale factor (k) and number of fractional digits (d) do not have the allowed combination of either -d < k <= 0 or 0 < k < d+2. Expect asterisks as output.",
                "/path/to/file2.f",
                TYPE, "Remark #8577", Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                1,
                "Syntax error, found END-OF-STATEMENT when expecting one of: ( % [ : . = =>",
                "t.f90",
                TYPE, "Error #5082", Priority.HIGH);
    }

    /**
     * Parses a warning log with 3 warnings and 1 error.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-5402">Issue 5402</a>
     */
    @Test
    public void issue5402() throws IOException {
        Issues warnings = new IntelParser().parse(openFile("issue5402.txt"));

        assertEquals(4, warnings.size());
        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                980,
                "label \"find_rule\" was declared but never referenced",
                "<stdout>",
                TYPE, "Warning #177", Priority.NORMAL);
        checkWarning(iterator.next(),
                2454,
                "function \"yy_flex_strlen\" was declared but never referenced",
                "<stdout>",
                TYPE, "Warning #177", Priority.NORMAL);
        checkWarning(iterator.next(),
                120,
                "function \"fopen\" (declared at line 237 of \"C:\\Program Files\\Microsoft Visual Studio 9.0\\VC\\INCLUDE\\stdio.h\") was declared \"deprecated (\"This function or variable may be unsafe. Consider using fopen_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.\") \"",
                "D:/hudson/workspace/continuous-snext-main-Win32/trunk/src/engine/AllocationProfiler.cpp",
                TYPE, "Warning #1786", Priority.NORMAL);
        checkWarning(iterator.next(),
                120,
                "function \"fopen\" (declared at line 237 of \"C:\\Program Files\\Microsoft Visual Studio 9.0\\VC\\INCLUDE\\stdio.h\") was declared \"deprecated (\"This function or variable may be unsafe. Consider using fopen_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.\") \"",
                "D:/hudson/workspace/continuous-snext-main-Win32/trunk/src/engine/AllocationProfiler.cpp",
                TYPE, "Error #1786", Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "intelc.txt";
    }
}

