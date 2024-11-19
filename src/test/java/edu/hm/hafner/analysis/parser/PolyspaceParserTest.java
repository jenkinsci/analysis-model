package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PolyspaceParser}.
 *
 *  @author Eva Habeeb
 */
class PolyspaceParserTest extends AbstractParserTest {
    PolyspaceParserTest() {
        super("polyspace.csv");
    }

    @Override
    protected PolyspaceParser createParser() {
        return new PolyspaceParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(9);

        softly.assertThat(report.get(0))
                .hasLineStart(222)
                .hasMessage("Check: Null pointer Impact: High")
                .hasModuleName("calculate()")
                .hasCategory("memory")
                .hasColumnStart(49)
                .hasDescription("Defect")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1))
                .hasLineStart(276)
                .hasDescription("Defect")
                .hasMessage("Check: Qualifier removed in conversion Impact: Low")
                .hasCategory("Programming")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(5))
                .hasLineStart(512)
                .hasDescription("MISRA C:2012")
                .hasMessage("Check: 11.1 Conversions shall not be performed between a pointer to a function and any other type. Category: Required")
                .hasModuleName("tester()")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void polyspaceCPTest() {
        var warnings = parse("polyspace_cp.csv");
        assertThat(warnings).hasSize(4);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasLineStart(30)
                    .hasFileName("D:/workspace/math.c")
                    .hasCategory("Data flow")
                    .hasDescription("Run-time Check")
                    .hasMessage("Check: Unreachable code")
                    .hasModuleName("xinitialize()")
                    .hasColumnStart(4)
                    .hasSeverity(Severity.WARNING_HIGH);
            softly.assertThat(warnings.get(1)).hasLineStart(34)
                    .hasFileName("D:/sample.h")
                    .hasCategory("Data flow")
                    .hasDescription("Run-time Check")
                    .hasModuleName("method_a()")
                    .hasColumnStart(4)
                    .hasSeverity(Severity.WARNING_NORMAL);
            softly.assertThat(warnings.get(2)).hasLineStart(66)
                    .hasDescription("Run-time Check")
                    .hasModuleName("errorCheck()")
                    .hasColumnStart(4)
                    .hasSeverity(Severity.WARNING_HIGH);
            softly.assertThat(warnings.get(3)).hasLineStart(217)
                    .hasDescription("MISRA C:2012")
                    .hasMessage("Check: 10.1 Operands shall not be of an inappropriate essential type. Category: Required")
                    .hasFileName("/file/SERVICE.c")
                    .hasModuleName("a_message()")
                    .hasColumnStart(27)
                    .hasCategory("10 The essential type model")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }
}
