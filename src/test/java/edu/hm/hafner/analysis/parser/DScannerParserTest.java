package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link DScannerParser}.
 *
 * @author Andre Pany
 */
class DScannerParserTest extends AbstractParserTest {
    DScannerParserTest() {
        super("dscanner-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);
        assertThat(report.getErrorMessages()).isEmpty();

        softly.assertThat(report.get(0))
                .hasCategory("dscanner.style.long_line")
                .hasFileName("./source/runtime/internal/api.d")
                .hasLineStart(24)
                .hasColumnStart(121)
                .hasMessage("Line is longer than 120 characters")
                .hasSeverity(Severity.WARNING_LOW);
        
        softly.assertThat(report.get(1))
            .hasCategory("dscanner.bugs.logic_operator_operands")
            .hasFileName("./source/runtime/internal/api.d")
            .hasLineStart(33)
            .hasColumnStart(1)
            .hasMessage("logic operator operands")
            .hasSeverity(Severity.WARNING_HIGH);
        
        softly.assertThat(report.get(2))
            .hasCategory("dscanner.trust_too_much")
            .hasFileName("./source/runtime/internal/api.d")
            .hasLineStart(34)
            .hasColumnStart(1)
            .hasMessage("trust too much")
            .hasSeverity(Severity.WARNING_LOW);
        
        softly.assertThat(report.get(3))
            .hasCategory("dscanner.suspicious.unused_variable")
            .hasFileName("./source/runtime/internal/api2.d")
            .hasLineStart(314)
            .hasColumnStart(16)
            .hasMessage("Variable library is never used.")
            .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldNotAcceptTextFiles() {
        assertThat(createParser().accepts(createReaderFactory("gcc.txt"))).isFalse();
    }

    @Test
    void shouldHandleIncompleteReports() {
        Report report = createParser().parse(createReaderFactory("dscanner-incomplete-report.json"));
        assertThat(report).hasSize(2);
        assertThat(report.getErrorMessages()).isEmpty();
    }

    @Test
    void shouldThrowParserException() {
        assertThatThrownBy(() -> createParser().parse(createReaderFactory("issues-invalid.json")))
                .isInstanceOf(ParsingException.class);
        assertThatThrownBy(() -> createParser().parse(createReaderFactory("issues-broken.json")))
                .isInstanceOf(ParsingException.class);
    }

    @Override
    protected DScannerParser createParser() {
        return new DScannerParser();
    }
}
