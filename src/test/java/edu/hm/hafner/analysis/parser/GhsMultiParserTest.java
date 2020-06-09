package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link GhsMultiParser}.
 */
class GhsMultiParserTest extends AbstractParserTest {
    GhsMultiParserTest() {
        super("ghsmulti.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.ERROR)
                .hasCategory("#5")
                .hasLineStart(2)
                .hasMessage(
                        "cannot open source input file \"file.h\": No such file or directory\n    #include <file.h>")
                .hasFileName("./maindir/tests/TestCase_0101.cpp");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("#546-D")
                .hasLineStart(37)
                .hasMessage(
                        "transfer of control bypasses initialization of:\n            variable \"CF_TRY_FLAG\" (declared at line 42)\n            variable \"CF_EXCEPTION_NOT_CAUGHT\" (declared at line 42)\n        CF_TRY_CHECK_EX(ex2);")
                .hasFileName("./maindir/tests/TestCase_0101.cpp");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("#177-D")
                .hasLineStart(29)
                .hasMessage(
                        "label\n          \"CF_TRY_LABELex1\" was declared but never referenced\n     CF_TRY_EX(ex1)")
                .hasFileName("./maindir/tests/TestCase_0101.cpp");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("#381-D")
                .hasLineStart(9)
                .hasMessage("extra\n          \";\" ignored\n  TEST_DSS( CHECK_4TH_CONFIG_DATA, 18, 142, 'F');")
                .hasFileName("./maindir/tests/TestCase_1601.cpp");

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("#177-D")
                .hasLineStart(23)
                .hasMessage("variable \"myvar\" was declared but never referenced\n  static const uint32 myvar")
                .hasFileName("D:/workspace/TEST/mytest.c");

        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("#11-D")
                .hasLineStart(639)
                .hasColumnStart(10)
                .hasMessage("unrecognized preprocessing directive")
                .hasFileName("D:/workspace/TEST/mytest.c");

    }

    /**
     * Verifies that the file names are correctly parsed.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-59118">JENKINS-59118</a>
     */
    @Test
    void issue59118() {
        Report warnings = parse("issue59118.txt");

        assertThat(warnings).hasSize(2);

        assertThat(warnings.get(0))
                .hasLineStart(19)
                .hasMessage(
                        "[2019-08-28T08:44:26.749Z]           operands of logical && or || must be primary expressions\n"
                                + "\n"
                                + "[2019-08-28T08:44:26.749Z]   #if !defined(_STDARG_H) && !defined(_STDIO_H) && !defined(_GHS_WCHAR_H)")
                .hasFileName("C:/Path/To/bar.h")
                .hasCategory("#1729-D")
                .hasSeverity(Severity.WARNING_NORMAL);
        assertThat(warnings.get(1))
                .hasLineStart(491)
                .hasMessage(
                        "[2019-08-28T08:44:28.122Z]           operands of logical && or || must be primary expressions\n"
                                + "\n"
                                + "[2019-08-28T08:44:28.122Z]                       if(t_deltaInterval != t_u4Interval && t_deltaInterval != 0)")
                .hasFileName("../../../../Sources/Foo/Bar/Test.c")
                .hasCategory("#1729-D")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected GhsMultiParser createParser() {
        return new GhsMultiParser();
    }
}

