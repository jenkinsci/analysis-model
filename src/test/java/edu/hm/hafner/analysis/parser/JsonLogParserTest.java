package edu.hm.hafner.analysis.parser;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.util.LineRange;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link JsonLogParser}.
 */
class JsonLogParserTest extends AbstractParserTest {
    JsonLogParserTest() {
        super("json-issues.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);
        assertThat(report.getErrorMessages()).isEmpty();

        softly.assertThat(report.get(0))
                .hasFileName("file.txt")
                .hasOnlyLineRanges(new LineRange(20, 21))
                .hasLineStart(10)
                .hasLineEnd(11)
                .hasCategory("c")
                .hasDescription("d")
                .hasType("t")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("msg")
                .hasPackageName("pn")
                .hasModuleName("mdl")
                .hasOrigin("orgn")
                .hasFingerprint("fgpt")
                .hasAdditionalProperties("ap")
                .hasId(UUID.fromString("823b92b6-98eb-41c4-83ce-b6ec1ed6f98f"));

        softly.assertThat(report.get(1))
                .hasFileName("test.txt")
                .hasLineStart(10)
                .hasLineEnd(11)
                .hasColumnStart(110)
                .hasColumnEnd(120)
                .hasDescription("some description")
                .hasSeverity(Severity.ERROR)
                .hasMessage("some message");

        softly.assertThat(report.get(2))
                .hasFileName("test.txt")
                .hasOnlyLineRanges(new LineRange(220, 220))
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasDescription("an \"important\" description")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("an \"important\" message");

        softly.assertThat(report.get(3))
                .hasFileName("some.properties")
                .hasLineStart(10)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(4))
                .hasFileName("file.xml")
                .hasOnlyLineRanges(new LineRange(21, 22))
                .hasLineStart(11)
                .hasLineEnd(12)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldNotAcceptXmlAndJsonFiles() {
        assertThat(createParser().accepts(createReaderFactory("xmlParserDefault.xml"))).isFalse();
        assertThat(createParser().accepts(createReaderFactory("issues.json"))).isFalse();
    }

    @Test
    void shouldReportDuplicateKey() {
        Report report = parse("json-issues-duplicate.log");
        assertThat(report).hasSize(0);
        assertThat(report.getErrorMessages()).contains(
                "Could not parse line: «{\"fileName\":\"invalid1.xml\",\"fileName\":\"invalid2.xml\"}»");
    }

    @Test
    void shouldReportLineBreak() {
        Report report = parse("json-issues-lineBreak.log");
        assertThat(report).hasSize(0);
        assertThat(report.getErrorMessages()).contains(
                "Could not parse line: «\"description\":\"an \\\"important\\\" description\"}»");
    }

    @Test
    void emptyReport() {
        JsonLogParser parser = createParser();
        Report report = parser.parse(createReaderFactory("json-issues-empty.txt"));
        assertThat(report).hasSize(0);
    }

    @Override
    protected JsonLogParser createParser() {
        return new JsonLogParser();
    }
}
