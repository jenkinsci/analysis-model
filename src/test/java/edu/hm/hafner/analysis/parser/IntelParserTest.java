package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;


import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

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

        assertThat(warnings).hasSize(7);

        Iterator<Issue> iterator = warnings.iterator();

        Issue firstAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(firstAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Remark")
                    .hasLineStart(1460)
                    .hasLineEnd(1460)
                    .hasMessage("LOOP WAS VECTORIZED.")
                    .hasFileName("D:/Hudson/workspace/foo/busdates.cpp")
                    .hasColumnStart(20)
                    .hasType(TYPE);


        });

        Issue secondAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(secondAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Remark")
                    .hasLineStart(2630)
                    .hasLineEnd(2630)
                    .hasMessage("FUSED LOOP WAS VECTORIZED.")
                    .hasFileName("D:/Hudson/workspace/foo/hols.cpp")
                    .hasColumnStart(15)
                    .hasType(TYPE);


        });

        Issue thirdAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(thirdAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Remark #1")
                    .hasLineStart(721)
                    .hasLineEnd(721)
                    .hasMessage("last line of file ends without a newline")
                    .hasFileName("D:/Hudson/workspace/zoo/oppdend2d_slv_strip_utils.cpp")
                    .hasType(TYPE);


        });

        Issue fourthAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(fourthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Remark #1418")
                    .hasLineStart(17)
                    .hasLineEnd(17)
                    .hasMessage("external function definition with no prior declaration")
                    .hasFileName("D:/Hudson/workspace/boo/serviceif.cpp")
                    .hasType(TYPE);


        });


        Issue fifthAnnotation = iterator.next();
        // Messages from the Fortran compiler:
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(fifthAnnotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Warning #6843")
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("A dummy argument with an explicit INTENT(OUT) declaration is not given an explicit value.   [X]")
                    .hasFileName("/path/to/file1.f90")
                    .hasType(TYPE);


        });

        Issue sixthAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(sixthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Remark #8577")
                    .hasLineStart(806)
                    .hasLineEnd(806)
                    .hasMessage("The scale factor (k) and number of fractional digits (d) do not have the allowed combination of either -d < k <= 0 or 0 < k < d+2. Expect asterisks as output.")
                    .hasFileName("/path/to/file2.f")
                    .hasType(TYPE);


        });

        Issue seventhAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(seventhAnnotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Error #5082")
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Syntax error, found END-OF-STATEMENT when expecting one of: ( % [ : . = =>")
                    .hasFileName("t.f90")
                    .hasType(TYPE);


        });

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

        assertThat(warnings).hasSize(4);
        Iterator<Issue> iterator = warnings.iterator();

        Issue firstAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(firstAnnotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Warning #177")
                    .hasLineStart(980)
                    .hasLineEnd(980)
                    .hasMessage("label \"find_rule\" was declared but never referenced")
                    .hasFileName("<stdout>")
                    .hasType(TYPE);


        });

        Issue secondAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(secondAnnotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Warning #177")
                    .hasLineStart(2454)
                    .hasLineEnd(2454)
                    .hasMessage("function \"yy_flex_strlen\" was declared but never referenced")
                    .hasFileName("<stdout>")
                    .hasType(TYPE);


        });

        Issue thirdAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(thirdAnnotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Warning #1786")
                    .hasLineStart(120)
                    .hasLineEnd(120)
                    .hasMessage("function \"fopen\" (declared at line 237 of \"C:\\Program Files\\Microsoft Visual Studio 9.0\\VC\\INCLUDE\\stdio.h\") was declared \"deprecated (\"This function or variable may be unsafe. Consider using fopen_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.\") \"")
                    .hasFileName("D:/hudson/workspace/continuous-snext-main-Win32/trunk/src/engine/AllocationProfiler.cpp")
                    .hasType(TYPE);


        });

        Issue fourthAnnotation = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(fourthAnnotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Error #1786")
                    .hasLineStart(120)
                    .hasLineEnd(120)
                    .hasMessage("function \"fopen\" (declared at line 237 of \"C:\\Program Files\\Microsoft Visual Studio 9.0\\VC\\INCLUDE\\stdio.h\") was declared \"deprecated (\"This function or variable may be unsafe. Consider using fopen_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.\") \"")
                    .hasFileName("D:/hudson/workspace/continuous-snext-main-Win32/trunk/src/engine/AllocationProfiler.cpp")
                    .hasType(TYPE);


        });

    }

    @Override
    protected String getWarningsFile() {
        return "intelc.txt";
    }
}

