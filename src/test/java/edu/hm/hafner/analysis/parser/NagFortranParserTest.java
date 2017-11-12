package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link NagFortranParser}.
 */
@SuppressWarnings("ReuseOfLocalVariable")
public class NagFortranParserTest extends ParserTester {
    private static final String TYPE = new NagFortranParser().getId();

    /**
     * Test parsing of a file containing an Info message output by the NAG Fortran Compiler.
     */
    @Test
    public void testInfoParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranInfo.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasLowPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing a Warning message output by the NAG Fortran Compiler.
     */
    @Test
    public void testWarningParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranWarning.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing a Questionable message output by the NAG Fortran Compiler.
     */
    @Test
    public void testQuestionableParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranQuestionable.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing an Extension message output by the NAG Fortran Compiler.
     */
    @Test
    public void testExtensionParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranExtension.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing an Obsolescent message output by the NAG Fortran Compiler.
     */
    @Test
    public void testObsolescentParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranObsolescent.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing a Deleted fature used message output by the NAG Fortran Compiler.
     */
    @Test
    public void testDeletedFeatureUsedParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranDeletedFeatureUsed.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasNormalPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing an Error message, with no line number, output by the NAG Fortran Compiler.
     */
    @Test
    public void testErrorParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranError.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasHighPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing a Runtime Error message output by the NAG Fortran Compiler.
     */
    @Test
    public void testRuntimeErrorParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranRuntimeError.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasHighPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing a Fatal Error message, on multiple lines, output by the NAG Fortran Compiler.
     */
    @Test
    public void testFatalErrorParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranFatalError.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasHighPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing a Panic message output by the NAG Fortran Compiler.
     */
    @Test
    public void testPanicParser() {
        Issues<Issue> warnings = new NagFortranParser().parse(openFile("NagFortranPanic.txt"));

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(1);
            softly.assertThat(warnings).hasHighPrioritySize(1);

            Issue warning = warnings.get(0);
            softly.assertThat(warning)
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
        });
    }

    /**
     * Test parsing of a file containing all categories of message output by the NAG Fortran Compiler.
     */
    @Test
    public void testMessageParser() {
        Issues<Issue> warnings =
                new NagFortranParser().parse(openFile());

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(10);
            softly.assertThat(warnings).hasHighPrioritySize(4);
            softly.assertThat(warnings).hasNormalPrioritySize(5);
            softly.assertThat(warnings).hasLowPrioritySize(1);

            Iterator<Issue> iterator = warnings.iterator();

            Issue warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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
        });
    }

    @Override
    protected String getWarningsFile() {
        return "NagFortran.txt";
    }

}
