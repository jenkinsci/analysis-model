package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link NagFortranParser}.
 */
public class NagFortranParserTest extends AbstractParserTest {
    NagFortranParserTest() {
        super("NagFortran.txt");
    }

    /**
     * Test parsing of a file containing an Info message output by the NAG Fortran Compiler.
     */
    @Test
    void testInfoParser() {
        Issues<Issue> warnings = parse("NagFortranInfo.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasLowPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("C:/file1.inc")
                .hasCategory("Info")
                .hasPriority(Priority.LOW)
                .hasMessage("Unterminated last line of INCLUDE file")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing a Warning message output by the NAG Fortran Compiler.
     */
    @Test
    void testWarningParser() {
        Issues<Issue> warnings = parse("NagFortranWarning.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasNormalPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("C:/file2.f90")
                .hasCategory("Warning")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Procedure pointer F pointer-assigned but otherwise unused")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing a Questionable message output by the NAG Fortran Compiler.
     */
    @Test
    void testQuestionableParser() {
        Issues<Issue> warnings = parse("NagFortranQuestionable.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasNormalPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("/file3.f90")
                .hasCategory("Questionable")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Array constructor has polymorphic element P(5) (but the constructor value will not be polymorphic)")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing an Extension message output by the NAG Fortran Compiler.
     */
    @Test
    void testExtensionParser() {
        Issues<Issue> warnings = parse("NagFortranExtension.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasNormalPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file4.f90")
                .hasCategory("Extension")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Left-hand side of intrinsic assignment is allocatable polymorphic variable X")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing an Obsolescent message output by the NAG Fortran Compiler.
     */
    @Test
    void testObsolescentParser() {
        Issues<Issue> warnings = parse("NagFortranObsolescent.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasNormalPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file5.f")
                .hasCategory("Obsolescent")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Fixed source form")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing a Deleted fature used message output by the NAG Fortran Compiler.
     */
    @Test
    void testDeletedFeatureUsedParser() {
        Issues<Issue> warnings = parse("NagFortranDeletedFeatureUsed.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasNormalPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file6.f90")
                .hasCategory("Deleted feature used")
                .hasPriority(Priority.NORMAL)
                .hasMessage("assigned GOTO statement")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing an Error message, with no line number, output by the NAG Fortran Compiler.
     */
    @Test
    void testErrorParser() {
        Issues<Issue> warnings = parse("NagFortranError.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasHighPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file7.f90")
                .hasCategory("Error")
                .hasPriority(Priority.HIGH)
                .hasMessage("Character function length 7 is not same as argument F (no. 1) in reference to SUB from O8K (expected length 6)")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing a Runtime Error message output by the NAG Fortran Compiler.
     */
    @Test
    void testRuntimeErrorParser() {
        Issues<Issue> warnings = parse("NagFortranRuntimeError.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasHighPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file8.f90")
                .hasCategory("Runtime Error")
                .hasPriority(Priority.HIGH)
                .hasMessage("Reference to undefined POINTER P")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing a Fatal Error message, on multiple lines, output by the NAG Fortran Compiler.
     */
    @Test
    void testFatalErrorParser() {
        Issues<Issue> warnings = parse("NagFortranFatalError.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasHighPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file9.f90")
                .hasCategory("Fatal Error")
                .hasPriority(Priority.HIGH)
                .hasMessage("SAME_NAME is not a derived type\n             detected at ::@N")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    /**
     * Test parsing of a file containing a Panic message output by the NAG Fortran Compiler.
     */
    @Test
    void testPanicParser() {
        Issues<Issue> warnings = parse("NagFortranPanic.txt");

        assertThat(warnings)
                .hasSize(1)
                .hasHighPrioritySize(1);

        assertSoftly(softly -> softly.assertThat(warnings.get(0))
                .hasFileName("file10.f90")
                .hasCategory("Panic")
                .hasPriority(Priority.HIGH)
                .hasMessage("User requested panic")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(0)
                .hasColumnEnd(0));
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues)
                .hasSize(10)
                .hasHighPrioritySize(4)
                .hasNormalPrioritySize(5)
                .hasLowPrioritySize(1);

        softly.assertThat(issues.get(0))
                .hasFileName("C:/file1.inc")
                .hasCategory("Info")
                .hasPriority(Priority.LOW)
                .hasMessage("Unterminated last line of INCLUDE file")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(1))
                .hasFileName("C:/file2.f90")
                .hasCategory("Warning")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Procedure pointer F pointer-assigned but otherwise unused")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(2))
                .hasFileName("/file3.f90")
                .hasCategory("Questionable")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Array constructor has polymorphic element P(5) (but the constructor value will not be polymorphic)")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(3))
                .hasFileName("file4.f90")
                .hasCategory("Extension")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Left-hand side of intrinsic assignment is allocatable polymorphic variable X")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(4))
                .hasFileName("file5.f")
                .hasCategory("Obsolescent")
                .hasPriority(Priority.NORMAL)
                .hasMessage("Fixed source form")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(5))
                .hasFileName("file6.f90")
                .hasCategory("Deleted feature used")
                .hasPriority(Priority.NORMAL)
                .hasMessage("assigned GOTO statement")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(6))
                .hasFileName("file7.f90")
                .hasCategory("Error")
                .hasPriority(Priority.HIGH)
                .hasMessage("Character function length 7 is not same as argument F (no. 1) in reference to SUB from O8K (expected length 6)")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(7))
                .hasFileName("file8.f90")
                .hasCategory("Runtime Error")
                .hasPriority(Priority.HIGH)
                .hasMessage("Reference to undefined POINTER P")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(8))
                .hasFileName("file9.f90")
                .hasCategory("Fatal Error")
                .hasPriority(Priority.HIGH)
                .hasMessage("SAME_NAME is not a derived type\n             detected at ::@N")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(issues.get(9))
                .hasFileName("file10.f90")
                .hasCategory("Panic")
                .hasPriority(Priority.HIGH)
                .hasMessage("User requested panic")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Override
    protected AbstractParser createParser() {
        return new NagFortranParser();
    }
}
