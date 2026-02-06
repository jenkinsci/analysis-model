package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Location;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.util.LineRange;

import java.util.Iterator;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

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
                .hasFingerprint("fingerprint")
                .hasAdditionalProperties("")
                .hasReference("")
                .hasNoLineRanges()
                .hasLocations(
                        new Location("file-name", 1, 2, 3, 4),
                        new Location("file-name", 5, 6, 0, 0)
                );
    }

    @Override
    protected XmlParser createParser() {
        return new XmlParser();
    }

    @Test
    void shouldHandleLocations() {
        var report = createParser().parseReport(createReaderFactory("xmlParserLocations.xml"));

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
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
                .hasFingerprint("fingerprint")
                .hasAdditionalProperties("")
                .hasNoLineRanges()
                .hasLocations(
                        new Location("file-name", 1, 2, 3, 4),
                        new Location("another-file-name", 5, 6, 0, 0)
                );
    }

    @Test
    void shouldParseWithCustomPath() {
        var parser = new XmlParser(CUSTOM_PATH);
        var report = parser.parseReport(createReaderFactory(ISSUES_CUSTOM_PATH_FILE));
        var iterator = report.iterator();
        try (var softly = new SoftAssertions()) {
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
        var report = createParser().parseReport(createReaderFactory(ISSUES_INCOMPATIBLE_VALUE));
        Iterator<Issue> iterator = report.iterator();
        try (var softly = new SoftAssertions()) {
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
                    .hasReference("")
                    .hasFingerprint("-")
                    .hasAdditionalProperties("")
                    .hasLocations(new Location("-", 1, 2, 0, 0));
        }
    }
}
