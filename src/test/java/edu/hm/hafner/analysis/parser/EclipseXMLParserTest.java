package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests for {@link TaglistParser}.
 *
 * @author Jason Faust
 */
class EclipseXMLParserTest extends StructuredFileParserTest {
    EclipseXMLParserTest() {
        super("eclipse-withinfo.xml");
    }

    @Override
    protected EclipseXMLParser createParser() {
        return new EclipseXMLParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("IssueType mismatch: cannot convert from float to Integer")
                .hasCategory(EclipseXMLParser.TYPE);

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(16)
                .hasLineEnd(16)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Dead code")
                .hasCategory(EclipseXMLParser.POTENTIAL_PROGRAMMING_PROBLEM);

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("The value of the local variable x is not used")
                .hasCategory(EclipseXMLParser.UNNECESSARY_CODE);

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(27)
                .hasLineEnd(27)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage(
                        "Statement unnecessarily nested within else clause. The corresponding then clause does not complete normally")
                .hasCategory(EclipseXMLParser.UNNECESSARY_CODE);

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Comparing identical expressions")
                .hasCategory(EclipseXMLParser.POTENTIAL_PROGRAMMING_PROBLEM);

        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("The allocated object is never used")
                .hasCategory(EclipseXMLParser.POTENTIAL_PROGRAMMING_PROBLEM);
    }

    /**
     * Tests that warnings are categorized as {@code Code} or {@code JavaDoc}.
     */
    @Test
    void javadocCategory() {
        var warnings = parse("eclipse-withjavadoc.xml");

        assertThat(warnings).hasSize(5);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(1)
                    .hasMessage("A default nullness annotation has not been specified for the package a")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(EclipseXMLParser.POTENTIAL_PROGRAMMING_PROBLEM);

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(3)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.JAVADOC);

            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(5)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.JAVADOC);

            softly.assertThat(warnings.get(3))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(7)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.JAVADOC);

            softly.assertThat(warnings.get(4))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(8)
                    .hasMessage("The value of the local variable unused is not used")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(EclipseXMLParser.UNNECESSARY_CODE);
        }
    }

    @Test
    void shouldOnlyAcceptXmlFiles() {
        var parser = createParser();

        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.xml"))).isTrue();

        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.txt"))).isFalse();
    }
}
