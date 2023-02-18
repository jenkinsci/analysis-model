package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link XmlParser}.
 *
 * @author Raphael Furch
 */
class XmlParserTest extends StructuredFileParserTest {
    private static final String ISSUES_DEFAULT_FILE = "xmlParserDefault.xml";
    private static final String ISSUES_CUSTOM_PATH_FILE = "xmlParserCustomPath.xml";
    private static final String ISSUES_INCOMPATIBLE_VALUE = "xmlParserIncompatibleValues.xml";
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
                .hasFingerprint("fingerprint")
                .hasAdditionalProperties("")
                .hasOnlyLineRanges(new LineRange(5, 6));

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
                    .hasFingerprint("fingerprint")
                    .hasAdditionalProperties("")
                    .hasOnlyLineRanges(new LineRange(5, 6));

            softly.assertThat(iterator.next())
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
                    .hasFingerprint("fingerprint")
                    .hasAdditionalProperties("")
                    .hasOnlyLineRanges(new LineRange(42, 43), new LineRange(44, 45));
        }
    }

    @Test
    void shouldAcceptSampleFile() {
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
                    .hasLineStart(1)
                    .hasLineEnd(2)
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
                    .hasNoLineRanges();
        }
    }
}
