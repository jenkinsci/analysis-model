package edu.hm.hafner.analysis.parser;

import java.util.Iterator;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link XmlParser}.
 *
 * @author Raphael Furch
 */
class XmlParserTest extends AbstractParserTest {
    private static final String ISSUES_DEFAULT_FILE = "xmlParserDefault.xml";
    private static final String ISSUES_CUSTOM_PATH_FILE = "xmlParserCustomPath.xml";
    private static final String ISSUES_INCOMPATIBLE_VALUE = "xmlParserIncompatibleValues.xml";
    private static final String ISSUES_EXCEPTION_FILE = "xmlParserException.xml";
    private static final String CUSTOM_PATH = "/analysisReport/elements/issue";

    /**
     * Creates a new instance of {@link XmlParserTest}.
     */
    XmlParserTest() {
        super(ISSUES_DEFAULT_FILE);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasId(UUID.fromString("63d61b1f-0cac-4e31-8bb9-f390ed1acfe8"))
                .hasFileName("file-name")
                .hasLineStart(1)
                .hasLineEnd(2)
                .hasColumnStart(3)
                .hasColumnEnd(4)
                .hasCategory("category")
                .hasType("type")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("message")
                .hasDescription("description")
                .hasPackageName("package-name")
                .hasModuleName("module-name")
                .hasOrigin("origin")
                .hasReference("reference")
                .hasFingerprint("fingerprint")
                .hasAdditionalProperties("")
                .hasOnlyLineRanges(createLineRange(5, 6));

    }

    private LineRange createLineRange(final int start, final int end) {
        return new LineRangeBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
    }

    @Override
    protected XmlParser createParser() {
        return new XmlParser();
    }

    @Test
    void shouldParseWithCustomPath() {
        XmlParser parser = new XmlParser(CUSTOM_PATH);
        Report report = parser.parse(createReaderFactory(ISSUES_CUSTOM_PATH_FILE));
        Iterator<Issue> iterator = report.iterator();
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(report)
                    .hasSize(2);
            softly.assertThat(iterator.next())
                    .hasId(UUID.fromString("c3b984a3-7f67-4332-953b-27d3943f232c"))
                    .hasFileName("file-name")
                    .hasLineStart(1)
                    .hasLineEnd(2)
                    .hasColumnStart(3)
                    .hasColumnEnd(4)
                    .hasCategory("category")
                    .hasType("type")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasMessage("1")
                    .hasDescription("description")
                    .hasPackageName("package-name")
                    .hasModuleName("module-name")
                    .hasOrigin("origin")
                    .hasReference("reference")
                    .hasFingerprint("fingerprint")
                    .hasAdditionalProperties("")
                    .hasOnlyLineRanges(createLineRange(5, 6));

            softly.assertThat(iterator.next())
                    .hasId(UUID.fromString("fbf2fee0-292f-4991-bd06-d8c5b13ace93"))
                    .hasFileName("file-name")
                    .hasLineStart(5)
                    .hasLineEnd(6)
                    .hasColumnStart(7)
                    .hasColumnEnd(8)
                    .hasCategory("category2")
                    .hasType("type2")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("2")
                    .hasDescription("description")
                    .hasPackageName("package-name")
                    .hasModuleName("module-name")
                    .hasOrigin("origin")
                    .hasReference("reference")
                    .hasFingerprint("fingerprint")
                    .hasAdditionalProperties("")
                    .hasOnlyLineRanges(createLineRange(42, 43), createLineRange(44, 45));
        }
    }

    @Test
    void shouldThrowParserException() {
        assertThatThrownBy(() -> createParser().parse(createReaderFactory(ISSUES_EXCEPTION_FILE)))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void shouldNotAcceptTextFiles() {
        assertThat(createParser().accepts(createReaderFactory("gcc.txt"))).isFalse();
        assertThat(createParser().accepts(createReaderFactory(ISSUES_DEFAULT_FILE))).isTrue();
    }

    @Test
    void shouldProduceIssuesEvenIfThereAreIncompatibleValues() {
        Report report = createParser().parse(createReaderFactory(ISSUES_INCOMPATIBLE_VALUE));
        Iterator<Issue> iterator = report.iterator();
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(report)
                    .hasSize(1);
            softly.assertThat(iterator.next())
                    .hasFileName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
                    .hasCategory("")
                    .hasType("-")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasMessage("")
                    .hasDescription("")
                    .hasPackageName("-")
                    .hasModuleName("-")
                    .hasOrigin("")
                    .hasReference("")
                    .hasFingerprint("-")
                    .hasAdditionalProperties("")
                    .hasOnlyLineRanges(createLineRange(1, 2));
        }
    }
}
