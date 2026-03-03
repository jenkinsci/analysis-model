package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import java.util.Iterator;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link LlvmLinkerParser}.
 *
 * @author Steven Scheffler
 */
class LlvmLinkerParserTest extends AbstractParserTest {
    LlvmLinkerParserTest() {
        super("llvm-linker.log");
    }

    @Override
    protected LlvmLinkerParser createParser() {
        return new LlvmLinkerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasMessage("cannot open xyz: No such file or directory")
                .hasFileName("/ld.lld")
                .hasCategory("Linker")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasMessage("undefined symbol: lld::elf::demangle(llvm::StringRef)")
                .hasFileName("/ld.lld-15")
                .hasCategory("Linker")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasMessage("duplicate symbol found")
                .hasFileName("/ld.lld")
                .hasCategory("Linker")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasMessage("creating a DT_TEXTREL in a shared object")
                .hasFileName("/ld.lld")
                .hasCategory("Linker")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldParseVersionedLld() {
        var warnings = parse("versioned-lld.log");
        assertThat(warnings).hasSize(3);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("/ld.lld-15")
                    .hasMessage("cannot find -lmylib");
        }
    }

    @Test
    void shouldIgnoreNonLldLines() {
        var warnings = parse("mixed-output.log");
        assertThat(warnings).hasSize(2);
        assertThat(warnings.stream()).allSatisfy(issue -> assertThat(issue).hasFileName("/ld.lld"));
    }

    @Test
    void issue76141() {
        var warnings = parse("issue76141.log");
        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasMessage("cannot open xyz: No such file or directory")
                    .hasFileName("/ld.lld-15")
                    .hasSeverity(Severity.WARNING_HIGH);
        }
    }
}
