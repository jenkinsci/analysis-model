package edu.hm.hafner.analysis.parser;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link JsonParser}.
 */
class JsonParserTest extends AbstractParserTest {
    JsonParserTest() {
        super("issues.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);
        assertThat(report.getErrorMessages()).isEmpty();

        softly.assertThat(report.get(0))
                .hasFileName("directory/test-file.txt")
                .hasOnlyLineRanges(createLineRange(110, 111), createLineRange(120, 121))
                .hasCategory("category")
                .hasDescription("description")
                .hasType("type")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("message")
                .hasPackageName("packageName")
                .hasModuleName("moduleName")
                .hasOrigin("origin")
                .hasReference("reference")
                .hasFingerprint("9CED6585900DD3CFB97B914A3CEB0E79")
                .hasAdditionalProperties("additionalProperties")
                .hasId(UUID.fromString("e7011244-2dab-4a54-a27b-2d0697f8f813"));

        softly.assertThat(report.get(1))
                .hasFileName("test.xml")
                .hasLineStart(110)
                .hasLineEnd(111)
                .hasColumnStart(210)
                .hasColumnEnd(220)
                .hasDescription("some description")
                .hasSeverity(Severity.ERROR)
                .hasMessage("some message");

        softly.assertThat(report.get(2))
                .hasFileName("test.txt")
                .hasOnlyLineRanges(createLineRange(110, 110), createLineRange(320, 320))
                .hasDescription("an \"important\" description")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("an \"important\" message");

        softly.assertThat(report.get(3))
                .hasFileName("some.properties")
                .hasLineStart(20)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(4))
                .hasFileName("file.xml")
                .hasOnlyLineRanges(createLineRange(11, 12), createLineRange(21, 22))
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    private LineRange createLineRange(final int start, final int end) {
        return new LineRangeBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
    }

    @Test
    void shouldNotAcceptTextFiles() {
        assertThat(createParser().accepts(createReaderFactory("gcc.txt"))).isFalse();
    }

    @Test
    void shouldThrowParserException() {
        assertThatThrownBy(() -> createParser().parse(createReaderFactory("issues-invalid.json")))
                .isInstanceOf(ParsingException.class);
        assertThatThrownBy(() -> createParser().parse(createReaderFactory("issues-broken.json")))
                .isInstanceOf(ParsingException.class);
    }

    @Override
    protected JsonParser createParser() {
        return new JsonParser();
    }
}
