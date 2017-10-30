package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueAssertSoft;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.IssuesAssert;
import edu.hm.hafner.analysis.Priority;

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
        IssuesAssert.assertThat(warnings).hasSize(1);

        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(warnings.iterator().next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("Warning")
                .hasLineStart(318)
                .hasLineEnd(318)
                .hasMessage("Inequality comparison for REAL(8)")
                .hasFileName("C:/zlaror.f")
                .hasType(TYPE);
        softly.assertAll();
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

        IssuesAssert.assertThat(warnings).hasSize(1);

        // NOCHECKSTYLE
        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(warnings.iterator().next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Error")
                .hasLineStart(81)
                .hasLineEnd(81)
                .hasMessage("Interface mismatch in dummy procedure 'f': Shape mismatch in dimension 1 of argument 'y'")
                .hasFileName("generic2.f90")
                .hasColumnStart(24)
                .hasType(TYPE);
        softly.assertAll();
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

        IssuesAssert.assertThat(warnings).hasSize(1);

        // NOCHECKSTYLE
        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(warnings.iterator().next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Fatal Error")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("Can't open module file 'ieee_arithmetic.mod' for reading: No such file or directory")
                .hasFileName("/path/to/file.f90")
                .hasColumnStart(10)
                .hasType(TYPE);
        softly.assertAll();
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

        IssuesAssert.assertThat(warnings).hasSize(1);

        // NOCHECKSTYLE
        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(warnings.iterator().next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Internal Error")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("free_pi_tree(): Unresolved fixup")
                .hasFileName("linear_algebra_mod.f90")
                .hasColumnStart(8)
                .hasType(TYPE);
        softly.assertAll();
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

        IssuesAssert.assertThat(warnings).hasSize(4);

        Iterator<Issue> iterator = warnings.iterator();

        IssueAssertSoft softly = new IssueAssertSoft();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("Warning")
                .hasLineStart(318)
                .hasLineEnd(318)
                .hasMessage("Inequality comparison for REAL(8)")
                .hasFileName("C:/zlaror.f")
                .hasType(TYPE);
        softly.assertAll();

        // NOCHECKSTYLE
        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Fatal Error")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("Can't open module file 'ieee_arithmetic.mod' for reading: No such file or directory")
                .hasFileName("/path/to/file.f90")
                .hasColumnStart(10)
                .hasType(TYPE);
        softly.assertAll();

        // NOCHECKSTYLE
        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Error")
                .hasLineStart(81)
                .hasLineEnd(81)
                .hasMessage("Interface mismatch in dummy procedure 'f': Shape mismatch in dimension 1 of argument 'y'")
                .hasFileName("generic2.f90")
                .hasColumnStart(24)
                .hasType(TYPE);
        softly.assertAll();

        // NOCHECKSTYLE
        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Internal Error")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("free_pi_tree(): Unresolved fixup")
                .hasFileName("linear_algebra_mod.f90")
                .hasColumnStart(8)
                .hasType(TYPE);
        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "GnuFortran.txt";
    }

}
