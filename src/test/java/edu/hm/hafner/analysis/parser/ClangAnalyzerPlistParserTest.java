package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests for {@link TaglistParser}.
 */
class ClangAnalyzerPlistParserTest extends AbstractParserTest {
    ClangAnalyzerPlistParserTest() {
        super("clang-analyzer-test.txt");
    }

    @Override
    protected ClangAnalyzerPlistParser createParser() {
        return new ClangAnalyzerPlistParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("/src/main.c")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("1st function call argument is an uninitialized value")
                .hasLineStart(538)
                .hasColumnStart(5)
                .hasCategory("Logic error")
                .hasType("Uninitialized argument value");

        softly.assertThat(report.get(1))
                .hasFileName("/src/main.c")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("1st function call argument is an uninitialized value")
                .hasLineStart(553)
                .hasColumnStart(4)
                .hasCategory("Logic error")
                .hasType("Uninitialized argument value");

        softly.assertThat(report.get(2))
                .hasFileName("/src/main.c")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("The left operand of '&' is a garbage value")
                .hasLineStart(1564)
                .hasColumnStart(16)
                .hasCategory("Logic error")
                .hasType("Result of operation is garbage or undefined");
    }

    @Test
    void shouldOnlyAcceptXmlFiles() {
        var parser = createParser();

        assertThat(parser.accepts(createReaderFactory("clang-analyzer-test.txt"))).isTrue();

        assertThat(parser.accepts(createReaderFactory("clang-analyzer-bad.txt"))).isFalse();
    }
}
