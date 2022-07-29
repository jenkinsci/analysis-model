package edu.hm.hafner.analysis.parser;

import java.util.UUID;

import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

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
        assertThat(report).hasSize(5);
        assertThat(report.getErrorMessages()).isEmpty();

        softly.assertThat(report.get(0))
                .hasFileName("test-file.txt")
                .hasOnlyLineRanges(new LineRange(110, 111), new LineRange(120, 121))
                .hasCategory("category")
                .hasDescription("description")
                .hasType("type")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("message")
                .hasPackageName("packageName")
                .hasModuleName("moduleName")
                .hasOrigin("origin")
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
                .hasOnlyLineRanges(new LineRange(110, 110), new LineRange(320, 320))
                .hasDescription("an \"important\" description")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("an \"important\" message");

        softly.assertThat(report.get(3))
                .hasFileName("some.properties")
                .hasLineStart(20)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(4))
                .hasFileName("file.xml")
                .hasOnlyLineRanges(new LineRange(11, 12), new LineRange(21, 22))
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected JsonParser createParser() {
        return new JsonParser();
    }
}
