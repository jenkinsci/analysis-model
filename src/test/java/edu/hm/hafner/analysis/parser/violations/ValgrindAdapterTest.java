package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import se.bjurr.violations.lib.model.Violation;

/**
 * Tests the class {@link ValgrindAdapter}.
 *
 * @author Tony Ciavarella
 */
class ValgrindAdapterTest extends AbstractParserTest {
    ValgrindAdapterTest() {
        super("valgrind.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(3))
                .hasCategory("valgrind:memcheck")
                .hasMessage("Conditional jump or move depends on uninitialised value(s)")
                .hasFileName("/home/some_user/terrible_program/terrible_program.cpp")
                .hasType("UninitCondition")
                .hasLineStart(5)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(2))
                .hasCategory("valgrind:memcheck")
                .hasMessage("Invalid write of size 4")
                .hasFileName("/home/some_user/terrible_program/terrible_program.cpp")
                .hasType("InvalidWrite")
                .hasLineStart(10)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(4))
                .hasCategory("valgrind:memcheck")
                .hasMessage("16 bytes in 1 blocks are definitely lost in loss record 1 of 1")
                .hasFileName("/home/some_user/terrible_program/terrible_program.cpp")
                .hasType("Leak_DefinitelyLost")
                .hasLineStart(3)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1))
                .hasCategory("valgrind:memcheck")
                .hasMessage("Syscall param write(buf) points to uninitialised byte(s)")
                .hasFileName("/home/buildozer/aports/main/musl/src/1.2.4/src/thread/x86_64/syscall_cp.s")
                .hasType("SyscallParam")
                .hasLineStart(29)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(0))
                .hasCategory("valgrind:memcheck")
                .hasMessage("Some type of error without a stack trace")
                .hasFileName(Violation.NO_FILE)
                .hasType("Not_A_Real_Error")
                .hasLineStart(Violation.NO_LINE)
                .hasSeverity(Severity.WARNING_HIGH);

        report.forEach(
                issue -> {
                    final String description = issue.getDescription();
                    if (Violation.NO_FILE.equals(issue.getFileName())) {
                        softly.assertThat(description).doesNotContain("Primary Stack Trace");
                    }
                    else {
                        softly.assertThat(description).contains("Primary Stack Trace", "&lt;insert_a_suppression_name_here&gt;");
                    }
                }
        );
    }

    @Override
    protected ValgrindAdapter createParser() {
        return new ValgrindAdapter();
    }
}
