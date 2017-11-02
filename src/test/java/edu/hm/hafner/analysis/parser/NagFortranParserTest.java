package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link NagFortranParser}.
 */
public class NagFortranParserTest extends ParserTester {
    private static final String TYPE = new NagFortranParser().getId();

    /**
     * Test parsing of a file containing an Info message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testInfoParser() throws IOException {
        Issues warnings = new NagFortranParser().parse(openFile("NagFortranInfo.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                1,
                "Unterminated last line of INCLUDE file",
                "C:/file1.inc",
                TYPE,
                "Info",
                Priority.LOW);
    }

    /**
     * Test parsing of a file containing a Warning message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testWarningParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranWarning.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                5,
                "Procedure pointer F pointer-assigned but otherwise unused",
                "C:/file2.f90",
                TYPE,
                "Warning",
                Priority.NORMAL);
    }

    /**
     * Test parsing of a file containing a Questionable message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testQuestionableParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranQuestionable.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                12,
                "Array constructor has polymorphic element P(5) (but the constructor value will not be polymorphic)",
                "/file3.f90",
                TYPE,
                "Questionable",
                Priority.NORMAL);
    }

    /**
     * Test parsing of a file containing an Extension message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testExtensionParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranExtension.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                9,
                "Left-hand side of intrinsic assignment is allocatable polymorphic variable X",
                "file4.f90",
                TYPE,
                "Extension",
                Priority.NORMAL);
    }

    /**
     * Test parsing of a file containing an Obsolescent message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testObsolescentParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranObsolescent.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                1,
                "Fixed source form",
                "file5.f",
                TYPE,
                "Obsolescent",
                Priority.NORMAL);
    }

    /**
     * Test parsing of a file containing a Deleted fature used message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testDeletedFeatureUsedParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranDeletedFeatureUsed.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                4,
                "assigned GOTO statement",
                "file6.f90",
                TYPE,
                "Deleted feature used",
                Priority.NORMAL);
    }

    /**
     * Test parsing of a file containing an Error message, with no line number, output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testErrorParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranError.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                0,
                "Character function length 7 is not same as argument F (no. 1) in reference to SUB from O8K (expected length 6)",
                "file7.f90",
                TYPE,
                "Error",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing a Runtime Error message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testRuntimeErrorParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranRuntimeError.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                7,
                "Reference to undefined POINTER P",
                "file8.f90",
                TYPE,
                "Runtime Error",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing a Fatal Error message, on multiple lines, output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testFatalErrorParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranFatalError.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                5,
                "SAME_NAME is not a derived type\n             detected at ::@N",
                "file9.f90",
                TYPE,
                "Fatal Error",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing a Panic message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testPanicParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile("NagFortranPanic.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                1,
                "User requested panic",
                "file10.f90",
                TYPE,
                "Panic",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing all categories of message output by the NAG Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testMessageParser() throws IOException {
        Issues warnings =
                new NagFortranParser().parse(openFile());

        assertEquals(10, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                1,
                "Unterminated last line of INCLUDE file",
                "C:/file1.inc",
                TYPE,
                "Info",
                Priority.LOW);
        checkWarning(iterator.next(),
                5,
                "Procedure pointer F pointer-assigned but otherwise unused",
                "C:/file2.f90",
                TYPE,
                "Warning",
                Priority.NORMAL);
        checkWarning(iterator.next(),
                12,
                "Array constructor has polymorphic element P(5) (but the constructor value will not be polymorphic)",
                "/file3.f90",
                TYPE,
                "Questionable",
                Priority.NORMAL);
        checkWarning(iterator.next(),
                9,
                "Left-hand side of intrinsic assignment is allocatable polymorphic variable X",
                "file4.f90",
                TYPE,
                "Extension",
                Priority.NORMAL);
        checkWarning(iterator.next(),
                1,
                "Fixed source form",
                "file5.f",
                TYPE,
                "Obsolescent",
                Priority.NORMAL);
        checkWarning(iterator.next(),
                4,
                "assigned GOTO statement",
                "file6.f90",
                TYPE,
                "Deleted feature used",
                Priority.NORMAL);
        checkWarning(iterator.next(),
                0,
                "Character function length 7 is not same as argument F (no. 1) in reference to SUB from O8K (expected length 6)",
                "file7.f90",
                TYPE,
                "Error",
                Priority.HIGH);
        checkWarning(iterator.next(),
                7,
                "Reference to undefined POINTER P",
                "file8.f90",
                TYPE,
                "Runtime Error",
                Priority.HIGH);
        checkWarning(iterator.next(),
                5,
                "SAME_NAME is not a derived type\n             detected at ::@N",
                "file9.f90",
                TYPE,
                "Fatal Error",
                Priority.HIGH);
        checkWarning(iterator.next(),
                1,
                "User requested panic",
                "file10.f90",
                TYPE,
                "Panic",
                Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "NagFortran.txt";
    }

}
