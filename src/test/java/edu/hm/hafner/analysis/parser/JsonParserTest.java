package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.util.LineRange;

import java.util.UUID;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link JsonParser}.
 */
class JsonParserTest extends StructuredFileParserTest {
    JsonParserTest() {
        super("issues.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        verifyReport(report);
    }

    private void verifyReport(final Report report) {
        assertThat(report).hasSize(5);
        assertThat(report.getErrorMessages()).isEmpty();

        assertThat(report.get(0))
                .hasFileName("test-file.txt")
                .hasOnlyLineRanges(new LineRange(120, 121))
                .hasLineStart(110)
                .hasLineEnd(111)
                .hasCategory("category")
                .hasDescription("description")
                .hasType("type")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("message")
                .hasPackageName("packageName")
                .hasModuleName("moduleName")
                .hasFingerprint("9CED6585900DD3CFB97B914A3CEB0E79")
                .hasAdditionalProperties("additionalProperties")
                .hasId(UUID.fromString("e7011244-2dab-4a54-a27b-2d0697f8f813"));

        assertThat(report.get(1))
                .hasFileName("test.xml")
                .hasLineStart(110)
                .hasLineEnd(111)
                .hasColumnStart(210)
                .hasColumnEnd(220)
                .hasDescription("some description")
                .hasSeverity(Severity.ERROR)
                .hasMessage("some message");

        assertThat(report.get(2))
                .hasFileName("test.txt")
                .hasOnlyLineRanges(new LineRange(320, 320))
                .hasLineStart(110)
                .hasLineEnd(110)
                .hasDescription("an \"important\" description")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("an \"important\" message");

        assertThat(report.get(3))
                .hasFileName("some.properties")
                .hasLineStart(20)
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(4))
                .hasFileName("file.xml")
                .hasOnlyLineRanges(new LineRange(21, 22))
                .hasLineStart(11)
                .hasLineEnd(12)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /** The same report as issues.json but with locations instead of line ranges. */
    @Test
    void shouldReadLocations() {
        verifyReport(parse("issues-location.json"));     }

    @Override
    protected JsonParser createParser() {
        return new JsonParser();
    }
}
