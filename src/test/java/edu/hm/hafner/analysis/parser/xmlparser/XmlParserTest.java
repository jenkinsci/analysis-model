package edu.hm.hafner.analysis.parser.xmlparser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.Assertions;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link XmlParser}.
 *
 * @author Raphael Furch
 */
class XmlParserTest extends AbstractParserTest {

    private static final String ISSUES_DEFAULT_FILE = "xmlParserDefault.xml";
    private static final String ISSUES_CUSTOM_FILE = "xmlParserCustom.xml";
    private static final String ISSUES_EXCEPTION_FILE = "xmlParserException.xml";
    private static final String CUSTOM_PATH = "/errorList/customIssue";

    /**
     * Creates a new instance of {@link XmlParserTest}.
     */
    XmlParserTest() {
        super(ISSUES_DEFAULT_FILE);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasFileName("File 1")
                .hasLineStart(1)
                .hasLineEnd(2)
                .hasColumnStart(3)
                .hasColumnEnd(4)
                .hasCategory("Category 1")
                .hasType("Type 1")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Message 1")
                .hasDescription("Description 1")
                .hasPackageName("Package 1")
                .hasModuleName("Module 1")
                .hasOrigin("Origin 1")
                .hasReference("Reference 1")
                .hasFingerprint("Fingerprint 1")
                .hasAdditionalProperties("Property 1, Property 2");



        softly.assertThat(iterator.next())
                .hasFileName("File 2")
                .hasLineStart(21)
                .hasLineEnd(22)
                .hasColumnStart(23)
                .hasColumnEnd(24)
                .hasCategory("Category 2")
                .hasType("Type 2")
                .hasSeverity(Severity.ERROR)
                .hasMessage("Message 2")
                .hasDescription("Description 2")
                .hasPackageName("Package 2")
                .hasModuleName("Module 2")
                .hasOrigin("Origin 2")
                .hasReference("Reference 2")
                .hasFingerprint("Fingerprint 2")
                .hasAdditionalProperties("Property 21, Property 22");

        softly.assertThat(iterator.next())
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasCategory("-")
                .hasType("-")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("-")
                .hasDescription("-")
                .hasPackageName("-")
                .hasModuleName("-")
                .hasOrigin("-")
                .hasReference("-")
                .hasFingerprint("-")
                .hasAdditionalProperties("-");

    }

    @Override
    protected XmlParser createParser() {
        return new XmlParser();
    }

    @Test
    void shouldParseWithCustomPathAndMapper() {
        XmlParser parser = new XmlParser(CUSTOM_PATH, new CustomXmlMapper());
        Report report = parser.parse(createReaderFactory(ISSUES_CUSTOM_FILE));
        Iterator<Issue> iterator = report.iterator();
        assertSoftly(softly -> {
            softly.assertThat(report)
                    .hasSize(1);
            softly.assertThat(iterator.next())
                    .hasFileName("File 1")
                    .hasLineStart(1)
                    .hasLineEnd(2)
                    .hasColumnStart(3)
                    .hasColumnEnd(4)
                    .hasCategory("Category 1")
                    .hasType("Type 1")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasMessage("Message 1")
                    .hasDescription("Description 1")
                    .hasPackageName("Package 1")
                    .hasModuleName("Module 1")
                    .hasOrigin("Origin 1")
                    .hasReference("Reference 1")
                    .hasFingerprint("Fingerprint 1")
                    .hasAdditionalProperties("Property 1, Property 2");
        });

    }

    @Test
    void shouldThrowParserException() {
        XmlParser parser = new XmlParser();
        Assertions.assertThatThrownBy(() -> parser.parse(createReaderFactory(ISSUES_EXCEPTION_FILE)))
                .isInstanceOf(ParsingException.class);



    }


}
