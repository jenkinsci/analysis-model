package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link GnuFortranParser}.
 */
public class GnuFortranParserTest extends ParserTester {
    private static final String TYPE = new GnuFortranParser().getId();

    /**
     * Test parsing of a file containing a Warning message output by the GNU Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testWarningParser() throws IOException {
        Issues warnings = new GnuFortranParser().parse(openFile("GnuFortranWarning.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                318,
                "Inequality comparison for REAL(8)",
                "C:/zlaror.f",
                TYPE,
                "Warning",
                Priority.NORMAL);
    }

    /**
     * Test parsing of a file containing an Error message output by the GNU Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testErrorParser() throws IOException {
        Issues warnings =
                new GnuFortranParser().parse(openFile("GnuFortranError.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                81, 24,
                "Interface mismatch in dummy procedure 'f': Shape mismatch in dimension 1 of argument 'y'",
                "generic2.f90",
                TYPE,
                "Error",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing a Fatal Error message output by the GNU Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testFatalErrorParser() throws IOException {
        Issues warnings =
                new GnuFortranParser().parse(openFile("GnuFortranFatalError.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                7, 10,
                "Can't open module file 'ieee_arithmetic.mod' for reading: No such file or directory",
                "/path/to/file.f90",
                TYPE,
                "Fatal Error",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing an Internal Error message output by the GNU Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testInternalErrorParser() throws IOException {
        Issues warnings =
                new GnuFortranParser().parse(openFile("GnuFortranInternalError.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                5, 8,
                "free_pi_tree(): Unresolved fixup",
                "linear_algebra_mod.f90",
                TYPE,
                "Internal Error",
                Priority.HIGH);
    }

    /**
     * Test parsing of a file containing all categories of message output by the GNU Fortran Compiler.
     *
     * @throws IOException if the file could not be read.
     */
    @Test
    public void testMessageParser() throws IOException {
        Issues warnings =
                new GnuFortranParser().parse(openFile());

        assertEquals(4, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkWarning(iterator.next(),
                318,
                "Inequality comparison for REAL(8)",
                "C:/zlaror.f",
                TYPE,
                "Warning",
                Priority.NORMAL);
        checkWarning(iterator.next(),
                7, 10,
                "Can't open module file 'ieee_arithmetic.mod' for reading: No such file or directory",
                "/path/to/file.f90",
                TYPE,
                "Fatal Error",
                Priority.HIGH);
        checkWarning(iterator.next(),
                81, 24,
                "Interface mismatch in dummy procedure 'f': Shape mismatch in dimension 1 of argument 'y'",
                "generic2.f90",
                TYPE,
                "Error",
                Priority.HIGH);
        checkWarning(iterator.next(),
                5, 8,
                "free_pi_tree(): Unresolved fixup",
                "linear_algebra_mod.f90",
                TYPE,
                "Internal Error",
                Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "GnuFortran.txt";
    }

}
