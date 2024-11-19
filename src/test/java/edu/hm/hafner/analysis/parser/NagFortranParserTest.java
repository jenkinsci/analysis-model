package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link NagFortranParser}.
 */
class NagFortranParserTest extends AbstractParserTest {
    NagFortranParserTest() {
        super("NagFortran.txt");
    }

    /**
     * Test parsing of a file containing an Info message output by the NAG Fortran Compiler.
     */
    @Test
    void testInfoParser() {
        var warnings = parse("NagFortranInfo.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 0, 1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/file1.inc")
                    .hasCategory("Info")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasMessage("Unterminated last line of INCLUDE file")
                    .hasLineStart(1);
        }
    }

    /**
     * Test parsing of a file containing a Warning message output by the NAG Fortran Compiler.
     */
    @Test
    void testWarningParser() {
        var warnings = parse("NagFortranWarning.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("C:/file2.f90")
                    .hasCategory("Warning")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Procedure pointer F pointer-assigned but otherwise unused")
                    .hasLineStart(5);
        }
    }

    /**
     * Test parsing of a file containing a Questionable message output by the NAG Fortran Compiler.
     */
    @Test
    void testQuestionableParser() {
        var warnings = parse("NagFortranQuestionable.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("/file3.f90")
                    .hasCategory("Questionable")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage(
                            "Array constructor has polymorphic element P(5) (but the constructor value will not be polymorphic)")
                    .hasLineStart(12);
        }
    }

    /**
     * Test parsing of a file containing an Extension message output by the NAG Fortran Compiler.
     */
    @Test
    void testExtensionParser() {
        var warnings = parse("NagFortranExtension.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file4.f90")
                    .hasCategory("Extension")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Left-hand side of intrinsic assignment is allocatable polymorphic variable X")
                    .hasLineStart(9);
        }
    }

    /**
     * Test parsing of a file containing an Obsolescent message output by the NAG Fortran Compiler.
     */
    @Test
    void testObsolescentParser() {
        var warnings = parse("NagFortranObsolescent.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file5.f")
                    .hasCategory("Obsolescent")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Fixed source form")
                    .hasLineStart(1);
        }
    }

    /**
     * Test parsing of a file containing a Deleted fature used message output by the NAG Fortran Compiler.
     */
    @Test
    void testDeletedFeatureUsedParser() {
        var warnings = parse("NagFortranDeletedFeatureUsed.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file6.f90")
                    .hasCategory("Deleted feature used")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("assigned GOTO statement")
                    .hasLineStart(4);
        }
    }

    /**
     * Test parsing of a file containing an Error message, with no line number, output by the NAG Fortran Compiler.
     */
    @Test
    void testErrorParser() {
        var warnings = parse("NagFortranError.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 1, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file7.f90")
                    .hasCategory("Error")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasMessage(
                            "Character function length 7 is not same as argument F (no. 1) in reference to SUB from O8K (expected length 6)")
                    .hasLineStart(0);
        }
    }

    /**
     * Test parsing of a file containing a Runtime Error message output by the NAG Fortran Compiler.
     */
    @Test
    void testRuntimeErrorParser() {
        var warnings = parse("NagFortranRuntimeError.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 1, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file8.f90")
                    .hasCategory("Runtime Error")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasMessage("Reference to undefined POINTER P")
                    .hasLineStart(7);
        }
    }

    /**
     * Test parsing of a file containing a Fatal Error message, on multiple lines, output by the NAG Fortran Compiler.
     */
    @Test
    void testFatalErrorParser() {
        var warnings = parse("NagFortranFatalError.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 1, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file9.f90")
                    .hasCategory("Fatal Error")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasMessage("SAME_NAME is not a derived type\n             detected at ::@N")
                    .hasLineStart(5);
        }
    }

    /**
     * Test parsing of a file containing a Panic message output by the NAG Fortran Compiler.
     */
    @Test
    void testPanicParser() {
        var warnings = parse("NagFortranPanic.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 1, 0, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("file10.f90")
                    .hasCategory("Panic")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasMessage("User requested panic")
                    .hasLineStart(1);
        }
    }

    /**
     * Test parsing of a file containing a Non-standard(Obsolete) message output by the NAG Fortran Compiler.
     */
    @Test
    void testNonStandardObsoleteParser() {
        var warnings = parse("NagFortranNonStandardObsolete.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("msgs71.f90")
                    .hasCategory("Non-standard(Obsolete)")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("Byte count on numeric data type detected at *@8")
                    .hasLineStart(2);
        }
    }

    /**
     * Test parsing of a file containing a Extension(NAG) message output by the NAG Fortran Compiler.
     */
    @Test
    void testExtensionNAGParser() {
        var warnings = parse("NagFortranExtensionNAG.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("msgs71.f90")
                    .hasCategory("Extension(NAG)")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("FORALL statement detected at 42@<end-of-statement>")
                    .hasLineStart(8);
        }
    }

    /**
     * Test parsing of a file containing a Extension(F2018) message output by the NAG Fortran Compiler.
     */
    @Test
    void testExtensionF2018Parser() {
        var warnings = parse("NagFortranExtensionF2018.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("msgs71.f90")
                    .hasCategory("Extension(F2018)")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("NON_RECURSIVE attribute detected at <end-of-statement>@NON_RECURSIVE")
                    .hasLineStart(5);
        }
    }

    /**
     * Test parsing of a file containing a Extension(F2008) message output by the NAG Fortran Compiler.
     */
    @Test
    void testExtensionF2008Parser() {
        var warnings = parse("NagFortranExtensionF2008.txt");

        assertThat(warnings).hasSize(1);
        assertThatReportHasSeverities(warnings, 0, 0, 1, 0);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("msgs71.f90")
                    .hasCategory("Extension(F2008)")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("NUM_IMAGES intrinsic procedure")
                    .hasLineStart(4);
        }
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(14);
        assertThatReportHasSeverities(report, 0, 4, 9, 1);

        softly.assertThat(report.get(0))
                .hasFileName("C:/file1.inc")
                .hasCategory("Info")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Unterminated last line of INCLUDE file")
                .hasLineStart(1);

        softly.assertThat(report.get(1))
                .hasFileName("C:/file2.f90")
                .hasCategory("Warning")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Procedure pointer F pointer-assigned but otherwise unused")
                .hasLineStart(5);

        softly.assertThat(report.get(2))
                .hasFileName("/file3.f90")
                .hasCategory("Questionable")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Array constructor has polymorphic element P(5) (but the constructor value will not be polymorphic)")
                .hasLineStart(12);

        softly.assertThat(report.get(3))
                .hasFileName("file4.f90")
                .hasCategory("Extension")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Left-hand side of intrinsic assignment is allocatable polymorphic variable X")
                .hasLineStart(9);

        softly.assertThat(report.get(4))
                .hasFileName("file5.f")
                .hasCategory("Obsolescent")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Fixed source form")
                .hasLineStart(1);

        softly.assertThat(report.get(5))
                .hasFileName("file6.f90")
                .hasCategory("Deleted feature used")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("assigned GOTO statement")
                .hasLineStart(4);

        softly.assertThat(report.get(6))
                .hasFileName("file7.f90")
                .hasCategory("Error")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage(
                        "Character function length 7 is not same as argument F (no. 1) in reference to SUB from O8K (expected length 6)")
                .hasLineStart(0);

        softly.assertThat(report.get(7))
                .hasFileName("file8.f90")
                .hasCategory("Runtime Error")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("Reference to undefined POINTER P")
                .hasLineStart(7);

        softly.assertThat(report.get(8))
                .hasFileName("file9.f90")
                .hasCategory("Fatal Error")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("SAME_NAME is not a derived type\n             detected at ::@N")
                .hasLineStart(5);

        softly.assertThat(report.get(9))
                .hasFileName("file10.f90")
                .hasCategory("Panic")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("User requested panic")
                .hasLineStart(1);

        softly.assertThat(report.get(10))
                .hasFileName("msgs71.f90")
                .hasCategory("Non-standard(Obsolete)")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Byte count on numeric data type detected at *@8")
                .hasLineStart(2);

        softly.assertThat(report.get(11))
                .hasFileName("msgs71.f90")
                .hasCategory("Extension(NAG)")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("FORALL statement detected at 42@<end-of-statement>")
                .hasLineStart(8);

        softly.assertThat(report.get(12))
                .hasFileName("msgs71.f90")
                .hasCategory("Extension(F2018)")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("NON_RECURSIVE attribute detected at <end-of-statement>@NON_RECURSIVE")
                .hasLineStart(5);

        softly.assertThat(report.get(13))
                .hasFileName("msgs71.f90")
                .hasCategory("Extension(F2008)")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("NUM_IMAGES intrinsic procedure")
                .hasLineStart(4);
    }

    @Override
    protected NagFortranParser createParser() {
        return new NagFortranParser();
    }
}
