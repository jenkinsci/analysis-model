package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static java.nio.charset.StandardCharsets.*;

/**
 * Tests for {@link TaglistParser}.
 * 
 * @author Jason Faust
 */
class EclipseXMLParserTest extends AbstractIssueParserTest {
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
                .hasColumnStart(13)
                .hasColumnEnd(16)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Type mismatch: cannot convert from float to Integer");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(16)
                .hasLineEnd(16)
                .hasColumnStart(8)
                .hasColumnEnd(40)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Dead code");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasColumnStart(9)
                .hasColumnEnd(9)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("The value of the local variable x is not used");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(27)
                .hasLineEnd(27)
                .hasColumnStart(8)
                .hasColumnEnd(40)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage(
                        "Statement unnecessarily nested within else clause. The corresponding then clause does not complete normally");

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasColumnStart(13)
                .hasColumnEnd(18)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Comparing identical expressions");

        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasColumnStart(1)
                .hasColumnEnd(95)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("The allocated object is never used");
    }

    @Test
    void shouldCountColumns() {
        Report report = parse("eclipse-columns.xml");

        assertThat(report).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(2)
                    .hasLineEnd(2)
                    .hasColumnStart(1)
                    .hasColumnEnd(5)
                    .hasFileName("C:/TEMP/Column.java")
                    .hasMessage("Syntax error on token \"12345\", delete this token");
        });
    }
    
    @Test
    void shouldOnlyAcceptXmlFiles() {
        EclipseXMLParser parser = createParser();
        
        assertThat(parser.accepts(getResourceAsFile("eclipse-columns.xml"), UTF_8)).isTrue();
        assertThat(parser.accepts(getResourceAsFile("eclipse-withinfo.xml"), UTF_8)).isTrue();
        
        assertThat(parser.accepts(getResourceAsFile("eclipse-columns.txt"), UTF_8)).isFalse();
        assertThat(parser.accepts(getResourceAsFile("eclipse-withinfo.txt"), UTF_8)).isFalse();
    }
}
