package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link IntelParserTest}.
 */
class IntelParserTest extends AbstractParserTest {
    IntelParserTest() {
        super("intelc.txt");
    }

    @Override
    protected IntelParser createParser() {
        return new IntelParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(9);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Remark")
                .hasLineStart(1460)
                .hasLineEnd(1460)
                .hasMessage("LOOP WAS VECTORIZED.")
                .hasFileName("D:/Hudson/workspace/foo/busdates.cpp")
                .hasColumnStart(20);

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Remark")
                .hasLineStart(2630)
                .hasLineEnd(2630)
                .hasMessage("FUSED LOOP WAS VECTORIZED.")
                .hasFileName("D:/Hudson/workspace/foo/hols.cpp")
                .hasColumnStart(15);

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Remark #1")
                .hasLineStart(721)
                .hasLineEnd(721)
                .hasMessage("last line of file ends without a newline")
                .hasFileName("D:/Hudson/workspace/zoo/oppdend2d_slv_strip_utils.cpp");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Remark #1418")
                .hasLineStart(17)
                .hasLineEnd(17)
                .hasMessage("external function definition with no prior declaration")
                .hasFileName("D:/Hudson/workspace/boo/serviceif.cpp");

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Warning #6843")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage(
                        "A dummy argument with an explicit INTENT(OUT) declaration is not given an explicit value.   [X]")
                .hasFileName("/path/to/file1.f90");

        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Remark #8577")
                .hasLineStart(806)
                .hasLineEnd(806)
                .hasMessage(
                        "The scale factor (k) and number of fractional digits (d) do not have the allowed combination of either -d < k <= 0 or 0 < k < d+2. Expect asterisks as output.")
                .hasFileName("/path/to/file2.f");

        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Error #5082")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("Syntax error, found END-OF-STATEMENT when expecting one of: ( % [ : . = =>")
                .hasFileName("t.f90");

        softly.assertThat(report.get(7))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Error ")
                .hasLineStart(17)
                .hasLineEnd(17)
                .hasMessage("expected a \";\"")
                .hasFileName("main.cpp");

        softly.assertThat(report.get(8))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Message #593")
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasMessage("variable \"var\" was set but never used")
                .hasFileName("D:/path/to/source/file.cpp");
    }

    /**
     * Parses a warning log with 3 warnings and 1 error.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-5402">Issue 5402</a>
     */
    @Test
    void issue5402() {
        Report warnings = parse("issue5402.txt");

        assertThat(warnings).hasSize(4);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Warning #177")
                    .hasLineStart(980)
                    .hasLineEnd(980)
                    .hasMessage("label \"find_rule\" was declared but never referenced")
                    .hasFileName("<stdout>");

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Warning #177")
                    .hasLineStart(2454)
                    .hasLineEnd(2454)
                    .hasMessage("function \"yy_flex_strlen\" was declared but never referenced")
                    .hasFileName("<stdout>");

            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Warning #1786")
                    .hasLineStart(120)
                    .hasLineEnd(120)
                    .hasMessage(
                            "function \"fopen\" (declared at line 237 of \"C:\\Program Files\\Microsoft Visual Studio 9.0\\VC\\INCLUDE\\stdio.h\") was declared \"deprecated (\"This function or variable may be unsafe. Consider using fopen_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.\") \"")
                    .hasFileName(
                            "D:/hudson/workspace/continuous-snext-main-Win32/trunk/src/engine/AllocationProfiler.cpp");

            softly.assertThat(warnings.get(3))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory("Error #1786")
                    .hasLineStart(120)
                    .hasLineEnd(120)
                    .hasMessage(
                            "function \"fopen\" (declared at line 237 of \"C:\\Program Files\\Microsoft Visual Studio 9.0\\VC\\INCLUDE\\stdio.h\") was declared \"deprecated (\"This function or variable may be unsafe. Consider using fopen_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.\") \"")
                    .hasFileName(
                            "D:/hudson/workspace/continuous-snext-main-Win32/trunk/src/engine/AllocationProfiler.cpp");
        }
    }
}
