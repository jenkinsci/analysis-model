package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link GnuFortranParser}.
 */
public class GnuFortranParserTest extends AbstractParserTest {
    GnuFortranParserTest() {
        super("GnuFortran.txt");
    }

    /**
     * Test parsing of a file containing a Warning message output by the GNU Fortran Compiler.
     */
    @Test
    public void testWarningParser() {
        Issues<Issue> warnings = new GnuFortranParser().parse(openFile("GnuFortranWarning.txt"));

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Warning")
                    .hasLineStart(318)
                    .hasLineEnd(318)
                    .hasMessage("Inequality comparison for REAL(8)")
                    .hasFileName("C:/zlaror.f");
        });
    }

    /**
     * Test parsing of a file containing an Error message output by the GNU Fortran Compiler.
     */
    @Test
    public void testErrorParser() {
        Issues<Issue> warnings =
                new GnuFortranParser().parse(openFile("GnuFortranError.txt"));

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Error")
                    .hasLineStart(81)
                    .hasLineEnd(81)
                    .hasMessage("Interface mismatch in dummy procedure 'f': Shape mismatch in dimension 1 of argument 'y'")
                    .hasFileName("generic2.f90")
                    .hasColumnStart(24);
        });
    }

    /**
     * Test parsing of a file containing a Fatal Error message output by the GNU Fortran Compiler.
     */
    @Test
    public void testFatalErrorParser() {
        Issues<Issue> warnings =
                new GnuFortranParser().parse(openFile("GnuFortranFatalError.txt"));

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Fatal Error")
                    .hasLineStart(7)
                    .hasLineEnd(7)
                    .hasMessage("Can't open module file 'ieee_arithmetic.mod' for reading: No such file or directory")
                    .hasFileName("/path/to/file.f90")
                    .hasColumnStart(10);
        });
    }

    /**
     * Test parsing of a file containing an Internal Error message output by the GNU Fortran Compiler.
     */
    @Test
    public void testInternalErrorParser() {
        Issues<Issue> warnings =
                new GnuFortranParser().parse(openFile("GnuFortranInternalError.txt"));

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Internal Error")
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("free_pi_tree(): Unresolved fixup")
                    .hasFileName("linear_algebra_mod.f90")
                    .hasColumnStart(8);
        });
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        Iterator<Issue> iterator = issues.iterator();
        softly.assertThat(issues).hasSize(4);
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("Warning")
                .hasLineStart(318)
                .hasLineEnd(318)
                .hasMessage("Inequality comparison for REAL(8)")
                .hasFileName("C:/zlaror.f");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Fatal Error")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("Can't open module file 'ieee_arithmetic.mod' for reading: No such file or directory")
                .hasFileName("/path/to/file.f90")
                .hasColumnStart(10);

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Error")
                .hasLineStart(81)
                .hasLineEnd(81)
                .hasMessage(
                        "Interface mismatch in dummy procedure 'f': Shape mismatch in dimension 1 of argument 'y'")
                .hasFileName("generic2.f90")
                .hasColumnStart(24);

        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("Internal Error")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("free_pi_tree(): Unresolved fixup")
                .hasFileName("linear_algebra_mod.f90")
                .hasColumnStart(8);
    }

    @Override
    protected AbstractParser createParser() {
        return new GnuFortranParser();
    }
}
